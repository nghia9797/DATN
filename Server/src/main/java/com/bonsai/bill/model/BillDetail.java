package com.bonsai.bill.model;

import com.bonsai.core.annotation.TableColumn;
import com.bonsai.core.annotation.TableName;
import com.bonsai.core.dao.BonsaiEntity;

@TableName(name = "bill_details", hasCheckTime = false)
public class BillDetail extends BonsaiEntity {
    @TableColumn(name = "bill_id")
    public Long billId;

    @TableColumn(name = "tree_id")
    public Long treeId;

    @TableColumn(name = "count")
    public Long count;

    @TableColumn(name = "amount_unit")
    public Long amountUnit;

    @TableColumn(name = "amount")
    public Long amount;

    @TableColumn(name = "discount")
    public Long discount;

    @TableColumn(name = "tree_name")
    public String treeName;

    @TableColumn(name = "tree_code")
    public String treeCode;

    @TableColumn(name = "tree_type")
    public String treeType;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getTreeId() {
        return treeId;
    }

    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(Long amountUnit) {
        this.amountUnit = amountUnit;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public String getTreeCode() {
        return treeCode;
    }

    public void setTreeCode(String treeCode) {
        this.treeCode = treeCode;
    }

    public String getTreeType() {
        return treeType;
    }

    public void setTreeType(String treeType) {
        this.treeType = treeType;
    }
}
