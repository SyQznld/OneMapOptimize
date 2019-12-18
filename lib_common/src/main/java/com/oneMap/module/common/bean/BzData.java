package com.oneMap.module.common.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BzData {
    @org.greenrobot.greendao.annotation.Id
    private Long Id;
    private String bzTime;
    private String bzType;
    private String polygon; //记录多个点的坐标
    private String bzName;
    private String bzRemark;
    private String bzResult;
    @Generated(hash = 1744031246)
    public BzData(Long Id, String bzTime, String bzType, String polygon,
            String bzName, String bzRemark, String bzResult) {
        this.Id = Id;
        this.bzTime = bzTime;
        this.bzType = bzType;
        this.polygon = polygon;
        this.bzName = bzName;
        this.bzRemark = bzRemark;
        this.bzResult = bzResult;
    }
    @Generated(hash = 2096224341)
    public BzData() {
    }
    public Long getId() {
        return this.Id;
    }
    public void setId(Long Id) {
        this.Id = Id;
    }
    public String getBzTime() {
        return this.bzTime;
    }
    public void setBzTime(String bzTime) {
        this.bzTime = bzTime;
    }
    public String getBzType() {
        return this.bzType;
    }
    public void setBzType(String bzType) {
        this.bzType = bzType;
    }
    public String getPolygon() {
        return this.polygon;
    }
    public void setPolygon(String polygon) {
        this.polygon = polygon;
    }
    public String getBzName() {
        return this.bzName;
    }
    public void setBzName(String bzName) {
        this.bzName = bzName;
    }
    public String getBzRemark() {
        return this.bzRemark;
    }
    public void setBzRemark(String bzRemark) {
        this.bzRemark = bzRemark;
    }
    public String getBzResult() {
        return this.bzResult;
    }
    public void setBzResult(String bzResult) {
        this.bzResult = bzResult;
    }

    @Override
    public String toString() {
        return "BzData{" +
                "Id=" + Id +
                ", bzTime='" + bzTime + '\'' +
                ", bzType='" + bzType + '\'' +
                ", polygon='" + polygon + '\'' +
                ", bzName='" + bzName + '\'' +
                ", bzRemark='" + bzRemark + '\'' +
                ", bzResult='" + bzResult + '\'' +
                '}';
    }
}
