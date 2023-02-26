package com.bonsai.core.dao;

import com.bonsai.core.annotation.CheckTime;
import com.bonsai.core.annotation.ID;
import com.bonsai.core.annotation.TableColumn;
import com.bonsai.core.annotation.TableName;
import com.google.gson.Gson;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BonsaiDao <T extends BonsaiEntity>{
    private Gson gson = new Gson();
    private JdbcTemplate jdbcTemplate;
    private Class<? extends BonsaiEntity> entityClass;
    private String tableName;
    private boolean hasCheckTime;
    private List<Field> fieldSql;
    private SqlGenerate sqlGenerate;
    private RowMapper<T> rowMapper;
    public BonsaiDao(DataSource dataSource, Class<? extends BonsaiEntity> entityClass){
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.entityClass = entityClass;
        tableName = ((TableName) entityClass.getAnnotation(TableName.class)).name();
        hasCheckTime = ((TableName) entityClass.getAnnotation(TableName.class)).hasCheckTime();
        sqlGenerate = new SqlGenerate();
        Field [] fields = entityClass.getFields();
        fieldSql = new ArrayList<>();
        for(Field field : fields){
            if(!field.isAnnotationPresent(TableColumn.class)) continue;
            if(field.isAnnotationPresent(CheckTime.class) && hasCheckTime == false) continue;
            if(hasCheckTime == false && (field.getName().equalsIgnoreCase("created"))) continue;
            if(hasCheckTime == false && (field.getName().equalsIgnoreCase("updated"))) continue;
            fieldSql.add(field);
        }
        rowMapper = getRowMapper();
    }

    private RowMapper<T> getRowMapper(){
        return new RowMapper<T>() {
            @Override
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                T result = null;
                try {
                    result = (T) entityClass.newInstance();
                    for(Field field : fieldSql){
                        String columnName = ((TableColumn) field.getAnnotation(TableColumn.class)).name();
                        Object value = null;
                        try {
                            value = rs.getObject(columnName);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Class classValue = field.getType();
                        Method setMethod = entityClass.getMethod(sqlGenerate.getMethodSetByField(field), classValue);
                        if(value != null){
                            if(classValue.equals(Boolean.class)){
                                setMethod.invoke(result, rs.getBoolean(columnName));
                            }else if(Number.class.isAssignableFrom(classValue)){
                                setMethod.invoke(result, classValue.getMethod("valueOf", String.class).invoke(classValue, value.toString()));
                            }else if(classValue.isArray()){
//                                setMethod.invoke(result, gson.fromJson(rs.getString(columnName), classValue));
                                field.set(result, gson.fromJson(rs.getString(columnName), classValue));
                            }else if(Collection.class.isAssignableFrom(classValue) || Map.class.isAssignableFrom(classValue)){
                                try {
                                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
//                                    setMethod.invoke(result, gson.fromJson(rs.getString(columnName), parameterizedType));
                                    field.set(result, gson.fromJson(rs.getString(columnName), parameterizedType));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }else if(classValue.equals(String.class)){
                                setMethod.invoke(result, rs.getString(columnName));
                            }
                        }
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
                return result;
            }
        };
    }

    public Long count(){
        String sql = sqlGenerate.count(tableName);
        return jdbcTemplate.query(sql, new ResultSetExtractor<Long>() {
            @Override
            public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                if(rs.next()){
                    return rs.getLong("count");
                }
                return 0L;
            }
        });
    }

    public Long count(String where, Object... params){
        String sql = sqlGenerate.count(where, tableName);
        return jdbcTemplate.query(sql, new ResultSetExtractor<Long>() {
            @Override
            public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                if(rs.next()){
                    return rs.getLong("count");
                }
                return null;
            }
        }, params);
    }

    public List<T> findAll(){
        String sql = sqlGenerate.findAll(tableName);
        return jdbcTemplate.query(sql, rowMapper);
    }

    public T findById(Long id){
        String sql = sqlGenerate.findById(id, tableName);
        return jdbcTemplate.queryForObject(sql, rowMapper);
    }

    public List<T> find(String where, Object... params){
        String sql = sqlGenerate.find(where, tableName);
        return jdbcTemplate.query(sql, rowMapper, params);
    }

    public ResultPaging<T> find(String where, Sort sort, Paging paging, Object... params){
        String sql = sqlGenerate.find(where, sort, paging, tableName);
        ResultPaging result = new ResultPaging();
        result.data = jdbcTemplate.query(sql, rowMapper, params);
        result.count = count(where, params);
        return result;
    }

    public T insert(T entity){
        final String sql = sqlGenerate.add(entity);
        if(sql != null){
            final GeneratedKeyHolder key = new GeneratedKeyHolder();
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                    return ps;
                }
            }, key);
            if(key.getKey() != null){
                entity.id = Long.valueOf(key.getKey().toString());
            }
            return entity;
        }
        return null;
    }

    public T update(T entity){
        String sql = sqlGenerate.update(entity);
        if(sql != null){
            int resultEf = jdbcTemplate.update(sql);
            if(resultEf > 0)
                return entity;
        }
        return null;
    }

    public void delete(Long id){
        String sql = sqlGenerate.delete(id, tableName);
        jdbcTemplate.execute(sql);
    }

    public void deletes(List<Long> ids){
        String sql = sqlGenerate.deletes(ids, tableName);
        jdbcTemplate.execute(sql);
    }
}
