package com.bonsai.tree;

import com.bonsai.core.RepositoryFactory;
import com.bonsai.core.dao.BonsaiDao;
import com.bonsai.core.dao.Paging;
import com.bonsai.core.dao.ResultPaging;
import com.bonsai.core.dao.Sort;
import com.bonsai.tree.model.RequestSearchTreeType;
import com.bonsai.tree.model.Tree;
import com.bonsai.tree.model.TreeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TreeTypeService {
    private BonsaiDao<TreeType> treeTypeDao;

    @Autowired
    public TreeTypeService(RepositoryFactory repositoryFactory){
        treeTypeDao = repositoryFactory.createDao(TreeType.class);
    }

    public List<TreeType> getAll(){
        return treeTypeDao.findAll();
    }

    public TreeType getById(Long id){
        try {
            return treeTypeDao.findById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ResultPaging<TreeType> search(RequestSearchTreeType requestSearch){
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
            return treeTypeDao.find(where, sort, paging);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public TreeType addTreeType(TreeType treeType){
        try {
            return treeTypeDao.insert(treeType);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public TreeType updateTreeType(TreeType treeType){
        try {
            return treeTypeDao.update(treeType);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Long id){
        try {
            treeTypeDao.delete(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deletes(List<Long> ids){
        try {
            treeTypeDao.deletes(ids);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
