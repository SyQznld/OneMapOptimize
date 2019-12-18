package com.oneMap.module.common.global;


import com.oneMap.module.common.base.BaseApplication;
import com.oneMap.module.common.utils.CommonUtil;

import java.io.File;

/**
 * 全局 变量   存放一些常量值以及接口flag等
 */
public class Global {

    public static final String BASE_URL_IP = "http://119.29.231.80:8064/";      //公司服务器
//        public static final String BASE_URL_IP = "http://192.168.1.253:85/";          //ZCX
    public static final String URL_HOUZHUI = "api/analysis.ashx";
    public static final String URL = BASE_URL_IP + URL_HOUZHUI;


    /**
     * 图层树目录以及点查配置文件 文件夹名称常量
     */
    public static final String CONFIG_FLODER = "一张图配置";
    public static final String CONFIG_LAYER_TREE = "数据清单";
    public static final String CONFIG_PROPERTY_FOLDER = "点查json";
    public static final String CONFIG_DZDZ = "DMDZ";
    public static final String CONFIG_MODULE = "ModuleConfig";
    public static final String TXT_SAVE_FOLDER = "Txt保存";
    public static final String CONFIG_DOCDESCRIBE = "一张图管理系统简要说明";



    /**
     * 保存文件路径   txt
     */
    public static String TXT_SAVE_PATH = CommonUtil.getSDPath()
            + File.separator
            + TXT_SAVE_FOLDER
            + File.separator;
    public static final String BZ_TXT = "BZ.txt";
    public static final String GATHER_TXT = "GATHER.txt";
    public static final String ANALYSIS_CACHE = "AnalysisCache.txt";

    //照片视频
    public static String PHOTOORVIDEO_SAVE_PATH = CommonUtil.getRWPath(BaseApplication.getBaseApplication().getContext())
            + File.separator
            + CONFIG_FLODER
            + File.separator
            + "图斑采集";

    //  测试用的
    public static String TEST = CommonUtil.getRWPath(BaseApplication.getBaseApplication().getContext())
            + File.separator
            + CONFIG_FLODER
            + File.separator
            + "ZUOBIAO_TEST.txt";


    //在线智能分析表名
    public static final String TABLE_CSGH = "hj_zg";
    public static final String TABLE_XZDL = "bg_2017";
    public static final String TABLE_TDLYZTGH = "tdlyztgh";
    public static final String TABLE_JSYDGZQ = "jsydgzq";


    /**
     * 主页 按钮 引导步骤
     */
    public static final String INTRO_FOCUS_MAIN_LAYER = "intro_focus_layer";
    public static final String INTRO_FOCUS_MAIN_MEASURE = "intro_focus_measure";
    public static final String INTRO_FOCUS_MAIN_SEARCH = "intro_focus_search";
    public static final String INTRO_FOCUS_MAIN_ANALYSIS = "intro_focus_analysis";
    public static final String INTRO_FOCUS_MAIN_GATHER = "intro_focus_gather";
    public static final String INTRO_FOCUS_MAIN_TUBANLIST = "intro_focus_tubanlist";
    public static final String INTRO_FOCUS_MAIN_SETTING = "intro_focus_setting";
    public static final String INTRO_FOCUS_MAIN_MORE = "intro_focus_more";
    public static final String INTRO_FOCUS_MAIN_TRANSPARENT = "intro_focus_transparent";
    public static final String INTRO_FOCUS_MAIN_RESET = "intro_focus_reset";
    public static final String INTRO_FOCUS_MAIN_LOCATION = "intro_focus_location";

    public static final int VISIBLE_NUMBER = 0;
    public static final int GONE_NUMBER = 8;



    public static final int MAIN_BAIDUDAOHANG_CODE = 999;


    public static final String BAIDU_CITY_NAME = "许昌";

}
