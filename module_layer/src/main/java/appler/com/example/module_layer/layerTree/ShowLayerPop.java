package appler.com.example.module_layer.layerTree;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oneMap.module.common.bean.EventBusData;
import com.oneMap.module.common.global.Global;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.MainHandler;
import com.oneMap.module.common.utils.ToastUtils;
import com.oneMap.module.common.utils.XmlToJson;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import appler.com.example.module_layer.R;
import appler.com.example.module_layer.layerTree.tree.LayerBean;
import appler.com.example.module_layer.layerTree.tree.ShpJson;
import appler.com.example.module_layer.layerTree.tree.TreeAdapter;
import appler.com.example.module_layer.layerTree.tree.TreeItem;


/**
 * 图层树 PopupWindow
 */
public class ShowLayerPop extends PopupWindow {
    private static final String TAG = "ShowPop";

    private WebView webView;
    private LayoutInflater inflater;
    private Context context;
    private View view;
    private PopupWindow popupwindow;

    private List<TreeItem> layerTreeDataList = new ArrayList<>();

    private boolean layerFileIsExist;  //判断图层文件是否存在
    private String baseLayer;
    /**
     * 8/28 于俊涛
     * 修改图层列表
     */
    private RecyclerView rv_Tree;
    private String propertyTxtpath;
    private int Reb = Color.rgb(178, 34, 34);
    public ShowLayerPop(Context context, WebView webView, String baseLayer) {
        this.context = context;
        this.webView = webView;
        this.baseLayer = baseLayer;
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layer_pop_layout, null);
        rv_Tree = view.findViewById(R.id.rvTree);
        //一张图配置--》点查json--》txt
        String storagePath = CommonUtil.getRWPath(context);
        propertyTxtpath = storagePath + File.separator + Global.CONFIG_FLODER + File.separator + Global.CONFIG_PROPERTY_FOLDER + File.separator;
        layerFileIsExist = initData(context);
        if (!layerFileIsExist) {
            ToastUtils.showShortToast("请检查图层配置文件是否存在");
        }

    }


    private boolean initData(Context context) {
        layerTreeDataList.clear();
        String data = "";
        String str = "";
        /**
         * 读取本地配置文件     优先读取SD卡,没SD卡读取内部存储      图层树目录 三级列表显示
         */
        String folderPath = CommonUtil.getRWPath(context) + File.separator + Global.CONFIG_FLODER + File.separator;
        List<String> allFilePathsByRoot = CommonUtil.getAllFilePathsByRoot(folderPath);
        //循环文件夹，匹配到数据清单文件，包含
        for (int ii = 0; ii < allFilePathsByRoot.size(); ii++) {
            String desFilePath = allFilePathsByRoot.get(ii);
            if (desFilePath.contains(Global.CONFIG_LAYER_TREE)) {
                if (new File(desFilePath).exists()) {
                    //按行读取
                    try {
                        File file = new File(desFilePath);
                        InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
                        BufferedReader bf = new BufferedReader(inputReader);
                        // 按行读取字符串
                        while ((str = bf.readLine()) != null) {
                            data = data + str;
                        }
                        bf.close();
                        inputReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String jsonString = XmlToJson.convertXmlToJson(data);
                    LayerBean layerBean = new Gson().fromJson(jsonString, new TypeToken<LayerBean>() {
                    }.getType());
                    List<TreeItem> list = initList(layerBean);
                    TreeAdapter treeAdapter = new TreeAdapter(context, list, webView);
                    rv_Tree.setLayoutManager(new LinearLayoutManager(context));
                    rv_Tree.setAdapter(treeAdapter);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }


    public void showPopupWindow(View parent) {
        if (layerFileIsExist) {  //图层配置文件存在

            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = manager.getDefaultDisplay().getWidth();
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;

            LinearLayout.LayoutParams lp;
            boolean screenChange = CommonUtil.isScreenChange();
            if (screenChange) {  //横屏
                width = width / 3;
                lp = new LinearLayout.LayoutParams(width, height);
            } else {
                width = width * 2 / 5;
                lp = new LinearLayout.LayoutParams(width, height);
            }

            view.setLayoutParams(lp);
            popupwindow = new PopupWindow(view, width, height, false);
            popupwindow.setBackgroundDrawable(new BitmapDrawable());
            popupwindow.setOutsideTouchable(true);
            popupwindow.setTouchable(true);
            //左负右正      上负下正
            int x = -(width + 5);
            int y = -(parent.getHeight());
            popupwindow.showAsDropDown(parent, x, y);
        } else {
            ToastUtils.showShortToast("请检查图层配置文件是否存在");
        }
    }


    private List<TreeItem> initList(LayerBean layerBean) {
        List<TreeItem> list = new ArrayList<>();
        List<LayerBean.AllLayerBean> allLayerBeans = layerBean.getAll_Layer();
        if (null != allLayerBeans && allLayerBeans.size() > 0) {
            List<LayerBean.AllLayerBean.OneLayerBean> oneLayerBeanList = allLayerBeans.get(0).getOne_Layer();
            for (int i = 0; i < oneLayerBeanList.size(); i++) {
                TreeItem One_Layer = new TreeItem();
                if (null != oneLayerBeanList.get(i).getName() && !"".equals(oneLayerBeanList.get(i).getName())) {
                    One_Layer.Layer_name = oneLayerBeanList.get(i).getName();
                    One_Layer.layerItemData = new ArrayList<>();
                    List<LayerBean.AllLayerBean.OneLayerBean.TwoLayerBean> twoLayerBeanList = oneLayerBeanList.get(i).getTwo_Layer();
                    for (int j = 0; j < twoLayerBeanList.size(); j++) {
                        if (null != twoLayerBeanList.get(j).getName() && !"".equals(twoLayerBeanList.get(j).getName())) {
                            TreeItem Two_Layer = new TreeItem();
                            Two_Layer.Layer_name = twoLayerBeanList.get(j).getName();
                            Two_Layer.layerItemData = new ArrayList<>();
                            List<LayerBean.AllLayerBean.OneLayerBean.TwoLayerBean.ThreeLayerBean> threeLayerBeanList = twoLayerBeanList.get(j).getThree_Layer();
                            for (int k = 0; k < threeLayerBeanList.size(); k++) {
                                TreeItem Three_Layer = new TreeItem();
                                Three_Layer.Layer_name = threeLayerBeanList.get(k).getLayer_name();
                                Three_Layer.Layer_SDName = threeLayerBeanList.get(k).getLayer_SDName();
                                Three_Layer.Layer_Type = threeLayerBeanList.get(k).getLayer_Type();
                                Three_Layer.IsDiancha = threeLayerBeanList.get(k).getIS_DianCha();
                                Three_Layer.ShpColor = threeLayerBeanList.get(k).getShp_Color();
                                Three_Layer.ShpBZKey = threeLayerBeanList.get(k).getShp_BZKey();
                                Three_Layer.LayerViewCenter = threeLayerBeanList.get(k).getLayerViewCenter();
                                Three_Layer.layerItemData = new ArrayList<>();
                                if (baseLayer.equals(Three_Layer.Layer_SDName)) {
                                    ShowBaseLayer(Three_Layer);
                                } else {
                                    Three_Layer.setCheck(false);
                                }
                                Two_Layer.layerItemData.add(Three_Layer);
                            }
                            One_Layer.layerItemData.add(Two_Layer);
                        } else {
                            TreeItem Two_Layer = new TreeItem();
                            Two_Layer.Layer_name = twoLayerBeanList.get(j).getLayer_name();
                            Two_Layer.Layer_SDName = twoLayerBeanList.get(j).getLayer_SDName();
                            Two_Layer.Layer_Type = twoLayerBeanList.get(j).getLayer_Type();
                            Two_Layer.IsDiancha = twoLayerBeanList.get(j).getIS_DianCha();
                            Two_Layer.ShpColor = twoLayerBeanList.get(j).getShp_Color();
                            Two_Layer.ShpBZKey = twoLayerBeanList.get(j).getShp_BZKey();
                            Two_Layer.LayerViewCenter = twoLayerBeanList.get(j).getLayerViewCenter();
                            if (baseLayer.equals(Two_Layer.Layer_SDName)) {
                                ShowBaseLayer(Two_Layer);
                            } else {
                                Two_Layer.setCheck(false);
                            }
                            One_Layer.layerItemData.add(Two_Layer);
                        }
                    }
                }
                list.add(One_Layer);
            }
        } else {
            Toast.makeText(context, "没有图层数据", Toast.LENGTH_LONG).show();
        }
        return list;
    }

    private void ShowBaseLayer(TreeItem treeItem) {
        treeItem.check = true;
        treeItem.setZhiDing_RGB(Reb);
        ShpJson shpJson = new ShpJson();
        String shpJsonString = shpJson.setShpJson(treeItem.getIsDiancha(), propertyTxtpath, treeItem.getLayer_name(),
                treeItem.getLayer_SDName(), treeItem.getShpColor(), treeItem.getShpBZKey(), true,treeItem.getLayerViewCenter());
        webView.loadUrl("javascript:loadLayers('" + treeItem.getLayer_Type() + "'," + shpJsonString + ")");
        PostLayerName(treeItem.getLayer_name());
    }

    private void PostLayerName(String layerName) {
        MainHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                EventBusData event = new EventBusData("TopLayer");
                event.setTopLayer(layerName);
                EventBus.getDefault().post(event);
            }
        });
    }
}