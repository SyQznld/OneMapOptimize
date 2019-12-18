package com.oneMap.module.main.moreOperation;

/**
 * 主页更多操作 实体类 显示文字以及图片
 */
public class MoreOperationData {

    private int operationImg;
    private String operationText;

    public MoreOperationData(int operationImg, String operationText) {
        this.operationImg = operationImg;
        this.operationText = operationText;
    }

    public MoreOperationData() {
    }

    public int getOperationImg() {
        return operationImg;
    }

    public void setOperationImg(int operationImg) {
        this.operationImg = operationImg;
    }

    public String getOperationText() {
        return operationText;
    }

    public void setOperationText(String operationText) {
        this.operationText = operationText;
    }

    @Override
    public String toString() {
        return "MoreOperationData{" +
                "operationImg=" + operationImg +
                ", operationText='" + operationText + '\'' +
                '}';
    }
}
