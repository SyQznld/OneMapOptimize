package appler.com.example.module_layer.layerTree.tree;

import android.graphics.Color;

import java.util.List;

/**
 * 图层树 每一条目实体类
 */
public class TreeItem {
    public String Layer_name;//图层或者图层类型的名字
    public String Layer_SDName;//SD卡中的名字
    public String Layer_Type; //    图层类型
    public String ShpColor;//Shp数据的颜色
    public String ShpBZKey;//标注字段
    public String IsDiancha;    //    是否点查
    public String LayerViewCenter;    //    打开图层的视野中心点
    public int itemLevel;//列表等级
    public int itemState;//展开状态
    public boolean check;//是否选择了该图层
    public int zhiDing_RGB = Color.rgb(0, 0, 0);
    public List<TreeItem> layerItemData;//    子节点


    public int getZhiDing_RGB() {
        return zhiDing_RGB;
    }

    public void setZhiDing_RGB(int zhiDing_RGB) {
        this.zhiDing_RGB = zhiDing_RGB;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getLayer_name() {
        return Layer_name;
    }

    public void setLayer_name(String layer_name) {
        Layer_name = layer_name;
    }

    public String getLayer_SDName() {
        return Layer_SDName;
    }

    public void setLayer_SDName(String layer_SDName) {
        Layer_SDName = layer_SDName;
    }

    public String getLayer_Type() {
        return Layer_Type;
    }

    public void setLayer_Type(String layer_Type) {
        Layer_Type = layer_Type;
    }

    public String getShpColor() {
        return ShpColor;
    }

    public void setShpColor(String shpColor) {
        ShpColor = shpColor;
    }

    public String getShpBZKey() {
        return ShpBZKey;
    }

    public void setShpBZKey(String shpBZKey) {
        ShpBZKey = shpBZKey;
    }

    public String getIsDiancha() {
        return IsDiancha;
    }

    public void setDiancha(String diancha) {
        IsDiancha = diancha;
    }

    public int getItemLevel() {
        return itemLevel;
    }

    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
    }

    public int getItemState() {
        return itemState;
    }

    public void setItemState(int itemState) {
        this.itemState = itemState;
    }

    public List<TreeItem> getLayerItemData() {
        return layerItemData;
    }

    public void setLayerItemData(List<TreeItem> layerItemData) {
        this.layerItemData = layerItemData;
    }

    public String getLayerViewCenter() {
        return LayerViewCenter;
    }

    public void setLayerViewCenter(String layerViewCenter) {
        LayerViewCenter = layerViewCenter;
    }

    @Override
    public String toString() {
        return "TreeItem{" +
                "Layer_name='" + Layer_name + '\'' +
                ", Layer_SDName='" + Layer_SDName + '\'' +
                ", Layer_Type='" + Layer_Type + '\'' +
                ", ShpColor='" + ShpColor + '\'' +
                ", ShpBZKey='" + ShpBZKey + '\'' +
                ", IsDiancha='" + IsDiancha + '\'' +
                ", LayerViewCenter='" + LayerViewCenter + '\'' +
                ", itemLevel=" + itemLevel +
                ", itemState=" + itemState +
                ", check=" + check +
                ", zhiDing_RGB=" + zhiDing_RGB +
                ", layerItemData=" + layerItemData +
                '}';
    }
}
