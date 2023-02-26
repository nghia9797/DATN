package com.bonsai.admin;

import com.bonsai.admin.model.Admin;
import com.bonsai.admin.model.RequestSearch;
import com.bonsai.auth.AuthService;
import com.bonsai.common.Contants;
import com.bonsai.common.Response;
import com.bonsai.core.dao.BonsaiDao;
import com.bonsai.core.dao.ResultPaging;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/manage/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private AuthService authService;

    @GetMapping("/search")
    public Response search(@RequestParam Map<String,Object> params, HttpServletRequest request){
        Gson gson = new Gson();
        RequestSearch requestSearch = gson.fromJson(gson.toJson(params), RequestSearch.class);
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "ADMIN:VIEW");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            ResultPaging<Admin> result = adminService.search(requestSearch);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @PostMapping("/create")
    public Response createAdmin(@RequestBody Admin admin, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "ADMIN:CREATE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Admin result = adminService.createAdmin(admin);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @PutMapping("/update")
    public Response updateAdmin(@RequestBody Admin admin, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "ADMIN:UPDATE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Admin result = adminService.updateAdmin(admin);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @DeleteMapping("/delete/{id}")
    public Response deleteAdmin(@PathVariable Long id, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "ADMIN:DELETE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            adminService.delete(id);
            return Response.createResponseSuccess(null);
        }else return resultCheck;
    }

    @DeleteMapping("/deletes")
    public Response delete(@RequestParam(value = "ids") String ids, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "ADMIN:DELETE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Long[] adminIds = new Gson().fromJson(ids, Long[].class);
            adminService.deletes(Arrays.asList(adminIds));
            return Response.createResponseSuccess(null);
        }else return resultCheck;
    }
}
