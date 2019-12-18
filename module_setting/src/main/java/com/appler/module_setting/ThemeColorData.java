package com.appler.module_setting;

public class ThemeColorData {

    private String color;  //颜色值
    private String colorDes;//颜色描述  黄色
    private int colorBcg;  //引用的背景资源
    private boolean isSelected; //是否选中

    public ThemeColorData() {
    }

    public ThemeColorData(String color, String colorDes, int colorBcg, boolean isSelected) {
        this.color = color;
        this.colorDes = colorDes;
        this.colorBcg = colorBcg;
        this.isSelected = isSelected;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorDes() {
        return colorDes;
    }

    public void setColorDes(String colorDes) {
        this.colorDes = colorDes;
    }

    public int getColorBcg() {
        return colorBcg;
    }

    public void setColorBcg(int colorBcg) {
        this.colorBcg = colorBcg;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "ThemeColorData{" +
                "color='" + color + '\'' +
                ", colorDes='" + colorDes + '\'' +
                ", colorBcg=" + colorBcg +
                ", isSelected=" + isSelected +
                '}';
    }
}
