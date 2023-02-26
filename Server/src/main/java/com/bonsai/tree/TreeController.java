package com.bonsai.tree;

import com.bonsai.auth.AuthService;
import com.bonsai.common.Contants;
import com.bonsai.common.Response;
import com.bonsai.core.dao.ResultPaging;
import com.bonsai.tree.model.RequestSearchTree;
import com.bonsai.tree.model.Tree;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manage/tree")
public class TreeController {
    private Gson gson = new Gson();
    @Autowired
    private AuthService authService;

    @Autowired
    private TreeService treeService;

    @Autowired
    private TreeTypeService treeTypeService;

    @GetMapping("/searchfree")
    public Response searchFree(@RequestParam Map<String,Object> params, HttpServletRequest request){
        RequestSearchTree requestSearch = gson.fromJson(gson.toJson(params), RequestSearchTree.class);
        ResultPaging<Tree> result = treeService.search(requestSearch);
        if(result == null) return Response.createResponseServerError();
        return Response.createResponseSuccess(result);
    }

    @GetMapping("/search")
    public Response search(@RequestParam Map<String,Object> params, HttpServletRequest request){
        RequestSearchTree requestSearch = gson.fromJson(gson.toJson(params), RequestSearchTree.class);
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "TREE:VIEW");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            ResultPaging<Tree> result = treeService.search(requestSearch);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @GetMapping("/getByIdFree/{id}")
    public Response getByIdFree(@PathVariable Long id, HttpServletRequest request){
        Tree result = treeService.getById(id);
        if(result == null) return Response.createResponseServerError();
        return Response.createResponseSuccess(result);
    }

    @GetMapping("/getByIdsFree")
    public Response getByIdsFree(@RequestParam("ids") String ids, HttpServletRequest request){
        Long[] treeIds = new Gson().fromJson(String.format("[%s]",ids),Long[].class);
        List<Tree> result = treeService.getByIds(Arrays.asList(treeIds));
        if(result == null) return Response.createResponseServerError();
        return Response.createResponseSuccess(result);
    }

    @GetMapping("/getById/{id}")
    public Response getById(@PathVariable Long id, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "TREE:VIEW");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Tree result = treeService.getById(id);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @PostMapping("/create")
    public Response createTree(@RequestBody Tree tree, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "TREE:CREATE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Tree result = treeService.createTree(tree);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @PutMapping("/update")
    public Response updateTree(@RequestBody Tree tree, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "TREE:UPDATE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Tree result = treeService.updateTree(tree);
            if(result == null) return Response.createResponseServerError();
            return Response.createResponseSuccess(result);
        }else return resultCheck;
    }

    @DeleteMapping("/delete/{id}")
    public Response deleteTree(@PathVariable Long id, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "TREE:DELETE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            treeService.delete(id);
            return Response.createResponseSuccess(null);
        }else return resultCheck;
    }

    @DeleteMapping("/deletes")
    public Response delete(@RequestParam(value = "ids") String ids, HttpServletRequest request){
        Response resultCheck = authService.checkSessionAndPermissionForAdmin(request, "TREE:DELETE");
        if(resultCheck.statusCode == Contants.StatusCode.OK){
            Long[] treeIds = new Gson().fromJson(ids, Long[].class);
            treeService.deletes(Arrays.asList(treeIds));
            return Response.createResponseSuccess(null);
        }else return resultCheck;
    }
}
