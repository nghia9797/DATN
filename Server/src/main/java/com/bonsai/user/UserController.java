package com.bonsai.user;

import com.bonsai.auth.AuthService;
import com.bonsai.common.Contants;
import com.bonsai.common.Response;
import com.bonsai.core.dao.ResultPaging;
import com.bonsai.user.model.RequestSearchUser;
import com.bonsai.user.model.User;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manage/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @GetMapping("/search")
    public Response search(@RequestParam Map<String, Object> params, HttpServletRequest request){
        Gson gson = new Gson();
        RequestSearchUser requestSearchUser = gson.fromJson(gson.toJson(params), RequestSearchUser.class);
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "USER:VIEW");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            ResultPaging<User> result = userService.search(requestSearchUser);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @PostMapping("/create")
    public Response createUser(@RequestBody User user, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "USER:CREATE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            User result = userService.createUser(user);
            if(result == null) return Response.createResponseServerError();
            if(result.id == 0){
                Response r = Response.createResponseError();
                r.message = "Tên đăng nhập đã được sử dụng";
                return r;
            }
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @PutMapping("/update")
    public Response updateUser(@RequestBody User user, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "USER:UPDATE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            User result = userService.updateUser(user);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @PutMapping("/updateForUser")
    public Response updateUserForUser(@RequestBody User user, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForUser(request, "USER:UPDATE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            User result = userService.updateUser(user);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @GetMapping("/getById/{id}")
    public Response getById(@PathVariable Long id, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "USER:UPDATE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            User result = userService.getById(id);
            if(result == null) return Response.createResponseServerError();
            result.password = null;
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @GetMapping("/getByIdForUser/{id}")
    public Response getByIdForUser(@PathVariable Long id, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForUser(request, "USER:UPDATE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            User result = userService.getById(id);
            if(result == null) return Response.createResponseServerError();
            result.password = null;
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @DeleteMapping("/delete/{id}")
    public Response delete(@PathVariable Long id, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "USER:DELETE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            userService.delete(id);
            return Response.createResponseSuccess(null);
        }else return resultCheck;
    }

    @DeleteMapping("/deletes")
    public Response delete(@RequestParam(value = "ids") String ids, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "USER:DELETE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Long[] userIds = new Gson().fromJson(String.format("[%s]",ids), Long[].class);
            userService.deletes(Arrays.asList(userIds));
            return Response.createResponseSuccess(null);
        }else return resultCheck;
    }

    @PutMapping("/changePassword")
    public Response changePassword(@RequestBody User user, HttpServletRequest request) throws NoSuchAlgorithmException {
        Response resultCheck = authService.checkSessionAndPermissionForUser(request, "");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            if(user.id != null){
                User u = userService.getById(user.id);
                u.password = AuthService.encryptPassword(user.password);
                user = userService.updateUser(u);
                if(user == null) return Response.createResponseError();
                return Response.createResponseSuccess(user);
            }
            return Response.createResponseError();
        }else return resultCheck;
    }
}
