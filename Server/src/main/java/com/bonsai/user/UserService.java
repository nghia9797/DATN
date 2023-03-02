package com.bonsai.user;

import com.bonsai.auth.AuthService;
import com.bonsai.core.RepositoryFactory;
import com.bonsai.core.dao.BonsaiDao;
import com.bonsai.core.dao.Paging;
import com.bonsai.core.dao.ResultPaging;
import com.bonsai.core.dao.Sort;
import com.bonsai.user.model.RequestSearchUser;
import com.bonsai.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private BonsaiDao<User> userDao;
    @Autowired
    public UserService(RepositoryFactory repositoryFactory){
        userDao = repositoryFactory.createDao(User.class);
    }
    public ResultPaging<User> search(RequestSearchUser requestSearch) {
        try {
            Sort sort = null;
            if(requestSearch.fieldSort != null && !requestSearch.fieldSort.isEmpty()){
                if(requestSearch.typeSort != null && !requestSearch.typeSort.isEmpty()){
                    Sort.Direction type = Sort.Direction.ASC;
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
            if(requestSearch.status != null && requestSearch.status >= 0){
                where += " and status = "+ requestSearch.status;
            }
            return userDao.find(where, sort, paging);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public User getByUsername(String userName){
        List<User> users = userDao.find(String.format("username = '%s'",userName));
        if(users != null && users.size() == 1){
            return users.get(0);
        }
        return null;
    }

    public User createUser(User user){
        try {
            user.created = new Date().getTime();
            user.updated = new Date().getTime();
            if(user.password == null){
                user.password = "123456";
            }
            user.password = AuthService.encryptPassword(user.password);
            return userDao.insert(user);
        }catch (Exception e){
            e.printStackTrace();
            if(e.getMessage().contains("Duplicate")){
                user.id = 0L;
                return user;
            }
        }
        return null;
    }

    public User updateUser(User user) {
        try {
            user.updated = System.currentTimeMillis();
            return userDao.update(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public void delete(Long id) {
        userDao.delete(id);
    }

    public void deletes(List<Long> ids){
        userDao.deletes(ids);
    }

    public User getById(Long id) {
        try {
            return userDao.findById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
