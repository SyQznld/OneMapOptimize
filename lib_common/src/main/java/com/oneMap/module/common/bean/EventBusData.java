package com.oneMap.module.common.bean;

/**
 * 传递数据
 */

public class EventBusData {

    private String message;

    private String measureType;
    private String measureResult;


    private String bzPolygon;
    private String gatherZuoBiao;
    private String measureResultReturn;
    private String analysisArea;
    private String picList;
    private String baseLayer;
    private String TopLayer;

    private String bdLng;
    private String bdLat;

    public EventBusData() {
    }

    public String getTopLayer() {
        return TopLayer;
    }

    public void setTopLayer(String topLayer) {
        TopLayer = topLayer;
    }

    public EventBusData(String message) {
        this.message = message;
    }

    public String getAnalysisArea() {
        return analysisArea;
    }

    public String getBaseLayer() {
        return baseLayer;
    }

    public void setBaseLayer(String baseLayer) {
        this.baseLayer = baseLayer;
    }
    public void setAnalysisArea(String analysisArea) {
        this.analysisArea = analysisArea;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public String getMeasureResult() {
        return measureResult;
    }

    public void setMeasureResult(String measureResult) {
        this.measureResult = measureResult;
    }

    public String getBzPolygon() {
        return bzPolygon;
    }

    public void setBzPolygon(String bzPolygon) {
        this.bzPolygon = bzPolygon;
    }


    public String getMeasureResultReturn() {
        return measureResultReturn;
    }

    public void setMeasureResultReturn(String measureResultReturn) {
        this.measureResultReturn = measureResultReturn;
    }

    public String getGatherZuoBiao() {
        return gatherZuoBiao;
    }

    public void setGatherZuoBiao(String gatherZuoBiao) {
        this.gatherZuoBiao = gatherZuoBiao;
    }

    public String getPicList() {
        return picList;
    }

    public void setPicList(String picList) {
        this.picList = picList;
    }

    public String getBdLng() {
        return bdLng;
    }

    public void setBdLng(String bdLng) {
        this.bdLng = bdLng;
    }

    public String getBdLat() {
        return bdLat;
    }

    public void setBdLat(String bdLat) {
        this.bdLat = bdLat;
    }

    @Override
    public String toString() {
        return "EventBusData{" +
                "message='" + message + '\'' +
                ", measureType='" + measureType + '\'' +
                ", measureResult='" + measureResult + '\'' +
                ", bzPolygon='" + bzPolygon + '\'' +
                ", gatherZuoBiao='" + gatherZuoBiao + '\'' +
                ", measureResultReturn='" + measureResultReturn + '\'' +
                ", analysisArea='" + analysisArea + '\'' +
                ", picList='" + picList + '\'' +
                ", baseLayer='" + baseLayer + '\'' +
                ", TopLayer='" + TopLayer + '\'' +
                ", bdLng='" + bdLng + '\'' +
                ", bdLat='" + bdLat + '\'' +
                '}';
    }
}
