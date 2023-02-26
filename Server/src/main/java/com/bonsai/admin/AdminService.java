package com.bonsai.admin;

import com.bonsai.admin.model.Admin;
import com.bonsai.admin.model.RequestSearch;
import com.bonsai.auth.AuthService;
import com.bonsai.core.RepositoryFactory;
import com.bonsai.core.dao.BonsaiDao;
import com.bonsai.core.dao.Paging;
import com.bonsai.core.dao.ResultPaging;
import com.bonsai.core.dao.Sort;
import com.bonsai.operation.OperationService;
import com.bonsai.operation.model.Operation;
import com.bonsai.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private RoleService roleService;
    @Autowired
    private OperationService operationService;

    private BonsaiDao<Admin> adminDao;
    @Autowired
    public AdminService(RepositoryFactory repositoryFactory){
        adminDao = repositoryFactory.createDao(Admin.class);
    }

    public ResultPaging<Admin> search(RequestSearch requestSearch){
        try {
            Sort sort = null;
            if(requestSearch.fieldSort != null && !requestSearch.fieldSort.isEmpty()){
                if(requestSearch.typeSort != null && !requestSearch.typeSort.isEmpty()){
                    Sort.Direction type = Sort.Direction.ACS;
                    if(requestSearch.typeSort.equalsIgnoreCase(Sort.Direction.DESC.name())){
                        type = Sort.Direction.DESC;
                    }
                    sort = new Sort(Arrays.asList(new String[]{requestSearch.fieldSort}),
                                    Arrays.asList(new Sort.Direction[]{type}));
                }
            }
            Paging paging = null;
            if(requestSearch.page != null && requestSearch.page >= 0){
                if(requestSearch.limit != null && requestSearch.limit > 0){
                    paging = new Paging(requestSearch.page, requestSearch.limit);
                }
            }
            String where = "id > 1";
            if(requestSearch.username != null && !requestSearch.username.isEmpty()){
                where += " and upper(username) like '%" + requestSearch.username.toUpperCase() + "%'";
            }
            if(requestSearch.fullname != null && !requestSearch.fullname.isEmpty()){
                where += " and upper(fullname) like '%" + requestSearch.fullname.toUpperCase() + "%'";
            }
            if(requestSearch.phone != null && !requestSearch.phone.isEmpty()){
                where += " and phone like '%" + requestSearch.phone + "%'";
            }
            if(requestSearch.email != null && !requestSearch.email.isEmpty()){
                where += " and email like '%" + requestSearch.email + "%'";
            }
            if(requestSearch.address != null && !requestSearch.address.isEmpty()){
                where += " and upper(address) like '%" + requestSearch.address.toUpperCase() + "%'";
            }
            if(requestSearch.from != null && requestSearch.from > 0){
                where += " and created >= "+ requestSearch.from;
            }
            if(requestSearch.to != null && requestSearch.to > 0){
                where += " and created <= "+ requestSearch.to;
            }
            if(requestSearch.status != null){
                where += " and status = "+ requestSearch.status;
            }
            return adminDao.find(where, sort, paging);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Admin getByUsername(String username){
        List<Admin> admins = adminDao.find(String.format("username = '%s'", username));
        if(admins != null && admins.size() == 1){
            return admins.get(0);
        }
        return null;
    }

    public Admin getById(Long id){
        try {
            Admin admin = adminDao.findById(id);
            if(admin != null){
                if(admin.id != 1){
                    if(admin.roleIds != null && admin.roleIds.size() > 0){
                        admin.roles = roleService.getByIds(admin.roleIds);
                    }
                    if(admin.operationIds != null && admin.operationIds.size() > 0){
                        admin.operations = operationService.getByIds(admin.operationIds);
                    }
                }else{
                    admin.operations = operationService.getAll();
                    admin.operationIds = admin.operations.stream().map(n -> n.id).collect(Collectors.toList());
                }
            }
            return admin;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Admin createAdmin(Admin admin){
        try {
            admin.created = System.currentTimeMillis();
            admin.updated = System.currentTimeMillis();
            admin.password = AuthService.encryptPassword(admin.password);
            return adminDao.insert(admin);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Admin updateAdmin(Admin admin){
        try {
            admin.updated = System.currentTimeMillis();
            return adminDao.update(admin);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Long id){
        adminDao.delete(id);
    }

    public void deletes(List<Long> ids){
        adminDao.deletes(ids);
    }
}
