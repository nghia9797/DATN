package com.bonsai.user.model;

import com.bonsai.core.annotation.TableColumn;
import com.bonsai.core.annotation.TableName;
import com.bonsai.core.dao.BonsaiEntity;

@TableName(name="users", hasCheckTime = true)
public class User extends BonsaiEntity {
    @TableColumn(name = "username")
    public String username;

    @TableColumn(name = "fullname")
    public String fullname;

    @TableColumn(name = "password")
    public String password;

    @TableColumn(name = "phone")
    public String phone;

    @TableColumn(name="email")
    public String email;

    @TableColumn(name = "address")
    public String address;

    @TableColumn(name= "status")
    public Integer status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
