package com.bonsai.tree;

import com.bonsai.core.RepositoryFactory;
import com.bonsai.core.dao.BonsaiDao;
import com.bonsai.core.dao.Paging;
import com.bonsai.core.dao.ResultPaging;
import com.bonsai.core.dao.Sort;
import com.bonsai.tree.model.RequestSearchTree;
import com.bonsai.tree.model.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TreeService {
    private BonsaiDao<Tree> treeDao;
    @Autowired
    private TreeTypeService treeTypeService;

    @Autowired
    public TreeService(RepositoryFactory repositoryFactory){
        treeDao = repositoryFactory.createDao(Tree.class);
    }

    public Tree getById(Long id){
        try {
            Tree tree = treeDao.findById(id);
            if(tree != null){
                tree.treeType = treeTypeService.getById(tree.typeId);
                return tree;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Tree> getByTreeType(Long treeTypeId){
        String where = "type_id = "+treeTypeId;
        return treeDao.find(where);
    }

    public ResultPaging<Tree> search(RequestSearchTree requestSearch){
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
            if(requestSearch.code != null && !requestSearch.code.isEmpty()){
                where += " and upper(code) like '%" + requestSearch.code.toUpperCase() + "%'";
            }
            if(requestSearch.name != null && !requestSearch.name.isEmpty()){
                where += " and upper(name) like '%" + requestSearch.name.toUpperCase() + "%'";
            }
            if(requestSearch.from != null && requestSearch.from > 0){
                where += " and created >= "+ requestSearch.from;
            }
            if(requestSearch.to != null && requestSearch.to > 0){
                where += " and created <= "+ requestSearch.to;
            }
            if(requestSearch.typeId != null && requestSearch.typeId > 0){
                where += " and type_id = "+ requestSearch.typeId;
            }
            if(requestSearch.count != null && requestSearch.count > 0){
                where += " and count > 0";
            }
            return treeDao.find(where, sort, paging);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Tree createTree(Tree tree){
        try {
            tree.code = "TREE"+ UUID.randomUUID().toString().replaceAll("-","").substring(0,6).toUpperCase();
            tree.created = System.currentTimeMillis();
            tree.updated = System.currentTimeMillis();
            return treeDao.insert(tree);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Tree updateTree(Tree tree){
        try {
            tree.updated = System.currentTimeMillis();
            return treeDao.update(tree);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Long id){
        try {
            treeDao.delete(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deletes(List<Long> ids){
        try {
            treeDao.deletes(ids);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Tree> getByIds(List<Long> ids) {
        try {
            String where = String.format("id in (%s)", ids.stream().map(n->String.valueOf(n)).collect(Collectors.joining(",")));
            return treeDao.find(where);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
