package com.bonsai.shipper;

import com.bonsai.auth.AuthService;
import com.bonsai.common.Contants;
import com.bonsai.common.Response;
import com.bonsai.core.dao.ResultPaging;
import com.bonsai.shipper.model.RequestSearchShipper;
import com.bonsai.shipper.model.Shipper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/manage/shipper")
public class ShipperController {
    @Autowired
    private AuthService authService;

    @Autowired
    private ShipperService shipperService;

    @GetMapping("/search")
    public Response search(@RequestParam Map<String,Object> params, HttpServletRequest request){
        Gson gson = new Gson();
        RequestSearchShipper requestSearch = gson.fromJson(gson.toJson(params), RequestSearchShipper.class);
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "SHIPPER:VIEW");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            ResultPaging<Shipper> result = shipperService.search(requestSearch);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @GetMapping("/getById/{id}")
    public Response getById(@PathVariable Long id, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "SHIPPER:VIEW");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Shipper result = shipperService.getById(id);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @PostMapping("/create")
    public Response createShipper(@RequestBody Shipper shipper, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "SHIPPER:CREATE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Shipper result = shipperService.createShipper(shipper);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @PutMapping("/update")
    public Response updateShipper(@RequestBody Shipper shipper, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "SHIPPER:UPDATE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Shipper result = shipperService.updateShipper(shipper);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @DeleteMapping("/delete/{id}")
    public Response deleteShipper(@PathVariable Long id, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "SHIPPER:DELETE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            shipperService.delete(id);
            return Response.createResponseSuccess(null);
        }else return resultCheck;
    }

    @DeleteMapping("/deletes")
    public Response delete(@RequestParam(value = "ids") String ids, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "SHIPPER:DELETE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Long[] shipperIds = new Gson().fromJson(ids, Long[].class);
            shipperService.deletes(Arrays.asList(shipperIds));
            return Response.createResponseSuccess(null);
        }else return resultCheck;
    }
}
