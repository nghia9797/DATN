package com.bonsai.report;

import com.bonsai.auth.AuthService;
import com.bonsai.common.Contants;
import com.bonsai.common.Response;
import com.bonsai.core.dao.ResultPaging;
import com.bonsai.report.model.ReportRevenue;
import com.bonsai.report.model.ReportRevenueRequest;
import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manage/report")
public class ReportController {
    @Autowired
    private AuthService authService;
    @Autowired
    private ReportService reportService;
    @GetMapping("/revenue")
    public Response getReportRevenue(@RequestParam Map<String,Object> params, HttpServletRequest request){
        Gson gson = new Gson();
        ReportRevenueRequest requestSearch = gson.fromJson(gson.toJson(params), ReportRevenueRequest.class);
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "REPORT:VIEW");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Map<String,Object> result = reportService.getDataReportRevenue(requestSearch);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @GetMapping("/revenue/download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadReportRevenue(@RequestParam Map<String,Object> params, HttpServletRequest request) throws IOException {
        Gson gson = new Gson();
        ReportRevenueRequest requestSearch = gson.fromJson(gson.toJson(params), ReportRevenueRequest.class);
//        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "REPORT:DOWNLOAD");
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "");
        resultCheck.statusCode = Contants.StatusCode.OK;
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Map<String,Object> result = reportService.getDataReportRevenue(requestSearch);
            if(result == null) return null;
            File template =  ResourceUtils.getFile("classpath:static/files/templateReport.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(template));
            XSSFSheet sheet = workbook.getSheet("report");
            Map<String,Object> data = reportService.getDataReportRevenue(requestSearch);
            if(data != null){
                exportData(workbook,sheet, data, requestSearch);
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            workbook.write(output);
            workbook.close();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Description", "File Transfer");
            headers.add("Content-Disposition", "attachment; filename=pfizer_polish_ontology.xlsx");
            headers.add("Content-Transfer-Encoding", "binary");
            headers.add("Connection", "Keep-Alive");
            headers.setContentType(
                    MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));


            InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(output.toByteArray()));
            return ResponseEntity.ok().contentLength(output.size()).headers(headers).body(isr);
        }else return null;
    }

    private void exportData(XSSFWorkbook workbook,XSSFSheet sheet, Map<String, Object> data, ReportRevenueRequest request) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String,Object>> types = (List<Map<String,Object>>) data.get("types");
        List<ReportRevenue> items = (List<ReportRevenue>) data.get("items");
        if(request.getFrom() != null && request.getFrom() > 0){
            XSSFRow rowTime = sheet.getRow(2);
            XSSFCell cell = rowTime.getCell(1);
            if(cell == null) cell = rowTime.createCell(1);
            cell.setCellValue(format.format(new Date(request.from)));
        }
        if(request.getTo() != null && request.getTo() > 0){
            XSSFRow rowTime = sheet.getRow(2);
            XSSFCell cell = rowTime.getCell(3);
            if(cell == null) cell = rowTime.createCell(3);
            cell.setCellValue(format.format(new Date(request.to)));
        }
        XSSFRow rowType = sheet.getRow(3);
        XSSFCell cellType = rowType.getCell(1);
        if(cellType == null) cellType = rowType.createCell(1);
        cellType.setCellValue(request.getTreeTypeName());

        XSSFRow rowReport = sheet.getRow(4);
        XSSFCell cellReport = rowReport.getCell(1);
        if(cellReport == null) cellReport = rowReport.createCell(1);
        cellReport.setCellValue(format.format(new Date()));

        int startIndex = 7;
        long tc = 0;
        long ta = 0;
        XSSFCellStyle styleTypeTree = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        styleTypeTree.setFont(font);
        styleTypeTree.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        styleTypeTree.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        for(Map<String,Object> type : types){
            String typeName = String.valueOf(type.get("name"));
            Long totalCount = Long.valueOf(String.valueOf(type.get("total_count")));
            tc += totalCount;
            Long totalAmount = Long.valueOf(String.valueOf(type.get("total_amount")));
            ta += totalAmount;
            List<ReportRevenue> itemForType = items.stream().filter(new Predicate<ReportRevenue>() {
                @Override
                public boolean test(ReportRevenue reportRevenue) {
                    return reportRevenue.getTreeTypeName().equalsIgnoreCase(typeName);
                }
            }).collect(Collectors.toList());
            if(startIndex == 7){
                sheet.shiftRows(7, sheet.getLastRowNum(), itemForType.size());
            }else{
                sheet.shiftRows(startIndex, sheet.getLastRowNum(), itemForType.size() + 1);
            }
            for(ReportRevenue item : itemForType){
                XSSFRow row = sheet.getRow(startIndex);
                if(row == null) row = sheet.createRow(startIndex);
                startIndex ++;
                row.createCell(0).setCellValue(item.getTreeCode());
                row.createCell(1).setCellValue(item.getTreeName());
                row.createCell(2).setCellValue(item.getTotalCount());
                row.createCell(3).setCellValue(item.getTotalAmount());
            }
            XSSFRow rowTreeType = sheet.getRow(startIndex);
            if(rowTreeType == null) rowTreeType = sheet.createRow(startIndex);
            for(int i = 0;i< 4;i++){
                if(i == 0) rowTreeType.createCell(i).setCellValue(typeName);
                if(i == 1) rowTreeType.createCell(i).setCellValue("Tổng");
                if(i == 2) rowTreeType.createCell(i).setCellValue(totalCount);
                if(i == 3) rowTreeType.createCell(i).setCellValue(totalAmount);
                rowTreeType.getCell(i).setCellStyle(styleTypeTree);
            }
            startIndex ++;
        }
        sheet.shiftRows(startIndex, sheet.getLastRowNum() , 1);
        XSSFRow rtotal = sheet.getRow(startIndex);
        if(rtotal == null) rtotal = sheet.createRow(startIndex);
        for(int i = 0;i< 4;i++){
            if(i == 0) rtotal.createCell(i).setCellValue("Tổng cộng");
            if(i == 1) rtotal.createCell(i).setCellValue("");
            if(i == 2) rtotal.createCell(i).setCellValue(tc);
            if(i == 3) rtotal.createCell(i).setCellValue(ta);
            rtotal.getCell(i).setCellStyle(styleTypeTree);
        }
        for(int i = 0;i<4;i++){
            sheet.autoSizeColumn(i);
        }
    }

}
