package com.oneMap.module.main;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

import com.oneMap.module.common.bean.EventBusData;
import com.oneMap.module.common.utils.MainHandler;
import com.oneMap.module.common.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import appler.com.example.module_analysis.Interface.GetZuoBiao;
import appler.com.example.module_analysis.freeDraw.LoadFreeDrawLayout;
import appler.com.example.module_layer.property.ShowPropertyPop;

/**
 * js与安卓交互事件
 */

public class JavaScriptinterface {
    private final String TAG = getClass().getSimpleName();
    private TextView textView;
    private ShowPropertyPop showPropertyPop;
    private Context mcontext;


    public JavaScriptinterface(TextView textView, ShowPropertyPop showPropertyPop, Context context) {
        this.textView = textView;
        this.showPropertyPop = showPropertyPop;
        this.mcontext = context;
    }


    /**
     * 点图查询 打开的图层全部属性信息
     */
    @JavascriptInterface
    public void skip(String title, String returnStr, String curPoint) {
        MainHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                List<String> datasName = new ArrayList<>();
                if (title.contains(",")) {
                    String[] datas = title.split(",");
                    for (int i = 0; i < datas.length; i++) {
                        datasName.add(datas[i]);
                    }
                } else {
                    datasName.add(title);
                }
                showPropertyPop.multiParams(datasName, returnStr, curPoint);
                showPropertyPop.showDianChaPop(textView);
            }
        });
    }


    /**
     * 选中当前选择图层的属性查询结果
     */
    @JavascriptInterface
    public void returnCurLayerDc(String attrs) {
        MainHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                showPropertyPop.singleParams(attrs);
                showPropertyPop.showDianChaPop(textView);
            }
        });
    }


    /**
     * 距离量算
     */
    @JavascriptInterface
    public void bzMesureResult(String type, String result) {
        getAnalysisArea(result);
        MainHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                EventBusData event = new EventBusData("measureResult");
                event.setMeasureType(type);
                event.setMeasureResult(result);
                EventBus.getDefault().post(event);
            }
        });
    }

    /**
     * 2019.08.09
     * 标注绘制完成点击事件  记录坐标点
     */
    @JavascriptInterface
    public void bzRecordCoordinates(final String accordinates, String measureFlag) {
        Log.i(TAG, "run:accordinates     " + accordinates + " measureFlag       " + measureFlag);
        MainHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                if (!"".equals(accordinates) && accordinates.contains(",")) {
                    String[] split = accordinates.split(",");
                    int coordinateCount = split.length;  //accordinates中包含坐标数
                    if ("huamian".equals(measureFlag) && coordinateCount < 3) {
                        ToastUtils.showShortToast("面积至少绘制三个点");
                    } else {
                        EventBusData event = new EventBusData("accordinates");
                        event.setBzPolygon(accordinates);
                        EventBus.getDefault().post(event);
                    }
                } else {
                    if ("huamian".equals(measureFlag)) {
                        ToastUtils.showShortToast("面积至少绘制三个点");
                    } else if ("ceju".equals(measureFlag)) {
                        ToastUtils.showShortToast("距离至少绘制两个点");
                    }
                }
            }

        });
    }

    /**
     * 2019.08.13 修改
     * 采集模块完成点击事件  记录坐标点
     */
    @JavascriptInterface
    public void gatherCoordinates(final String accordinates, String flag) {
        MainHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                if (!"".equals(accordinates) && "analysis".equals(flag)) {
                    getAnalysisi_AreaPoint(accordinates);
                } else if (!"".equals(accordinates)) {
                    EventBusData event = new EventBusData("gatherCoordinates");
                    event.setBzPolygon(accordinates);
                    EventBus.getDefault().post(event);
                } else {
                    if ("".equals(accordinates)) {
                        ToastUtils.showShortToast("请至少绘制三个点");
                    }
                }
            }
        });
    }

    /**
     * 智能分析 自由绘制 得到绘制范围的坐标
     *
     * @param Coordinate
     */
    @JavascriptInterface
    public void getAnalysisi_Area(final String Coordinate) {

    }

    //    智能分析获取画的点
    private void getAnalysisi_AreaPoint(String accordinates) {
        String[] accordinates1 = accordinates.split(",");
        String POLYGON = "";
        for (int i = 0; i < accordinates1.length; i++) {
            if (i == accordinates1.length - 1) {
                POLYGON = POLYGON + accordinates1[i] + ", " + accordinates1[0];
            } else {
                POLYGON = POLYGON + accordinates1[i] + ", ";
            }
        }
        POLYGON = "POLYGON ((" + POLYGON + "))";
        Log.i(TAG, "getAnalysisi_AreaPoint: " + POLYGON);
        GetZuoBiao zuoBiao = new LoadFreeDrawLayout();
        zuoBiao.getZuoBiao(POLYGON);
    }

    //智能分析获取画的面积
    private void getAnalysisArea(String area) {
        String[] areaArray = area.split(" ");
        String area1 = "";
        for (int i = 1; i < areaArray.length; i++) {
            area1 = area1 + areaArray[i] + "   ";
        }
        EventBusData event = new EventBusData("getAnalysisArea");
        event.setAnalysisArea(area1);
        EventBus.getDefault().post(event);
    }

    /**
     * 采集  获取坐标
     *
     * @param zuobiao
     */
    @JavascriptInterface
    public void getGatherZuobiao(final String zuobiao) {
        Log.i("", "GetZuoBiao zuobiao: " + zuobiao);
        MainHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                EventBusData event = new EventBusData("gatherZuobiao");
                event.setGatherZuoBiao(zuobiao);
                EventBus.getDefault().post(event);
            }
        });
    }

    /**
     * 采集测量界面返回
     */
    @JavascriptInterface
    public void gatherMeasureBackLayout(String picListStr) {
        MainHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "gatherMeasureBackLayout: " + picListStr);
                EventBusData event = new EventBusData("gatherMeasureBack");
                event.setPicList(picListStr);
                EventBus.getDefault().post(event);
            }
        });
    }

    @JavascriptInterface
    public void loadBaseLayer(String baseLayer) {
        Log.i(TAG, "loadBaseLayer: " + baseLayer);
        MainHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                EventBusData event = new EventBusData("baseLayer");
                event.setBaseLayer(baseLayer);
                EventBus.getDefault().post(event);
            }
        });
    }

    @JavascriptInterface
    public void getBaiduLatlng(String bdLng,String bdLat) {
        Log.i(TAG, "getBaiduLatlng: " + bdLng + " " + bdLat);
        MainHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                EventBusData event = new EventBusData("bdLatlng");
                event.setBdLng(bdLng);
                event.setBdLat(bdLat);
                EventBus.getDefault().post(event);
            }
        });
    }

}
