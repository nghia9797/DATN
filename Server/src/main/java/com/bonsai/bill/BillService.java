package com.bonsai.bill;

import com.bonsai.bill.model.Bill;
import com.bonsai.bill.model.BillDetail;
import com.bonsai.bill.model.RequestSearchBill;
import com.bonsai.core.RepositoryFactory;
import com.bonsai.core.dao.BonsaiDao;
import com.bonsai.core.dao.Paging;
import com.bonsai.core.dao.ResultPaging;
import com.bonsai.core.dao.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BillService {
    @Autowired
    private BillDetailService billDetailService;

    private BonsaiDao<Bill> billDao;

    @Autowired
    public BillService(RepositoryFactory repositoryFactory){
        this.billDao = repositoryFactory.createDao(Bill.class);
    }

    public Bill createBill(Bill bill){
        try {
            bill.code = "BILL"+ UUID.randomUUID().toString().replaceAll("-","").substring(0,12);
            bill.created = System.currentTimeMillis();
            bill.updated = System.currentTimeMillis();
            bill = billDao.insert(bill);
            if(bill != null){
                billDetailService.createBillDetailForBill(bill);
                return bill;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Bill updateBill(Bill bill){
        try {
            bill.updated = System.currentTimeMillis();
            return billDao.update(bill);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Bill> getBillForUser(Long userId){
        try {
            String where = String.format("receiver = %d", userId);
            return billDao.find(where);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Bill getBillById(Long id){
        try {
            Bill bill = billDao.findById(id);
            if(bill != null){
                bill.billDetail = billDetailService.getBillDetailByBillId(bill.id);
                return bill;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void deleteBill(Long id){
        try {
            List<BillDetail> details = billDetailService.getBillDetailByBillId(id);
            billDetailService.deletes(details.stream().map(n -> n.id).collect(Collectors.toList()));
            billDao.delete(id);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public ResultPaging<Bill> search(RequestSearchBill requestSearch) {
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
            if(requestSearch.code != null && !requestSearch.code.isEmpty()){
                where += " and upper(code) like '%" + requestSearch.code.toUpperCase() + "%'";
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

            if(requestSearch.receiver != null){
                where += " and receiver = "+ requestSearch.receiver;
            }
            return billDao.find(where,sort, paging);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void deletes(List<Long> ids){
        List<BillDetail> details = billDetailService.getBillDetailByListBillId(ids);
        if(details != null){
            billDetailService.deletes(details.stream().map(n -> n.id).collect(Collectors.toList()));
            billDao.deletes(ids);
        }
    }
}
