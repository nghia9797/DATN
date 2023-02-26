package com.bonsai.role;

import com.bonsai.core.RepositoryFactory;
import com.bonsai.core.dao.BonsaiDao;
import com.bonsai.core.dao.Paging;
import com.bonsai.core.dao.ResultPaging;
import com.bonsai.core.dao.Sort;
import com.bonsai.operation.OperationService;
import com.bonsai.role.model.RequestSearchRole;
import com.bonsai.role.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private BonsaiDao<Role> roleDao;

    @Autowired
    private OperationService operationService;

    @Autowired
    public RoleService(RepositoryFactory repositoryFactory){
        roleDao = repositoryFactory.createDao(Role.class);
    }

    public List<Role> getByIds(List<Long> ids){
        return roleDao.find(String.format("id in (%s)", ids.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(","))));
    }

    public List<Role> getAll(){
        return roleDao.findAll();
    }

    public Role getById(Long id){
        try {
            return roleDao.findById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ResultPaging<Role> search(RequestSearchRole requestSearch){
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
            String where = "1 = 1";
            if(requestSearch.name != null && !requestSearch.name.isEmpty()){
                where += " and upper(name) like '%" + requestSearch.name.toUpperCase() + "%'";
            }
            return roleDao.find(where, sort, paging);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Role createRole(Role role){
        try {
            return roleDao.insert(role);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Role updateRole(Role role){
        try {
            return roleDao.update(role);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Long id){
        try {
            roleDao.delete(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deletes(List<Long> ids){
        try {
            roleDao.deletes(ids);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
