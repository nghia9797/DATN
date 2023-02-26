package com.bonsai.bill;

import com.bonsai.bill.model.Bill;
import com.bonsai.bill.model.BillDetail;
import com.bonsai.core.RepositoryFactory;
import com.bonsai.core.dao.BonsaiDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillDetailService {
    private BonsaiDao<BillDetail> billDetailDao;

    @Autowired
    public BillDetailService(RepositoryFactory repositoryFactory){
        this.billDetailDao = repositoryFactory.createDao(BillDetail.class);
    }

    public List<BillDetail> getBillDetailByBillId(Long billId){
        try {
            String where = "bill_id = "+billId;
            return billDetailDao.find(where);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<BillDetail> getBillDetailByListBillId(List<Long> ids){
        try {
            String where = String.format("bill_id in (%s)", ids.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",")));
            return billDetailDao.find(where);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void createBillDetailForBill(Bill bill){
        try {
            for(BillDetail billDetail : bill.billDetail){
                billDetail.setBillId(bill.id);
                billDetailDao.insert(billDetail);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deletes(List<Long> ids){
        billDetailDao.deletes(ids);
    }
}
