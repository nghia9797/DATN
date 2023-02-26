package com.bonsai.auth;

import com.bonsai.common.Response;
import com.bonsai.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/user/signin")
    public Response userSignin(@RequestBody User user){
        return authService.userSignin(user);
    }

    @PostMapping("/admin/login")
    public Response loginAdmin(@RequestBody Map<String,String> params){
        String username = params.getOrDefault("username","");
        String password = params.getOrDefault("password", "");
        return authService.loginAdmin(username, password);
    }

    @PostMapping("/user/login")
    public Response loginUser(@RequestBody Map<String,String> params){
        String username = params.getOrDefault("username","");
        String password = params.getOrDefault("password", "");
        return authService.loginUser(username, password);
    }

    @GetMapping("/check")
    public Response checkSession(HttpServletRequest request, @RequestParam(name = "type") Integer type){
        if(type == 1){
            return authService.checkSessionAndPermissionForUser(request, "");
        }else if (type == 2){
            return authService.checkSessionAndPermissionForAdmin(request, "");
        } else return Response.createResponseNotLogin();
    }

    @GetMapping("/logout")
    public Response logout(HttpServletRequest request){
        return authService.logout(request);
    }
}
