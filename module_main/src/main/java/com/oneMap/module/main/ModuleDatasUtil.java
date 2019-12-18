package com.oneMap.module.main;

import android.view.View;

import com.oneMap.module.common.bean.ModuleData;
import com.oneMap.module.common.global.Global;
import com.oneMap.module.common.widget.materailintroview.shape.Focus;

import java.util.ArrayList;
import java.util.List;

public class ModuleDatasUtil {

    private String TAG = getClass().getSimpleName();

    public static ModuleData moduleData1, moduleData2, moduleData3, moduleData4, moduleData5,
            moduleData6, moduleData7, moduleData8, moduleData9, moduleData10, moduleData11;

    public static List<ModuleData> getModuleDatas(View ll_main_layer,
                                            View ll_main_measure,
                                            View ll_main_baidu,
                                            View ll_main_search,
                                            View ll_main_analysis,
                                            View ll_main_gather,
                                            View ll_main_more,
                                            View ib_screen,
                                            View ib_transparent,
                                            View ib_reset,
                                            View ib_location) {
        List<ModuleData> moduleDataList = new ArrayList<>();
        initModule(ll_main_layer, ll_main_measure, ll_main_baidu,ll_main_search, ll_main_analysis, ll_main_gather, ll_main_more,
                ib_screen,ib_transparent, ib_reset, ib_location);
        moduleDataList.add(moduleData1);
        moduleDataList.add(moduleData2);
        moduleDataList.add(moduleData3);
        moduleDataList.add(moduleData4);
        moduleDataList.add(moduleData5);
        moduleDataList.add(moduleData6);
        moduleDataList.add(moduleData8);
        moduleDataList.add(moduleData9);
        moduleDataList.add(moduleData10);
        moduleDataList.add(moduleData11);
        return moduleDataList;
    }


    public static void initModule(View ll_main_layer,
                            View ll_main_measure,
                            View ll_main_baidu,
                            View ll_main_search,
                            View ll_main_analysis,
                            View ll_main_gather,
                            View ll_main_more,
                            View ib_screen,
                            View ib_transparent,
                            View ib_reset,
                            View ib_location) {
        moduleData1 = new ModuleData();
        moduleData1.setModuleView(ll_main_layer);
        moduleData1.setId(Global.INTRO_FOCUS_MAIN_LAYER);
        moduleData1.setText("图层，点击显示图层树");
        moduleData1.setFocus(Focus.MINIMUM);

        moduleData2 = new ModuleData();
        moduleData2.setModuleView(ll_main_measure);
        moduleData2.setId(Global.INTRO_FOCUS_MAIN_MEASURE);
        moduleData2.setText("测量，点击测量距离或面积");
        moduleData2.setFocus(Focus.MINIMUM);


        moduleData3 = new ModuleData();
        moduleData3.setModuleView(ll_main_baidu);
        moduleData3.setId(Global.INTRO_FOCUS_MAIN_MEASURE);
        moduleData3.setText("搜索，根据关键字进行地名搜索");
        moduleData3.setFocus(Focus.MINIMUM);

        moduleData4 = new ModuleData();
        moduleData4.setModuleView(ll_main_search);
        moduleData4.setId(Global.INTRO_FOCUS_MAIN_SEARCH);
        moduleData4.setText("地名，地名地址定位");
        moduleData4.setFocus(Focus.MINIMUM);

        moduleData5 = new ModuleData();
        moduleData5.setModuleView(ll_main_analysis);
        moduleData5.setId(Global.INTRO_FOCUS_MAIN_ANALYSIS);
        moduleData5.setText("分析，计算出各类型占比，与图表结合");
        moduleData5.setFocus(Focus.MINIMUM);

        moduleData6 = new ModuleData();
        moduleData6.setModuleView(ll_main_gather);
        moduleData6.setId(Global.INTRO_FOCUS_MAIN_GATHER);
        moduleData6.setText("采集，自定义绘制图斑，提交保存");
        moduleData6.setFocus(Focus.MINIMUM);

        moduleData7 = new ModuleData();
        moduleData7.setModuleView(ll_main_more);
        moduleData7.setId(Global.INTRO_FOCUS_MAIN_MORE);
        moduleData7.setText("更多，地名地址搜索、地图缩放等");
        moduleData7.setFocus(Focus.MINIMUM);

        moduleData8 = new ModuleData();
        moduleData8.setModuleView(ib_screen);
        moduleData8.setId(Global.INTRO_FOCUS_MAIN_MORE);
        moduleData8.setText("屏幕方向，切换横竖屏");
        moduleData8.setFocus(Focus.MINIMUM);

        moduleData9 = new ModuleData();
        moduleData9.setModuleView(ib_transparent);
        moduleData9.setId(Global.INTRO_FOCUS_MAIN_TRANSPARENT);
        moduleData9.setText("透明度，拖动进度条控制图层透明度显示");
        moduleData9.setFocus(Focus.MINIMUM);

        moduleData10 = new ModuleData();
        moduleData10.setModuleView(ib_reset);
        moduleData10.setId(Global.INTRO_FOCUS_MAIN_RESET);
        moduleData10.setText("复位，回到地图初始加载位置");
        moduleData10.setFocus(Focus.MINIMUM);

        moduleData11 = new ModuleData();
        moduleData11.setModuleView(ib_location);
        moduleData11.setId(Global.INTRO_FOCUS_MAIN_LOCATION);
        moduleData11.setText("定位，定位到当前位置");
        moduleData11.setFocus(Focus.MINIMUM);
    }

}
