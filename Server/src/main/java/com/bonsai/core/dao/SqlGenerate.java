package com.bonsai.core.dao;

import com.bonsai.core.annotation.*;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlGenerate {
    public String findAll(String tableName){
        return String.format("select * from %s", tableName);
    }

    public String findById(Long id, String tableName){
        return String.format("select * from %s where id = %d", tableName, id);
    }

    public String find(String where, String tableName){
        return String.format("select * from %s where %s", tableName, where);
    }

    public String find(String where, Sort sort, Paging paging, String tableName){
        String sqlSort = "";
        String sqlPaging = "";
        if(sort != null){
            if(sort.getColumnNames() != null && sort.getDirections() != null
                    && sort.getColumnNames().size() == sort.getDirections().size() && sort.getColumnNames().size() != 0){
                sqlSort = " order by ";
                for(int i = 0; i < sort.getColumnNames().size(); i++){
                    if(i == 0){
                        sqlSort += String.format("%s %s ", sort.getColumnNames().get(i), sort.getDirections().get(i).name());
                    }else{
                        sqlSort += String.format(",%s %s ", sort.getColumnNames().get(i), sort.getDirections().get(i).name());
                    }
                }
            }
        }
        if(paging != null){
            if(paging.getPage() >= 0 && paging.getPageSize() > 0){
                sqlPaging = String.format(" limit %d offset %d", paging.getPageSize(), paging.getPage() * paging.getPageSize());
            }
        }
        return String.format("select * from %s where %s %s %s", tableName, where, sqlSort, sqlPaging);
    }

    public String count(String tableName){
        return String.format("select count(*) count from %s", tableName);
    }

    public String count(String where, String tableName){
        return String.format("select count(*) count from %s where %s", tableName, where);
    }

    public <T extends  BonsaiEntity> String add(T entity) {
        try {
            Class entityClass = entity.getClass();
            String tableName = ((TableName) entityClass.getAnnotation(TableName.class)).name();
            boolean hasCheckTime = ((TableName) entityClass.getAnnotation(TableName.class)).hasCheckTime();
            String sql = "insert into %s(%s) values(%s)";
            String keys = "";
            String values = "";
            Field [] fields = entityClass.getFields();
            for(Field field : fields){
                if(field.isAnnotationPresent(ID.class)
                        || !field.isAnnotationPresent(TableColumn.class)
                        || (hasCheckTime == false && (field.isAnnotationPresent(CheckTime.class)))) continue;
                Class classValue = field.getType();
                Method getMethod = entityClass.getMethod(getMethodGetByField(field));

                Object value = getMethod.invoke(entity);
                if(value != null){
                    keys += String.format("%s,", ((TableColumn) field.getAnnotation(TableColumn.class)).name());
                    if(classValue.isArray() || Collection.class.isAssignableFrom(classValue) || Map.class.isAssignableFrom(classValue)){
                        values += "'" + new Gson().toJson(getMethod.invoke(entity)) + "',";
                    }else if(classValue.equals(String.class)){
                        values += "'" + classValue.cast(getMethod.invoke(entity)) + "',";
                    }else{
                        values += classValue.cast(getMethod.invoke(entity)) + ",";
                    }
                }
            }
            keys = keys.substring(0, keys.length() - 1);
            values = values.substring(0, values.length() - 1);
            return String.format(sql, tableName, keys, values);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public <T extends BonsaiEntity> String update(T entity) {
        try {
            String sql = "update %s set %s where %s";
            Class entityClass = entity.getClass();
            String tableName = ((TableName)entityClass.getAnnotation(TableName.class)).name();
            boolean hasCheckTime = ((TableName)entityClass.getAnnotation(TableName.class)).hasCheckTime();
            String setValues = "";
            String where = "";
            Field[] fields = entityClass.getFields();
            for(Field field : fields){
                if(field.isAnnotationPresent(ID.class)){
                    where = " id = " + entityClass.getMethod("getId").invoke(entity);
                    continue;
                }
                if(!field.isAnnotationPresent(TableColumn.class)) continue;
                if(field.isAnnotationPresent(CheckTime.class) && hasCheckTime == false) continue;
                String columnName = ((TableColumn) field.getAnnotation(TableColumn.class)).name();
                Class classValue = field.getType();
                Method getMethod = entityClass.getMethod(getMethodGetByField(field));
                Object value = getMethod.invoke(entity);
                if(value != null){
                    if(classValue.isArray() || Collection.class.isAssignableFrom(classValue) || Map.class.isAssignableFrom(classValue)){
                        setValues += " " + columnName + " = '" + new Gson().toJson(getMethod.invoke(entity)) + "',";
                    }else if(classValue.equals(String.class)){
                        setValues += " " + columnName + " = '" + classValue.cast(getMethod.invoke(entity)) + "',";
                    }else{
                        setValues += " " + columnName + " = " + classValue.cast(getMethod.invoke(entity)) + ",";
                    }
                }
            }
            setValues = setValues.substring(0, setValues.length() - 1);
            return String.format(sql, tableName, setValues, where);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String delete(Long id, String tableName){
        return String.format("delete from %s where id = %d", tableName, id);
    }

    public String deletes(List<Long> ids, String tableName){
        return String.format("delete from %s where id in (%s)", tableName, ids.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",")));
    }

    public String getMethodGetByField(Field field){
        String fieldName = field.getName();
        return String.format("get%s%s", fieldName.substring(0,1).toUpperCase(), fieldName.substring(1, fieldName.length()));
    }

    public String getMethodSetByField(Field field){
        String fieldName = field.getName();
        return String.format("set%s%s", fieldName.substring(0,1).toUpperCase(), fieldName.substring(1, fieldName.length()));
    }
}
