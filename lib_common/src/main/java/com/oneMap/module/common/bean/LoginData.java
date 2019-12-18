package com.oneMap.module.common.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LoginData {

    @org.greenrobot.greendao.annotation.Id
    private Long Id;
    private String role;
    private String power;
    private String usermanager;
    private String note;
    private String username;
    private String password;
    private String realname;
    private String company;
    private String roleid;
    private String telephone;
    private String layers;
    private String mokuai;
    private String userId;
    @Generated(hash = 390739750)
    public LoginData(Long Id, String role, String power, String usermanager,
            String note, String username, String password, String realname,
            String company, String roleid, String telephone, String layers,
            String mokuai, String userId) {
        this.Id = Id;
        this.role = role;
        this.power = power;
        this.usermanager = usermanager;
        this.note = note;
        this.username = username;
        this.password = password;
        this.realname = realname;
        this.company = company;
        this.roleid = roleid;
        this.telephone = telephone;
        this.layers = layers;
        this.mokuai = mokuai;
        this.userId = userId;
    }
    @Generated(hash = 1578814127)
    public LoginData() {
    }
    public Long getId() {
        return this.Id;
    }
    public void setId(Long Id) {
        this.Id = Id;
    }
    public String getRole() {
        return this.role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getPower() {
        return this.power;
    }
    public void setPower(String power) {
        this.power = power;
    }
    public String getUsermanager() {
        return this.usermanager;
    }
    public void setUsermanager(String usermanager) {
        this.usermanager = usermanager;
    }
    public String getNote() {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRealname() {
        return this.realname;
    }
    public void setRealname(String realname) {
        this.realname = realname;
    }
    public String getCompany() {
        return this.company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getRoleid() {
        return this.roleid;
    }
    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }
    public String getTelephone() {
        return this.telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getLayers() {
        return this.layers;
    }
    public void setLayers(String layers) {
        this.layers = layers;
    }
    public String getMokuai() {
        return this.mokuai;
    }
    public void setMokuai(String mokuai) {
        this.mokuai = mokuai;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    

}
