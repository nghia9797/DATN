package com.bonsai.bill.model;

import com.bonsai.core.annotation.TableColumn;
import com.bonsai.core.annotation.TableName;
import com.bonsai.core.dao.BonsaiEntity;

import java.util.List;

@TableName(name = "bills", hasCheckTime = true)
public class Bill extends BonsaiEntity {
    @TableColumn(name = "code")
    public String code;

    @TableColumn(name = "status")
    public Integer status;

    @TableColumn(name = "sender")
    public Long sender;

    @TableColumn(name = "receiver")
    public Long receiver;

    @TableColumn(name = "address")
    public String address;

    @TableColumn(name = "note")
    public String note;

    @TableColumn(name = "typepay")
    public Integer typePay;

    @TableColumn(name = "feeship")
    public Long feeShip;

    @TableColumn(name = "sender_name")
    public String senderName;

    @TableColumn(name = "receiver_name")
    public String receiverName;

    @TableColumn(name = "amount")
    public Long amount;

    public List<BillDetail> billDetail;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getTypePay() {
        return typePay;
    }

    public void setTypePay(Integer typePay) {
        this.typePay = typePay;
    }

    public Long getFeeShip() {
        return feeShip;
    }

    public void setFeeShip(Long feeShip) {
        this.feeShip = feeShip;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
