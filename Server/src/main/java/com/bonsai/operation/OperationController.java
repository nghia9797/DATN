package com.bonsai.operation;

import com.bonsai.auth.AuthService;
import com.bonsai.common.Contants;
import com.bonsai.common.Response;
import com.bonsai.operation.model.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/manage/operation")
public class OperationController {
    @Autowired
    private AuthService authService;
    @Autowired
    private OperationService operationService;
    @GetMapping("/getAll")
    public Response getAll(HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            List<Operation> result = operationService.getAll();
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }
}
