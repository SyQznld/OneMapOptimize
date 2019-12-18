package com.oneMap.module.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.oneMap.module.common.bean.EventBusData;
import com.oneMap.module.common.bean.GatherData;
import com.oneMap.module.common.bean.ModuleData;
import com.oneMap.module.common.global.ArouterConstants;
import com.oneMap.module.common.global.Global;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.LocationUtils;
import com.oneMap.module.common.utils.ToastUtils;
import com.oneMap.module.common.widget.MaterialIntroView;
import com.oneMap.module.common.widget.materailintroview.animation.MaterialIntroListener;
import com.oneMap.module.common.widget.materailintroview.shape.Focus;
import com.oneMap.module.common.widget.materailintroview.shape.FocusGravity;
import com.oneMap.module.common.widget.seekbar.OnSeekBarChangeListener;
import com.oneMap.module.common.widget.seekbar.VerticalSeekBar;
import com.oneMap.module.main.loadlayout.LoadMeasureLayout;
import com.oneMap.module.main.moreOperation.ShowMoreOperationPop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import appler.com.example.module_analysis.LoadAnalysisPopLayout;
import appler.com.example.module_gather.gather.LoadGatherLayout;
import appler.com.example.module_gather.gather.PhotoOrVideoReturnListener;
import appler.com.example.module_gather.selectpic.ImageSelectUtils;
import appler.com.example.module_gather.tbitem.LoadTbItemInfoLayout;
import appler.com.example.module_layer.layerTree.MyDb;
import appler.com.example.module_layer.layerTree.ShowLayerPop;
import appler.com.example.module_layer.property.ShowPropertyPop;
import appler.com.example.module_layer.search.LoadSearchLayout;

import static com.oneMap.module.common.global.Global.CONFIG_MODULE;
import static com.oneMap.module.common.global.Global.PHOTOORVIDEO_SAVE_PATH;


/**
 * 主页面
 */
@Route(path = ArouterConstants.TO_MAINACTIVITY)
public class MainActivity extends Activity implements View.OnClickListener, MaterialIntroListener {

    private String TAG = getClass().getSimpleName();
    private long mExitTime = 0;
    private WebView mMWebView;
    /**
     * 返回、查找、减加、定位、清除，透明度工具条
     */
    private ImageButton ib_zhunxin;
    private ImageButton ib_location;
    private ImageButton ib_reset;
    private ImageButton ib_screen;    //屏幕方向
    private ImageButton ib_transparent;
    private LinearLayout ll_seekbar;   //透明度
    private VerticalSeekBar verticalSeekBar;   //进度条
    private TextView tv_transparent;
    private boolean isSeekbarShow;  //进度条布局是否显示
    private TextView mTvGps;

    /**
     * 功能操作
     */
    private TextView tv_main_layer;
    private LinearLayout ll_main_layer;
    private TextView tv_main_measure;
    private LinearLayout ll_main_measure;
    private TextView tv_main_search;
    private LinearLayout ll_main_search;
    private TextView tv_main_baidu;
    private LinearLayout ll_main_baidu;
    private TextView tv_main_analysis;
    private LinearLayout ll_main_analysis;
    private TextView tv_main_gather;
    private LinearLayout ll_main_gather;
    private TextView tv_main_more;
    private LinearLayout ll_main_more;

    //页面布局懒加载
    private LinearLayout ll_load;

    //弹框pop
    private ShowLayerPop showLayerPop;  //图层树弹框
    private ShowPropertyPop showPropertyPop;  //点查弹框pop
    private ShowMoreOperationPop moreOperationPop;  //主页-更多操作
    /**
     * 引导用户view
     */
    private static int j = 0;
    //获取到的经纬度坐标
    private String lnglat;

    //采集页面传值
    @Autowired(name = "gatherData")
    GatherData gatherData;
    //编辑操作 回传实体类对象
    @Autowired(name = "tbEditData")
    GatherData tbEditData;

