package appler.com.example.module_layer.property;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import appler.com.example.module_layer.R;

import static android.content.ContentValues.TAG;


/**
 * 点查 弹框显示
 */
public class ShowPropertyPop extends PopupWindow {
    private View view;
    private Context context;
    private PopupWindow popupWindow;
    private Button btn_guanbi;
    private ListView list_tile;
    private ListView list_info;

    private List<String> titleDatas;
    private List<PropertyData> datasInfo;
    private WebView webView;

    private String curPoint;//点击的某个点坐标，点查功能用到

    private int selectPosition = -1;  //图层列表选中的条目
    private String flag = "";     //判断是点击地图还是图层列表

    private String title = "";


    public ShowPropertyPop(Context context, WebView webView) {
        super(context);
        this.context = context;
        this.webView = webView;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.property_pop_layout, null);
    }

    //打开多个图层 显示最后一个打开图层相关点查信息
    public void multiParams(List<String> group_list, String info, String curPoint) {
        this.titleDatas = group_list;
        this.curPoint = curPoint;
        datasInfo = new ArrayList<>();
        flag = "multi";
        parseDcInfo(info);
    }


    //点击某个图层   显示相关点查信息
    public void singleParams(String info) {
        datasInfo = new ArrayList<>();
        flag = "single";
        parseDcInfo(info);
    }


    /**
     * 解析点查信息
     */
    private void parseDcInfo(String info) {
        Log.i(TAG, "parseDcInfo: " + info);
        Log.i(TAG, "parseDcInfo title: " + title);
        Log.i(TAG, "parseDcInfo titleDatas: " + titleDatas);
        try {
            JSONObject js = new JSONObject(info);

            String substring = info.substring(1, info.length() - 1);
            if (substring.contains(",")) {
                //有多个属性键值对
                String[] split = substring.split(",");
                for (int i = 0; i < split.length; i++) {
                    String s = split[i];
                    if (s.contains(":")) {
                        String[] split1 = s.split(":");
                        if (split1.length > 1) {
                            String key = split1[0].replace("\"", "");
                            addParams(key, js);
                        }
                    }
                }
            } else {
                //仅仅有一个属性值
                if (substring.contains(":")) {
                    String[] split1 = substring.split(":");
                    if (split1.length > 1) {
                        String key = split1[0].replace("\"", "");
                        addParams(key, js);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 填充属性弹框 key-value键值对，与点查文件中对应匹配
     */
    @SuppressLint("DefaultLocale")
    private void addParams(String parseKey, JSONObject js) throws JSONException {
        final Map<String, String> map = ParamMapUtils.propertiseMap();
        if (!map.containsKey(parseKey)) {
            //TODO  注释掉这段代码，点查字典中若果匹配不到key值，则不会在列表中显示；取消注释，按照提供的数据中字段显示
//            PropertyData params = new PropertyData();
//            params.setName(parseKey);
//            params.setParam(js.getString(parseKey));
//            datasInfo.add(params);
        } else {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String map_key = entry.getKey();
                String name = entry.getValue();
                if (parseKey.equals(map_key)) {
                    PropertyData params = new PropertyData();
                    params.setName(name);
                    String param = js.getString(parseKey);
                    if (null == param || "null".equals(param)) {
                        param = "";
                    }

                    if (parseKey.equals("ZC") || parseKey.equals("周长") || parseKey.equals("Shape_Length")|| parseKey.equals("Shape_Leng")) {
                        if (!"0.0".equals(param) && !"".equals(param) && !"null".equals(param)&& null != param) {
                            if (!param.contains("m") && !param.contains("M") && !param.contains("米")) {
                                Double zc = Double.valueOf(param);    //取两位小数位
                                param = String.format("%.2f", zc) + "米";
                            }
                        }
                    }
                    //宗地面积：41220.00㎡
                    if (parseKey.contains("MJ") || parseKey.contains("面积") || parseKey.equals("Shape_Area")||parseKey.contains("mj")) {
                        if (!"0.0".equals(param) && !"".equals(param)&& !"null".equals(param)&& null != param) {
                            if (param.contains(":")){
                                String[] split = param.split(":");
                                param = split[1].trim();
                            }
                            if (!param.contains("㎡") && !param.contains("平方米") && !param.contains("亩")) {
                                Double mj = Double.valueOf(param);    //取两位小数位
                                param = String.format("%.2f", mj) + "平方米";
                            }
                        }
                    }
                    if (parseKey.contains("亩") || parseKey.contains("MS") || parseKey.equals("M")) {
                        if (!"0.0".equals(param) && !"".equals(param)&& !"null".equals(param)&& null != param) {
                            if (param.contains(":")){
                                String[] split = param.split(":");
                                param = split[1].trim();
                            }
                            if (!param.contains("㎡") && !param.contains("平方米") && !param.contains("亩")) {
                                Double mj = Double.valueOf(param);
                                param = String.format("%.2f", mj) + "亩";
                            }
                        }
                    }
                    params.setParam(param);
                    datasInfo.add(params);
                }
            }
        }
    }


    public void showDianChaPop(View parent) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = manager.getDefaultDisplay().getHeight();
        int width = manager.getDefaultDisplay().getWidth();
        int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
        if (popupWindow == null) {
            initPopView();
            popupWindow = new PopupWindow();
            popupWindow.setWidth(width / 2);
            if (datasInfo.size() > 8) {
                popupWindow = new PopupWindow(view, width / 2, height / 2, true);
            } else {
                popupWindow = new PopupWindow(view, width / 2, wrapContent, true);
            }
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(false);

        } else {
            initPopView();
            if (null != popupWindow) {
                if (datasInfo.size() > 8 ) {
                    popupWindow.update(width / 2, height / 2);
                } else {
//                    popupWindow.update(width / 2, ((height / 20) * datasInfo.size() + 1) + 60);
                    popupWindow.update(width / 2, wrapContent);
                }
            }
        }

        int viewHeight = parent.getHeight();
        //获取pop的宽
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setContentView(view);
        int measuredWidth = popupWindow.getContentView().getMeasuredWidth();
        //左负右正      上负下正
        int x = -(measuredWidth * 2 + parent.getWidth() * 2);
        int y = -(viewHeight + 10);
        popupWindow.showAsDropDown(parent, x, y);


        //点击关闭后，弹框以及选中图斑边界消失
        btn_guanbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("javascript:removeLocation()");
                popupWindow.dismiss();
                popupWindow = null;
            }
        });
        //回传点查显示属性键值对
        list_tile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
                title = titleDatas.get(position);
                webView.loadUrl("javascript:changeTopAttr('" + title + "','" + curPoint + "')");
//                title = "";
            }
        });

    }

    private void initPopView() {
        PropertyTitlesAdapter titlesAdapter;
        list_tile = view.findViewById(R.id.list_tile);
        list_info = view.findViewById(R.id.list_info);
        btn_guanbi = view.findViewById(R.id.btn_guanbi);
        titlesAdapter = new PropertyTitlesAdapter(titleDatas, context, title);
        list_tile.setAdapter(titlesAdapter);
        list_info.setAdapter(new PropertyListAdapter(context, datasInfo));
        if ("multi".equals(flag)) {
            if (titleDatas.size() > 1) {
                selectPosition = titleDatas.size() - 1;
            }
        }
        titlesAdapter.setSelectedItem(selectPosition);
        titlesAdapter.notifyDataSetChanged();
    }
}
