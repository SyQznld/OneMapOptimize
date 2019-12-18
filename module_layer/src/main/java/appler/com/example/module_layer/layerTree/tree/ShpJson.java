package appler.com.example.module_layer.layerTree.tree;

import android.util.Log;

import com.google.gson.JsonObject;

public class ShpJson {
    /**
     * 拼接显示shp文件的json  传递对象
     */
    public String setShpJson(String isDiancha, String propertyTxtpath, String layerTitle, String sdCardName, String shpColor, String shpBZKey, boolean isShow,String layerViewCenter) {
        JsonObject jsonObject = new JsonObject();
        if ("true".equals(isDiancha)) {
            jsonObject.addProperty("identify", true);                                        //是否点查
        } else {
            jsonObject.addProperty("identify", false);                                        //是否点查
        }
        jsonObject.addProperty("path", propertyTxtpath);                                      //路径 到文本前一级
        jsonObject.addProperty("title", layerTitle);  //点查标题（当前图层）
        jsonObject.addProperty("name", sdCardName);                          //文本名称（txt）
        jsonObject.addProperty("color", shpColor == null ? "" : shpColor);   //shp加载显示颜色
        jsonObject.addProperty("label", shpBZKey == null ? "" : shpBZKey);   //添加标注的字段
        jsonObject.addProperty("isShow", isShow);   //打开关闭图层
        jsonObject.addProperty("layerViewCenter", layerViewCenter);                            //打开图层的中心点 视野范围内
        Log.i("chro", "setShpJson: " + jsonObject.toString());
        return jsonObject.toString();
    }
}