    //拍照相关变量声明
    private String defaultPhotoAddress;
    private List<String> photoList;
    private int PHOTO_REQUEST_CODE = 10;
    private int VIDEO_REQUEST_CODE = 11;
    private static final int ALBUM_REQUEST_CODE = 0x00000011;
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];
    private float[] values;
    private String formatvalues;
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor magnetic;

    private File file;
    private String videoFileName;
    private LoadGatherLayout gatherLayout;

    private String screenOrientation; //屏幕方向
    private List<String> screenList;
    private List<ModuleData> moduleDataList = new ArrayList<>();
    private String TopLayer = "";

    private boolean isLocation;  //是否定位

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置主题颜色
        setTheme();

        //当在Android的layout设计里面如果输入框过多，则在输入弹出软键盘的时候，下面的输入框会有一部分被软件盘挡住，从而不能获取焦点输入。
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);



        /**如果不添加这句话 获取到的值为null*/
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
        //控件初始化
        initView();
        setScreenOrientation();

        //主页模块显示
        showModule();

        //设置动态权限、GPS、webView、图片/视频命名
        initSetting();

        //初始化弹框 图层树、点查、搜索
        initPop();

        if (moduleDataList.size() > 0) {
            View view = moduleDataList.get(0).getModuleView();
            String id = moduleDataList.get(0).getId();
            String text = moduleDataList.get(0).getText();
            if (view.getVisibility() == Global.VISIBLE_NUMBER) {
                showIntro(view, id, text, Focus.MINIMUM);
            }
        }

        //获取到采集图斑条目的详情信息
        initGatherItemLayout();

        //判断方向
        initSensor();
    }


    /**
     * 设置动态权限、GPS、webView、图片视频
     */
    private void initSetting() {

        //定位设置
        LocationUtils locationUtils = new LocationUtils();
        lnglat = locationUtils.initGpsSetting(MainActivity.this, mMWebView);

        //webview初始化
        initWebView();

        //图片视频
        initPhotoAndVideo();
    }


    private void initPhotoAndVideo() {
        defaultPhotoAddress = CommonUtil.getRWPath(MainActivity.this) + File.separator + ".jpg";
        file = videoRename();
    }

    /**
     * 初始化WebView
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        showPropertyPop = new ShowPropertyPop(MainActivity.this, mMWebView);
        mMWebView.addJavascriptInterface(new JavaScriptinterface(tv_main_layer, showPropertyPop, MainActivity.this), "android");
        mMWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;//
            }
        });


        String sdPath = CommonUtil.getRWPath(MainActivity.this);
        WebSettings webSettings = mMWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false); //隐藏webview缩放按钮
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        mMWebView.loadUrl("file:///android_asset/map.html?SDPath=" + sdPath);
    }


    @Override
    public void onClick(View view) {
        /**
         * 当我们在Android依赖库中使用switch-case语句访问资源ID时会报错，报的错误是case分支后面跟的参数必须是常数，
         * 换句话说出现这个问题的原因是Android library中生成的R.java中的资源ID不是常数，所以现在只能用if（）语句
         * /

         /**
         *  功能操作
         * */
        if (view.getId() == R.id.tv_main_layer) {  //图层列表
            String mapConfigPath = CommonUtil.getRWPath(MainActivity.this) + File.separator + "mapdata" + File.separator + "MapConfig.txt";
            if (new File(mapConfigPath).exists()) {
                ib_zhunxin.setVisibility(View.GONE);
                mMWebView.loadUrl("javascript:measureClear()");
                ll_load.removeAllViews();
                showLayerPop.showPopupWindow(tv_main_layer);
                mMWebView.loadUrl("javascript:removeSelectedPolygon()");
            } else {
                ToastUtils.showShortToast("请检查地图配置文件是否为存在");
            }
        }
        if (view.getId() == R.id.tv_main_measure) {  //距离量算弹框
            mMWebView.loadUrl("javascript:mapTool.removeLocationMarker()");
            ll_load.removeAllViews();
            LoadMeasureLayout loadMeasureLayout = new LoadMeasureLayout(MainActivity.this, ll_load, mMWebView, ib_zhunxin);
            loadMeasureLayout.showMeasureLayout();
            mMWebView.loadUrl("javascript:removeSelectedPolygon()");
        }
        if (view.getId() == R.id.tv_main_baidu) {  //搜索，百度导航
            ll_load.removeAllViews();
            ARouter.getInstance().build(ArouterConstants.TO_BAIDUACTIVITY).navigation(MainActivity.this,Global.MAIN_BAIDUDAOHANG_CODE);
        }
        if (view.getId() == R.id.tv_main_search) {  //地名，地名地址搜索
            ib_zhunxin.setVisibility(View.GONE);
            mMWebView.loadUrl("javascript:measureClear()");
            mMWebView.loadUrl("javascript:removeSelectedPolygon()");
            ll_load.removeAllViews();
            LoadSearchLayout searchLayout = new LoadSearchLayout(MainActivity.this, mMWebView, ll_load);
            searchLayout.showSearchLayout();
        }
        if (view.getId() == R.id.tv_main_analysis) {  //智能分析
            ib_zhunxin.setVisibility(View.GONE);
            mMWebView.loadUrl("javascript:measureClear()");
            mMWebView.loadUrl("javascript:removeSelectedPolygon()");
            ll_load.removeAllViews();
            LoadAnalysisPopLayout loadAnalysisPopLayout = new LoadAnalysisPopLayout(this, mMWebView, ll_load, ib_zhunxin);
            loadAnalysisPopLayout.showFenxiPop(tv_main_analysis);
        }
        if (view.getId() == R.id.tv_main_gather) {  //信息采集
            photoList = new ArrayList<>();
            ib_zhunxin.setVisibility(View.GONE);
            mMWebView.loadUrl("javascript:measureClear()");
            mMWebView.loadUrl("javascript:removeSelectedPolygon()");
            ll_load.removeAllViews();

            loadGatherLayout();
        }
        if (view.getId() == R.id.tv_main_more) {  //更多弹框显示
            mMWebView.loadUrl("javascript:measureClear()");
            moreOperationPop.showPop(tv_main_more);
        }


        /**
         * 左下角 透明度 复位 定位
         * */
        if (view.getId() == R.id.ib_screen) {  //调整屏幕方向
            SharedPreferences sPreferences = getSharedPreferences("onemap", MODE_PRIVATE);
            String screen = sPreferences.getString("screen", "");
            if ("竖屏".equals(screen)) {      //放横屏
                screen = "横屏";
                ib_screen.setImageResource(R.drawable.ic_screen_land);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//固定屏幕方向为横屏
            } else if ("横屏".equals(screen)) {   //放竖屏
                screen = "竖屏";
                ib_screen.setImageResource(R.drawable.ic_screen_vertical);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//固定屏幕方向为竖屏
            }

            SharedPreferences.Editor editor = sPreferences.edit();
            //保存屏幕方向
            editor.putString("screen", screen);
            //切记最后要使用commit方法将数据写入文件
            editor.commit();
            mMWebView.loadUrl("javascript:mapTool.fuwei()");
        }
        if (view.getId() == R.id.ib_transparent) {  //透明度
            setLayerTransparent();
        }
        if (view.getId() == R.id.ib_reset) {  //复位
            mMWebView.loadUrl("javascript:mapTool.fuwei()");
        }

        if (view.getId() == R.id.ib_location) {  //定位
            if (isLocation) {
                mMWebView.loadUrl("javascript:mapTool.removeLocationMarker()");
                isLocation = false;
            } else {
                lnglat = new LocationUtils().initGpsSetting(MainActivity.this, mMWebView);
                if (!"".equals(lnglat) && !"null".equals(lnglat) && null != lnglat) {
                    if (lnglat.contains(",")) {
                        double longitude = Double.parseDouble(lnglat.split(",")[0]);
                        double latitude = Double.parseDouble(lnglat.split(",")[1]);
                        if (0.0 != longitude && 0.0 != latitude) {
                            mMWebView.loadUrl("javascript:mapTool.dingwei('" + longitude + "','" + latitude + "')");
                        } else {
                            ToastUtils.showShortToast("请位于宽阔地带，并检查位置信息是否打开");
                            return;
                        }
                    }
                } else {
                    ToastUtils.showShortToast("请位于宽阔地带，并检查位置信息是否打开");
                    return;
                }
                isLocation = true;
            }
        }
    }

    /**
     * 设置屏幕方向
     */
    private void setScreenOrientation() {
        //显示用户此前保存的数据
        SharedPreferences sPreferences = getSharedPreferences("onemap", MODE_PRIVATE);
        String screen = sPreferences.getString("screen", "");
        if (!"".equals(screen) && null != screen && !"null".equals(screen)) {
            if ("竖屏".equals(screen)) {
                ib_screen.setImageResource(R.drawable.ic_screen_land);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//固定屏幕方向
            } else if ("横屏".equals(screen)) {
                ib_screen.setImageResource(R.drawable.ic_screen_vertical);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//固定屏幕方向
            }
        } else {
            screenList = new ArrayList<>();
            screenList.add("竖屏");
            screenList.add("横屏");
            screenOrientation = screenList.get(0);
            new MaterialDialog.Builder(MainActivity.this)
                    .title(R.string.setScreen)
                    .content(">>>可以点击左下角切换按钮进行屏幕设置")
                    .items(R.array.screenOrientation)
                    .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            screenOrientation = screenList.get(which);
                            if ("竖屏".equals(screenOrientation)) {
                                ib_screen.setImageResource(R.drawable.ic_screen_land);
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//固定屏幕方向
                            } else if ("横屏".equals(screenOrientation)) {
                                ib_screen.setImageResource(R.drawable.ic_screen_vertical);
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//固定屏幕方向
                            }
                            SharedPreferences.Editor editor = sPreferences.edit();
                            //保存屏幕方向
                            editor.putString("screen", screenOrientation);
                            //切记最后要使用commit方法将数据写入文件
                            editor.commit();
                            return false;
                        }
                    }).onPositive(new MaterialDialog.SingleButtonCallback() {
                @SuppressLint("ApplySharedPref")
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    if (which == DialogAction.POSITIVE) {
                        dialog.dismiss();

                    } else if (which == DialogAction.NEGATIVE) {
                        dialog.dismiss();
                    }
                }
            })
                    .positiveText("确定")
                    .negativeText("取消")
                    .show();
        }
    }



    /**
     * 加载采集界面布局
     */
    private void loadGatherLayout() {
        gatherLayout = new LoadGatherLayout(this, mMWebView, ll_load, ib_zhunxin);
        gatherLayout.showGatherLayout(tbEditData);
        TakeCamera();
    }


    /**
     * 透明度调整代码
     */
    private void setLayerTransparent() {
        if (isSeekbarShow) {
            ll_seekbar.setVisibility(View.GONE);
            isSeekbarShow = false;
        } else {
            if (!"".equals(TopLayer)) {
                ll_seekbar.setVisibility(View.VISIBLE);
                isSeekbarShow = true;
                verticalSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onStartTrackingTouch(double progressInPercent) {
                    }

                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    @Override
                    public void onStopTrackingTouch(double progressInPercent) {
                        setSeekbar(progressInPercent, TopLayer);
                    }

                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    @Override
                    public void onProgressChange(double progressInPercent) {
                        setSeekbar(progressInPercent, TopLayer);
                    }
                });

                verticalSeekBar.setProgressBarBackgroundColor(R.color.bg_transparent);
                verticalSeekBar.setProgressBarColor(R.color.white_10);
                verticalSeekBar.setThumbColor(getResources().getColor(R.color.bg_transparent));
            } else {
                ToastUtils.showShortToast("暂无地图数据，无法调整透明度");
            }
        }
    }


    //设置seekbar的值
    @SuppressLint("SetTextI18n")
    private void setSeekbar(double progressInPercent, String layertitle) {

        @SuppressLint("DefaultLocale") String format = String.format("%.0f", progressInPercent);
        tv_transparent.setText(format + "%");
        mMWebView.loadUrl("javascript:setLayerOpacity('" + layertitle + "','" + format + "')");
    }


    /**
     * 图斑对应操作
     */
    private void initGatherItemLayout() {
        //获取到采集图斑条目的详情信息
        if (null != gatherData) {
            ll_load.removeAllViews();
            LoadTbItemInfoLayout gatherItemLayout = new LoadTbItemInfoLayout(MainActivity.this, mMWebView, ll_load);
            gatherItemLayout.showTbItemInfoLayout(gatherData);
        }
        //图斑详情编辑的详情信息
        if (null != tbEditData) {
            ll_load.removeAllViews();
            loadGatherLayout();
        }
    }


    /**
     * 采集模块 选取图片弹框
     */
    private void TakeCamera() {
        gatherLayout.setGatherTakeClickListener(new LoadGatherLayout.GatherTakeClickListener() {
            @Override
            public void gatherTake() {
                new MaterialDialog.Builder(MainActivity.this)
                        .items(R.array.take_photo)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                switch (position) {
                                    case 0: //本地相册
                                        ImageSelectUtils.openPhoto(MainActivity.this, ALBUM_REQUEST_CODE, false, 9);
                                        break;
                                    case 1: //拍照
                                        Uri imageUri;
                                        File outputImage = new File(defaultPhotoAddress);
                                        //调用系统拍照
                                        if (Build.VERSION.SDK_INT >= 24) {
                                            imageUri = FileProvider.getUriForFile(MainActivity.this, "com.oneMap.module.common.fileprovider", outputImage);
                                        } else {
                                            imageUri = Uri.fromFile(outputImage);
                                        }
                                        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                        startActivityForResult(photoIntent, PHOTO_REQUEST_CODE);
                                        break;
                                    case 2: //视频
                                        Intent videoIntent = new Intent();
                                        videoIntent.setAction("android.media.action.VIDEO_CAPTURE");
                                        videoIntent.addCategory("android.intent.category.DEFAULT");

                                        if (file.exists()) {
                                            file.delete();
                                        }
                                        Uri uri2;
                                        if (Build.VERSION.SDK_INT >= 24) {
                                            uri2 = FileProvider.getUriForFile(MainActivity.this, "com.oneMap.module.common.fileprovider", file);
                                        } else {
                                            uri2 = Uri.fromFile(file);
                                        }
                                        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri2);
                                        videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                        videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
                                        startActivityForResult(videoIntent, VIDEO_REQUEST_CODE);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
    }


    //初始化pop
    private void initPop() {
        moreOperationPop = new ShowMoreOperationPop(MainActivity.this, tv_main_more, ll_load, mMWebView, ib_zhunxin);   //主页-更多操作
    }


    //视频命名
    @SuppressLint("SimpleDateFormat")
    private File videoRename() {
        videoFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".mp4";
        //创建文件夹
        File out = new File(PHOTOORVIDEO_SAVE_PATH);
        if (!out.exists()) {
            out.mkdirs();
        }
        return new File(PHOTOORVIDEO_SAVE_PATH + File.separator + videoFileName);
    }

    /**
     * 可配置主页显示模块
     */
    private void showModule() {
        String rwPath = CommonUtil.getRWPath(MainActivity.this);
        String modulePath = rwPath + File.separator + Global.CONFIG_FLODER + File.separator + CONFIG_MODULE + ".txt";
        if (new File(modulePath).exists()) {
            String moduleStr = CommonUtil.readFile(modulePath);
            moduleStr = CommonUtil.removeUnnecessarySpace(moduleStr);
            if (!"".equals(moduleStr)) {
                if (!moduleStr.contains("图层")) {
                    ll_main_layer.setVisibility(View.GONE);
                }
                if (!moduleStr.contains("测量")) {
                    ll_main_measure.setVisibility(View.GONE);
                }
                if (!moduleStr.contains("搜索")) {
                    ll_main_baidu.setVisibility(View.GONE);
                }
                if (!moduleStr.contains("地名")) {
                    ll_main_search.setVisibility(View.GONE);
                }
                if (!moduleStr.contains("分析")) {
                    ll_main_analysis.setVisibility(View.GONE);
                }
                if (!moduleStr.contains("采集")) {
                    ll_main_gather.setVisibility(View.GONE);
                }
                if (!moduleStr.contains("更多")) {
                    ll_main_more.setVisibility(View.GONE);
                }
            }
        }
        String dmdzTxt = rwPath + File.separator + Global.CONFIG_FLODER + File.separator + Global.CONFIG_DZDZ + ".txt";
        if (!new File(dmdzTxt).exists()) {
            ll_main_search.setVisibility(View.GONE);
        }else {
            ll_main_search.setVisibility(View.VISIBLE);
        }

        List<ModuleData> moduleDatas = ModuleDatasUtil.getModuleDatas(
                ll_main_layer, ll_main_measure,ll_main_baidu, ll_main_search, ll_main_analysis, ll_main_gather,  ll_main_more,
                ib_screen,ib_transparent, ib_reset, ib_location);
        moduleDataList.addAll(moduleDatas);
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onUserClicked(String materialIntroViewId) {
        for (int i = j; i < moduleDataList.size(); i++) {
            if (i < moduleDataList.size() - 1) {
                View view1 = moduleDataList.get(i + 1).getModuleView();
                String id1 = moduleDataList.get(i + 1).getId();
                String text1 = moduleDataList.get(i + 1).getText();
                Focus focus = moduleDataList.get(i + 1).getFocus();
                if (view1.getVisibility() == Global.VISIBLE_NUMBER) {
                    showIntro(view1, id1, text1, focus);
                    j = i + 1;
                    break;
                }
            } else {
                View view1 = moduleDataList.get(i).getModuleView();
                String id1 = moduleDataList.get(i).getId();
                String text1 = moduleDataList.get(i).getText();
                Focus focus = moduleDataList.get(i).getFocus();
                if (view1.getVisibility() == Global.VISIBLE_NUMBER) {
                    showIntro(view1, id1, text1, focus);
                }
            }
        }
    }


    /**
     * 提示文字与view控件绑定，以某种样式弹出
     */
    public void showIntro(View view, String id, String text, Focus focusType) {
        new MaterialIntroView.Builder(MainActivity.this)
                .enableDotAnimation(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(focusType)
                .setDelayMillis(200)
                .enableFadeAnimation(true)
                .setListener(this)
                .performClick(true)
                .setInfoText(text)
                .setTarget(view)
                .setUsageId(id) //THIS SHOULD BE UNIQUE ID
                .show();
    }


    /**
     * 图片路径保存
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Global.MAIN_BAIDUDAOHANG_CODE){//百度导航
                String lng = data.getStringExtra("Lng");
                String lat = data.getStringExtra("Lat");
                String address = data.getStringExtra("Address");
                String detailedAddress = data.getStringExtra("DetailedAddress");
                mMWebView.loadUrl("javascript:mapTool.baiduLocation('" + lng + "','" + lat + "')");
            }else{
                if (requestCode == PHOTO_REQUEST_CODE) {
                    //文件夹目录是否存在
                    File folderAddr = new File(Global.PHOTOORVIDEO_SAVE_PATH);
                    if (!folderAddr.exists() || !folderAddr.isDirectory()) {
                        folderAddr.mkdirs();
                    }
                    //将原图片压缩拷贝到指定目录
                    @SuppressLint("SimpleDateFormat") String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    String lnglat = new LocationUtils().initGpsSetting(MainActivity.this, mMWebView);
                    //图片路径：基本路径+时间+经纬度+角度
                    final String photoPaths = Global.PHOTOORVIDEO_SAVE_PATH + File.separator + time + "&" + lnglat + "&" + formatvalues + ".jpg";
                    String targetPath = photoPaths.replace(',', '$');
                    CommonUtil.dealImage(defaultPhotoAddress, targetPath);
                    // 删除原图
                    new File(defaultPhotoAddress).delete();
                    if (photoList == null) {
                        photoList = new ArrayList<>();
                    }
                    photoList.add(targetPath);

                } else if (requestCode == VIDEO_REQUEST_CODE) {
                    if (photoList == null) {
                        photoList = new ArrayList<>();
                    }
                    photoList.add(Global.PHOTOORVIDEO_SAVE_PATH + File.separator + videoFileName);
                } else if (requestCode == ALBUM_REQUEST_CODE) {//添加附件  从系统图库选择图片
                    ArrayList<String> images = data.getStringArrayListExtra(ImageSelectUtils.SELECT_RESULT);
                    photoList.addAll(images);
                }
                PhotoOrVideoReturnListener photoOrVideoReturnListener = gatherLayout;
                photoOrVideoReturnListener.returnPhotoOrVideo(MainActivity.this, photoList);
            }
        }
    }


    public void initSensor() {
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        // 初始化加速度传感器
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 初始化地磁场传感器
        magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        calculateOrientation();
    }
    private class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = event.values;
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticFieldValues = event.values;
            }
            calculateOrientation();
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

    }
    //计算角度值
    public String calculateOrientation() {
        values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        // 要经过一次数据格式的转换，转换为度
        values[0] = (float) Math.toDegrees(values[0]);
        DecimalFormat df = new DecimalFormat("######0.00");
        formatvalues = df.format(values[0]);
        return formatvalues;
    }


    /**
     * view初始化和加监听事件
     */
    @SuppressLint("WrongViewCast")
    private void initView() {
        mMWebView = findViewById(R.id.mWebView);
        mMWebView.setOnClickListener(this);

        ib_zhunxin = findViewById(R.id.ib_zhunxin);

        ib_screen = findViewById(R.id.ib_screen);
        ib_screen.setOnClickListener(this);
        ib_transparent = findViewById(R.id.ib_transparent);
        ib_transparent.setOnClickListener(this);
        ib_reset = findViewById(R.id.ib_reset);
        ib_reset.setOnClickListener(this);
        ib_location = findViewById(R.id.ib_location);
        ib_location.setOnClickListener(this);
        mTvGps = findViewById(R.id.tv_gps);
        mTvGps.setOnClickListener(this);

        tv_main_layer = findViewById(R.id.tv_main_layer);
        ll_main_layer = findViewById(R.id.ll_main_layer);
        tv_main_layer.setOnClickListener(this);
        tv_main_measure = findViewById(R.id.tv_main_measure);
        ll_main_measure = findViewById(R.id.ll_main_measure);
        tv_main_measure.setOnClickListener(this);
        tv_main_search = findViewById(R.id.tv_main_search);
        ll_main_search = findViewById(R.id.ll_main_search);
        tv_main_search.setOnClickListener(this);
        tv_main_baidu = findViewById(R.id.tv_main_baidu);
        ll_main_baidu = findViewById(R.id.ll_main_baidu);
        tv_main_baidu.setOnClickListener(this);
        tv_main_analysis = findViewById(R.id.tv_main_analysis);
        ll_main_analysis = findViewById(R.id.ll_main_analysis);
        tv_main_analysis.setOnClickListener(this);
        tv_main_gather = findViewById(R.id.tv_main_gather);
        ll_main_gather = findViewById(R.id.ll_main_gather);
        tv_main_gather.setOnClickListener(this);
        tv_main_more = findViewById(R.id.tv_main_more);
        ll_main_more = findViewById(R.id.ll_main_more);
        tv_main_more.setOnClickListener(this);

        ll_seekbar = findViewById(R.id.ll_seekbar);
        verticalSeekBar = findViewById(R.id.verticalSeekBar);
        tv_transparent = findViewById(R.id.tv_transparent);
        ll_load = findViewById(R.id.ll_load);
    }


    /**
     * 点击两次退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //两秒之内按返回键就会退出
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtils.showShortToast(getString(R.string.app_exit_hint));
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 设置主题颜色
     */
    private void setTheme() {
        SharedPreferences sPreferences = getSharedPreferences("theme", MODE_PRIVATE);
        String themeColor = sPreferences.getString("themeColor", "");
        if (!"".equals(themeColor) && null != themeColor && !"null".equals(themeColor)) {
            switch (themeColor) {
                case "yellow":
                    setTheme(R.style.YellowAppTheme);
                    break;
                case "orange":
                    setTheme(R.style.OrangeAppTheme);
                    break;
                case "pink":
                    setTheme(R.style.PinkAppTheme);
                    break;
                case "red":
                    setTheme(R.style.RedAppTheme);
                    break;
                case "blue":
                    setTheme(R.style.BlueAppTheme);
                    break;
                case "green":
                    setTheme(R.style.GreenAppTheme);
                    break;
                case "purple":
                    setTheme(R.style.PurpleAppTheme);
                    break;
                case "black":
                    setTheme(R.style.BlckAppTheme);
                    break;
            }
        } else {
            setTheme(R.style.BlckAppTheme);
        }
    }



    @Override
    public void onResume() {
        // 注册监听
        super.onResume();
        //设置屏幕方向
//        setScreenOrientation();
        mSensorManager.registerListener(new MySensorEventListener(), accelerometer, Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(new MySensorEventListener(), magnetic, Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onPause() {
        // 解除注册
        super.onPause();
        mSensorManager.unregisterListener(new MySensorEventListener());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //数据库版本信息
        MyDb myDb = new MyDb(this, 1);
        SQLiteDatabase sqLiteDatabase = myDb.getWritableDatabase();
        sqLiteDatabase.close();

        EventBus.getDefault().unregister(this);
    }

    /**
     * EventBus监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(EventBusData eventBusData) {
        //加载的基础图层
        if (null != eventBusData.getBaseLayer() && !"".equals(eventBusData.getBaseLayer())) {
            showLayerPop = new ShowLayerPop(MainActivity.this, mMWebView, eventBusData.getBaseLayer());   //图层树
        }

        //置顶图层
        if (null != eventBusData.getTopLayer() && !"".equals(eventBusData.getTopLayer())) {
            TopLayer = eventBusData.getTopLayer();
        }

        //百度经纬度点击事件
        String bdLng = eventBusData.getBdLng();
        String bdLat = eventBusData.getBdLat();
        if (null != bdLng && null != bdLat){
            boolean baiduAvilible = CommonUtil.isAvilible(MainActivity.this, "com.baidu.BaiduMap");
            if (baiduAvilible) {        //安装有百度地图，进行导航
                try {
                    Intent intent = Intent.getIntent("intent://map/direction?" +
                            "destination=latlng:" + bdLat + "," + bdLng + "|name:终点 " +        //终点
                            "&mode=driving&" +          //导航路线方式
                            "region=河南" +           //
                            "&src=河南#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                    startActivity(intent); //启动调用
                } catch (URISyntaxException e) {
                    Log.e("intent", e.getMessage());
                }
            } else {
                Toast.makeText(MainActivity.this, "您尚未安装百度地图", Toast.LENGTH_LONG).show();
                new MaterialDialog.Builder(MainActivity.this)
                        .title("安装应用")
                        .content("前往下载百度地图！")
                        .positiveText("确定")
                        .negativeText("取消")
                        .widgetColor(Color.BLUE)//不再提醒的checkbox 颜色
                        .onAny(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (which == DialogAction.POSITIVE) {
                                    Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
                                    Intent intent_ = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent_);
                                } else if (which == DialogAction.NEGATIVE) {
                                    dialog.dismiss();
                                }
                            }
                        }).show();
            }
        }
    }
}
