package com.bonsai.report;

import com.bonsai.auth.AuthService;
import com.bonsai.common.Contants;
import com.bonsai.common.Response;
import com.bonsai.core.dao.ResultPaging;
import com.bonsai.report.model.ReportRevenueRequest;
import com.google.gson.Gson;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

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
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "REPORT:DOWNLOAD");
        resultCheck.statusCode = Contants.StatusCode.OK;
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Map<String,Object> result = reportService.getDataReportRevenue(requestSearch);
            if(result == null) return null;
            XSSFWorkbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            workbook.write(output);
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

}
