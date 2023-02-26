package com.bonsai.shipper.model;

import com.bonsai.core.annotation.TableColumn;
import com.bonsai.core.annotation.TableName;
import com.bonsai.core.dao.BonsaiEntity;

@TableName(name = "shipers", hasCheckTime = false)
public class Shipper extends BonsaiEntity {
    @TableColumn(name = "name")
    public String name;

    @TableColumn(name = "phone")
    public String phone;

    @TableColumn(name = "address")
    public String address;

    @TableColumn(name = "indentify_id")
    public String indentifyId;

    @TableColumn(name = "birth")
    public Long birth;

    @TableColumn(name = "avatar")
    public String avatar;

    @TableColumn(name = "status")
    public Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIndentifyId() {
        return indentifyId;
    }

    public void setIndentifyId(String indentifyId) {
        this.indentifyId = indentifyId;
    }

    public Long getBirth() {
        return birth;
    }

    public void setBirth(Long birth) {
        this.birth = birth;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
