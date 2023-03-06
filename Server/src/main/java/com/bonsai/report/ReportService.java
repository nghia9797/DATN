package com.bonsai.report;

import com.bonsai.report.model.ReportRevenue;
import com.bonsai.report.model.ReportRevenueRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getDataReportRevenue(ReportRevenueRequest requestSearch){
        Map<String,Object> result = new HashMap<>();
        try {
            String where = "";
            Calendar calendar = Calendar.getInstance();
            if(requestSearch.from != null && requestSearch.from > 0){
                calendar.setTimeInMillis(requestSearch.from);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                requestSearch.from = calendar.getTimeInMillis();
                where += " and b.updated >= "+ requestSearch.from;
            }
            if(requestSearch.to != null && requestSearch.to > 0){
                calendar.setTimeInMillis(requestSearch.to);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                requestSearch.to = calendar.getTimeInMillis();
                where += " and b.updated <= "+ requestSearch.to;
            }
            if(requestSearch.treeType != null && requestSearch.treeType >= 0){
                where += " and bd.tree_type = "+ requestSearch.treeType;
            }
            String sql = String.format("select sum(bd.count) total_count,sum(bd.amount) total_amount, bd.tree_name, bd.tree_code, tt.name from bills b, bill_details bd, type_trees tt \n" +
                    "where status = 3 and b.id = bd.bill_id and bd.tree_type = tt.id %s group by bd.tree_code, bd.tree_name, tt.name order by name", where);
            List<ReportRevenue> data = jdbcTemplate.query(sql, new RowMapper<ReportRevenue>() {
                @Override
                public ReportRevenue mapRow(ResultSet rs, int rowNum) throws SQLException {
                    ReportRevenue reportRevenue = new ReportRevenue();
                    reportRevenue.setTotalCount(rs.getLong("total_count"));
                    reportRevenue.setTotalAmount(rs.getLong("total_amount"));
                    reportRevenue.setTreeCode(rs.getString("tree_code"));
                    reportRevenue.setTreeName(rs.getString("tree_name"));
                    reportRevenue.setTreeTypeName(rs.getString("name"));
                    return reportRevenue;
                }
            });
            result.put("items", data);
            sql = String.format("select sum(bd.count) total_count,sum(bd.amount) total_amount, tt.name from bills b, bill_details bd, type_trees tt \n" +
                    "where status = 3 and b.id = bd.bill_id and bd.tree_type = tt.id %s group by  tt.name order by name", where);
            List<Map<String,Object>> types = jdbcTemplate.queryForList(sql);
            result.put("types", types);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
