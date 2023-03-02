package com.bonsai.auth;
import com.bonsai.admin.AdminService;
import com.bonsai.admin.model.Admin;
import com.bonsai.common.Response;
import com.bonsai.operation.OperationService;
import com.bonsai.operation.model.Operation;
import com.bonsai.role.model.Role;
import com.bonsai.user.UserService;
import com.bonsai.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private OperationService operationService;
    private final long EXPIRATION = 24*60*60*1000;

    private Map<String,Map<String,Object>> session = new HashMap<>();

    public static String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(password.getBytes(StandardCharsets.UTF_8));
        String hash = DatatypeConverter.printHexBinary(md5.digest());
        return hash;
    }

    public Response loginUser(String username, String password){
        try {
            User user = userService.getByUsername(username);
            if(user != null && user.status == 0){
                if(user.password.equals(encryptPassword(password))){
                    Map<String,Object> payload = new HashMap<String, Object>();
                    payload.put("id", user.id);
                    payload.put("username", user.username);
                    payload.put("fullname", user.fullname);
                    payload.put("password", password);
                    payload.put("operations", new HashSet<String>(Arrays.asList(new String[]{"USER:VIEW","USER:UPDATE", "BILL:CREATE", "BILL:VIEW","BILL:UPDATE"})));
                    payload.put("expired", System.currentTimeMillis() + EXPIRATION);
                    payload.put("type", 1);
                    String key = UUID.randomUUID().toString();
                    session.put(key, payload);
                    Map<String,Object> result = new HashMap<>();
                    user.password = null;
                    result.put("token", key);
                    result.put("user", user);
                    return Response.createResponseSuccess(result);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.createResponseError();
    }

    public Response loginAdmin(String username, String password){
        try {
            Admin admin = adminService.getByUsername(username);
            if(admin != null && admin.status == 0){
                admin = adminService.getById(admin.id);
                if(admin.password.equals(encryptPassword(password))){
                    Map<String,Object> payload = new HashMap<String, Object>();
                    payload.put("id", admin.id);
                    payload.put("username", admin.username);
                    payload.put("fullname", admin.fullname);
                    payload.put("password", password);
                    Set<Long> operationIds = new HashSet<>();
                    if(admin.operationIds != null) operationIds.addAll(admin.operationIds);
                    if(admin.roles != null){
                        for(Role role : admin.roles){
                            if(role.operationIds != null){
                                operationIds.addAll(role.operationIds);
                            }
                        }
                    }
                    Set<String> operations = new HashSet<>();
                    if(operationIds.size() > 0){
                        List<Operation> ops = operationService.getByIds(operationIds.stream().collect(Collectors.toList()));
                        if(ops != null){
                            operations = ops.stream().map(n -> n.alias).collect(Collectors.toSet());
                        }
                    }
                    payload.put("operations", operations);
                    payload.put("expired", System.currentTimeMillis() + EXPIRATION);
                    payload.put("type", 2);
                    String key = UUID.randomUUID().toString();
                    session.put(key, payload);
                    Map<String,Object> result = new HashMap<>();
                    admin.password = null;
                    result.put("token", key);
                    result.put("admin", admin);
                    return Response.createResponseSuccess(result);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.createResponseError();
    }

    public Response checkSessionAndPermissionForAdmin(HttpServletRequest request, String operation){
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if(token.startsWith("Bearer")){
                token = token.split(" ")[1];
                if(session.containsKey(token)){
                    Map<String, Object> payload = session.get(token);
                    if((Integer) payload.get("type") != 2) return Response.createResponseNotLoginAdmin();
                    if((Long) payload.get("expired") >= System.currentTimeMillis()){
                        Set<String> operations = (Set<String>) payload.get("operations");
                        if(operation == null || operation.isEmpty()) return Response.createResponseSuccess(payload);
                        if(operations != null){
                            for(String op : operations){
                                if(op.equals(operation)) return Response.createResponseSuccess(payload);
                            }
                            return Response.createResponseNotAllow();
                        }
                    } else return Response.createResponseNotLoginAdmin();
                }else return Response.createResponseNotLoginAdmin();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.createResponseError();
    }

    public Response checkSessionAndPermissionForUser(HttpServletRequest request, String operation){
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if(token.startsWith("Bearer")){
                token = token.split(" ")[1];
                if(session.containsKey(token)){
                    Map<String, Object> payload =  session.get(token);
                    if((Integer) payload.get("type") != 1) return Response.createResponseNotLogin();
                    if((Long) payload.get("expired") >= System.currentTimeMillis()){
                        Set<String> operations = (Set<String>) payload.get("operations");
                        if(operation == null || operation.isEmpty()) return Response.createResponseSuccess(payload);
                        if(operations != null){
                            for(String op : operations){
                                if(op.equals(operation)) return Response.createResponseSuccess(payload);
                            }
                            return Response.createResponseNotAllow();
                        }
                    }else return Response.createResponseNotLogin();
                }else return Response.createResponseNotLogin();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.createResponseError();
    }


    public Response logout(HttpServletRequest request){
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if(token.startsWith("Bearer")){
                token = token.split(" ")[1];
                if(session.containsKey(token)){
                    session.remove(token);
                }
            }
            return Response.createResponseSuccess(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.createResponseError();
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    public void deleteTokenTimeOut(){
        System.out.println("Start delete token timeout");
        List<String> tokenTimeouts = new ArrayList<>();
        for(String key : session.keySet()){
            Map<String,Object> payload =  session.get(key);
            if((Long) payload.get("expired") < System.currentTimeMillis()){
                tokenTimeouts.add(key);
            }
        }
        for(String key : tokenTimeouts){
            session.remove(key);
        }
        System.out.println("End delete token timeout");
    }

    public Response userSignin(User user) {
        try {
            user = userService.createUser(user);
            if(user.id > 0){
                return Response.createResponseSuccess(null);
            }else{
                Response r = Response.createResponseError();
                r.message = "Tên đăng nhập đã tồn tại";
                return r;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.createResponseError();
    }
}
