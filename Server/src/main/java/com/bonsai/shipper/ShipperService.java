package com.bonsai.shipper;

import com.bonsai.core.RepositoryFactory;
import com.bonsai.core.dao.BonsaiDao;
import com.bonsai.core.dao.Paging;
import com.bonsai.core.dao.ResultPaging;
import com.bonsai.core.dao.Sort;
import com.bonsai.shipper.model.RequestSearchShipper;
import com.bonsai.shipper.model.Shipper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ShipperService {
    private BonsaiDao<Shipper> shipperDao;

    @Autowired
    public ShipperService(RepositoryFactory repositoryFactory){
        shipperDao = repositoryFactory.createDao(Shipper.class);
    }

    public ResultPaging<Shipper> search(RequestSearchShipper requestSearch){
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
            String where = "id > 1";
            if(requestSearch.name != null && !requestSearch.name.isEmpty()){
                where += " and upper(name) like '%" + requestSearch.name.toUpperCase() + "%'";
            }
            if(requestSearch.code != null && !requestSearch.code.isEmpty()){
                where += " and upper(code) like '%" + requestSearch.code.toUpperCase() + "%'";
            }
            if(requestSearch.phone != null && !requestSearch.phone.isEmpty()){
                where += " and phone like '%" + requestSearch.phone + "%'";
            }
            if(requestSearch.indentifyId != null && !requestSearch.indentifyId.isEmpty()){
                where += " and indentify_id like '%" + requestSearch.indentifyId + "%'";
            }
            if(requestSearch.address != null && !requestSearch.address.isEmpty()){
                where += " and upper(address) like '%" + requestSearch.address.toUpperCase() + "%'";
            }
            if(requestSearch.from != null && requestSearch.from > 0){
                where += " and birth >= "+ requestSearch.from;
            }
            if(requestSearch.to != null && requestSearch.to > 0){
                where += " and birth <= "+ requestSearch.to;
            }
            if(requestSearch.status != null && requestSearch.status >= 0){
                where += " and status = "+ requestSearch.status;
            }
            return shipperDao.find(where, sort, paging);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Shipper createShipper(Shipper shipper){
        try {
            shipper.code = "NV"+ UUID.randomUUID().toString().replaceAll("-","").substring(0,6).toUpperCase();
            return shipperDao.insert(shipper);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Shipper updateShipper(Shipper shipper){
        try {
            return shipperDao.update(shipper);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Long id){
        try {
            shipperDao.delete(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deletes(List<Long> ids){
        try {
            shipperDao.deletes(ids);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Shipper getById(Long id){
        try {
            return shipperDao.findById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
