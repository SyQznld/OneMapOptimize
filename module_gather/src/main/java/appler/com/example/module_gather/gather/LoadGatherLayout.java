package appler.com.example.module_gather.gather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oneMap.module.common.bean.EventBusData;
import com.oneMap.module.common.bean.GatherData;
import com.oneMap.module.common.bean.LoginData;
import com.oneMap.module.common.dao.GatherDao;
import com.oneMap.module.common.dao.LoginDao;
import com.oneMap.module.common.global.Global;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.GlideUtils;
import com.oneMap.module.common.utils.LocationUtils;
import com.oneMap.module.common.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import appler.com.example.module_gather.R;
import appler.com.example.module_gather.picshow.LoadPicShowLayout;


/**
 * 采集页面布局
 */
public class LoadGatherLayout implements View.OnClickListener, PhotoOrVideoReturnListener {
    private String TAG = getClass().getSimpleName();
    private Activity context;
    private WebView webView;
    private LinearLayout linearLayout;
    private ImageButton ib_zhunxin;
    private Button btn_gather_point;
    private Button btn_gather_hand;
    private ImageView iv_gather_draw;
    private TextView tv_gather_back;
    private TextView tv_gather_clear;
    private TextView tv_gather_time;
    private TextView tv_gather_submit;
    private TextView tv_gather_draw;
    private TextView iv_gather_title;
    private TextView tv_gather_username;
    private EditText et_gather_tubanName;
    private EditText et_gather_tubanRemark;
    private CardView cv_gahter_draw;

    private ImageView iv_gather_close;
    private View gatherView;
    private String username;

    private String TYPE_DRAW = "draw";
    private String measureFlag = "huamian"; //与数据量算模块类似，采集模块只针对画面操作
    private String ZHUNXIN = "zhunxin";  //准心
    private String SHOUHUI = "shouhui";  //手绘
    private String operationType = "";//准心或手绘


    //图片部分
    private ImageView iv_pic;
    private TextView tv_picNum;
    private TextView tv_camera;
    private TextView tv_longitude;
    private TextView tv_latitude;


    private GatherData tbEditdata;
    private String polygon;
    private List<String> picList = new ArrayList<>();

    public LoadGatherLayout(Activity context, WebView webView, LinearLayout linearLayout, ImageButton ib_zhunxin) {
        EventBus.getDefault().register(this);
        this.context = context;
        this.webView = webView;
        this.linearLayout = linearLayout;
        this.ib_zhunxin = ib_zhunxin;
    }


    @SuppressLint("DefaultLocale")
    public void showGatherLayout(GatherData gatherData) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        LinearLayout.LayoutParams lp;
        boolean screenChange = CommonUtil.isScreenChange();
        if (screenChange) {  //横屏
            lp = new LinearLayout.LayoutParams(width / 4, LinearLayout.LayoutParams.FILL_PARENT);
        } else {
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        gatherView = inflater.inflate(R.layout.gather_layout, null);
        gatherView.setLayoutParams(lp);
        linearLayout.addView(gatherView);


        //初始化布局控件
        initView();

        //控件点击事件
        setViewOnClick();

        //初始化操作 准心
        initOperation();

        //初始化经纬度、时间以及登录用户名称
        initLnglatAndUser(gatherData);

    }

    private void initOperation() {
        setButtonBcgAndColor(TYPE_DRAW, btn_gather_point);
        operationType = ZHUNXIN;
        webView.loadUrl("javascript:mapTool.huamian('" + operationType + "')");
        ib_zhunxin.setVisibility(View.VISIBLE);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void initLnglatAndUser(GatherData gatherData) {
        //获取经纬度
        LocationUtils locationUtils = new LocationUtils();
        String s = locationUtils.initGpsSetting(context, webView);
        if (!"".equals(s)) {
            if (s.contains(",")) {
                if (!"0.0".equals(s.split(",")[0]) && !"0.0".equals(s.split(",")[1])) {
                    tv_longitude.setVisibility(View.VISIBLE);
                    tv_latitude.setVisibility(View.VISIBLE);
                    tv_longitude.setText("经度：" + String.format("%.5f", Double.valueOf(s.split(",")[0])));
                    tv_latitude.setText("纬度：" + String.format("%.6f", Double.valueOf(s.split(",")[1])));
                }
            }
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        tv_gather_time.setText(df.format(new Date()));

        LoginData loginData = LoginDao.getLoginData();
        if (null == loginData) {
            tv_gather_username.setText("");
        } else {
            username = loginData.getRealname();
            tv_gather_username.setText(username);
        }

        if (gatherData != null) {
            tbEditdata = gatherData;
            et_gather_tubanName.setText(tbEditdata.getGatherName());
            et_gather_tubanRemark.setText(tbEditdata.getGatherRemark());
            tv_gather_time.setText(tbEditdata.getGatherTime());
            polygon = tbEditdata.getGatherPolygon();
            tv_gather_draw.setVisibility(View.GONE);
            cv_gahter_draw.setVisibility(View.GONE);
            ib_zhunxin.setVisibility(View.GONE);
            iv_gather_title.setText("图斑编辑");
            String photoPath = tbEditdata.getGatherPhotos();
            if (!"".equals(photoPath)) {
                List<String> photoList = Arrays.asList(photoPath.split(","));
                returnPhotoOrVideo(context, photoList);
            }
            if (!"".equals(polygon) && !"null".equals(polygon) && null != polygon) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> polygons = Arrays.asList((polygon += "," + polygon.split(",")[0]).split(","));
                        polygon = "POLYGON ((" + TextUtils.join(", ", polygons) + "))";
                        webView.loadUrl("javascript:showFW('" + polygon + "')");
                    }
                }, 1000);
            }
        }
    }


    private void initView() {
        iv_gather_close = gatherView.findViewById(R.id.iv_gather_close);
        tv_gather_back = gatherView.findViewById(R.id.tv_gather_back);
        tv_gather_clear = gatherView.findViewById(R.id.tv_gather_clear);
        btn_gather_point = gatherView.findViewById(R.id.btn_gather_point);
        btn_gather_hand = gatherView.findViewById(R.id.btn_gather_hand);
        iv_gather_draw = gatherView.findViewById(R.id.iv_gather_draw);
        tv_gather_time = gatherView.findViewById(R.id.et_gather_time);
        tv_gather_submit = gatherView.findViewById(R.id.tv_gather_submit);
        et_gather_tubanName = gatherView.findViewById(R.id.et_gather_tubanName);
        et_gather_tubanRemark = gatherView.findViewById(R.id.et_gather_tubanRemark);
        tv_gather_username = gatherView.findViewById(R.id.tv_gather_username);
        tv_gather_draw = gatherView.findViewById(R.id.tv_gather_draw);
        cv_gahter_draw = gatherView.findViewById(R.id.cv_gahter_draw);
        iv_gather_title = gatherView.findViewById(R.id.iv_gather_title);

        iv_pic = gatherView.findViewById(R.id.iv_gather_pic);
        tv_picNum = gatherView.findViewById(R.id.tv_gatherpic_num);
        tv_camera = gatherView.findViewById(R.id.tv_gather_camera);
        tv_longitude = gatherView.findViewById(R.id.tv_gather_longitude);
        tv_latitude = gatherView.findViewById(R.id.tv_gather_latitude);

    }

    private void setViewOnClick() {
        iv_gather_close.setOnClickListener(this);
        tv_gather_back.setOnClickListener(this);
        tv_gather_clear.setOnClickListener(this);
        btn_gather_point.setOnClickListener(this);
        btn_gather_hand.setOnClickListener(this);
        iv_gather_draw.setOnClickListener(this);
        tv_gather_submit.setOnClickListener(this);
        iv_pic.setOnClickListener(this);
        tv_camera.setOnClickListener(this);

    }

    /**
     * 设置背景颜色以及字体颜色
     */
    private void setButtonBcgAndColor(String str, Button button) {
        if (TYPE_DRAW.equals(str)) {
            setDrawTypeBtn(btn_gather_point, btn_gather_hand);
        }

        button.setBackgroundResource(R.drawable.shape_measure_selected);
        button.setTextColor(CommonUtil.getDarkColorPrimary(context));
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_gather_close) {
            ib_zhunxin.setVisibility(View.GONE);
            linearLayout.removeView(gatherView);
            webView.loadUrl("javascript:measureClear()");
            webView.loadUrl("javascript:analysis_clearLocationPolygon()");
            webView.loadUrl("javascript:clearArrowMarker()");
        }
        if (i == R.id.tv_gather_back) {//撤销
            webView.loadUrl("javascript:Draw_backout('" + measureFlag + "')");
        }
        if (i == R.id.tv_gather_clear) {//清除
            webView.loadUrl("javascript:measureClear()");
        }
        if (i == R.id.btn_gather_point) {//准心
            operationType = ZHUNXIN;
            setButtonBcgAndColor(TYPE_DRAW, btn_gather_point);
            ib_zhunxin.setVisibility(View.VISIBLE);
            iv_gather_draw.setImageResource(R.drawable.ic_point);
            iv_gather_draw.setClickable(true);
            webView.loadUrl("javascript:mapTool.huamian('" + operationType + "')");
        }
        if (i == R.id.btn_gather_hand) {//手绘
            operationType = SHOUHUI;
            setButtonBcgAndColor(TYPE_DRAW, btn_gather_hand);
            ib_zhunxin.setVisibility(View.GONE);
            iv_gather_draw.setImageResource(R.drawable.ic_point_un);
            iv_gather_draw.setClickable(false);
            webView.loadUrl("javascript:mapTool.huamian('" + operationType + "')");
        }
        if (i == R.id.iv_gather_draw) {//绘制操作
            webView.loadUrl("javascript:getAccordinate('" + measureFlag + "')");
        }
        if (i == R.id.tv_gather_submit) {//保存
            if (!"".equals(et_gather_tubanName.getText().toString())) {
                if (null == tbEditdata) {
                    webView.loadUrl("javascript:measureSave('" + measureFlag + "','" + "gather" + "')");
                } else {
                    polygon = tbEditdata.getGatherPolygon();
                    setGatherData(polygon);
                }
            } else {
                ToastUtils.showShortToast("图斑名称不能为空");
            }
        }


        if (i == R.id.iv_gather_pic) {  //显示图片列表
            if (picList.size() > 0) {
                ib_zhunxin.setVisibility(View.GONE);
                linearLayout.removeAllViews();
                LoadPicShowLayout picShowLayout = new LoadPicShowLayout(context, webView, linearLayout);
                picShowLayout.showPicShowLayout(picList);
            } else {
                ToastUtils.showShortToast("暂无图片");
            }
        }
        if (i == R.id.tv_gather_camera) {  //拍摄图片、视频
            gatherTakeClickListener.gatherTake();
        }
    }

    //判断准心或是手绘
    private void setDrawTypeBtn(Button btn_gather_point, Button btn_gather_hand) {
        btn_gather_point.setBackgroundResource(R.drawable.shape_measure_unselected);
        btn_gather_hand.setBackgroundResource(R.drawable.shape_measure_unselected);
        btn_gather_point.setTextColor(context.getResources().getColor(R.color.text_908f94));
        btn_gather_hand.setTextColor(context.getResources().getColor(R.color.text_908f94));
    }


    /**
     * EventBus监听
     */
    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(EventBusData eventBusData) {
        switch (eventBusData.getMessage()) {
            case "gatherCoordinates":
                String gatherZuoBiao = eventBusData.getBzPolygon();
                if (!"".equals(et_gather_tubanName.getText().toString())) {
                    setGatherData(gatherZuoBiao);
                }
//                必须要解绑，不然会初始化多次
                EventBus.getDefault().unregister(this);
                break;
            case "gatherMeasureBack":
                linearLayout.removeAllViews();
                //实现重载界面可继续绘制点
                linearLayout.addView(gatherView);
                if (operationType == ZHUNXIN) {
                    ib_zhunxin.setVisibility(View.VISIBLE);
                }
                //上一页面图片操作后 实时返回
                String picListStr = eventBusData.getPicList();
                Log.i(TAG, "onMoonEvent: " + picListStr);
                if (!"".equals(picListStr)) {
                    tv_picNum.setVisibility(View.VISIBLE);
                    if (picListStr.contains("#")) {   //用特殊表示分割，因为图片命名中经纬度中间用的是逗号隔开
                        String[] split = picListStr.split("#");
                        tv_picNum.setText("共" + split.length + "张");
                        GlideUtils.loadNet(iv_pic, split[split.length - 1], R.mipmap.zrzy);
                    } else {
                        tv_picNum.setText("共1张");
                        GlideUtils.loadNet(iv_pic, picListStr, R.mipmap.zrzy);
                    }
                } else {
                    tv_picNum.setVisibility(View.GONE);
                    iv_pic.setImageResource(R.mipmap.zrzy);
                }
                break;
        }
    }

    /**
     * 获取到坐标值后 填充采集信息
     */
    private void setGatherData(String zuobiao) {
        String gatherName = et_gather_tubanName.getText().toString();
        String gatherRemark = et_gather_tubanRemark.getText().toString();
        String gatherTime = tv_gather_time.getText().toString();
        String gatherUsername = tv_gather_username.getText().toString();
        GatherData gatherData = new GatherData();
        gatherData.setGatherName(gatherName);
        gatherData.setGatherRemark(gatherRemark);
        gatherData.setGatherTime(gatherTime);
        gatherData.setGatherPeople(gatherUsername);
        String photoLocation = TextUtils.join(",", picList);
        gatherData.setGatherPhotos(photoLocation);
        String gahterJson = CommonUtil.readTxtFromSDCard(Global.TXT_SAVE_PATH, Global.GATHER_TXT);
        if ("".equals(gahterJson)) {
            GatherDao.deleteAllData();
        }
        if (null == tbEditdata) {
            gatherData.setGatherPolygon(zuobiao);
            GatherDao.insertGather(gatherData);
            webView.loadUrl("javascript:measureClear()");
            webView.loadUrl("javascript:measureEnd()");
            ToastUtils.showShortToast("图斑保存成功");
        } else {
            gatherData.setId(tbEditdata.getId());
            gatherData.setGatherPolygon(tbEditdata.getGatherPolygon());
            GatherDao.updateGather(gatherData);
            webView.loadUrl("javascript:analysis_clearLocationPolygon()");
            webView.loadUrl("javascript:clearArrowMarker()");
            ToastUtils.showShortToast("图斑修改成功");
        }
        linearLayout.removeView(gatherView);
        ib_zhunxin.setVisibility(View.GONE);

        //保存文件
        List<GatherData> gatData = GatherDao.queryAll();
        if (null != gatData) {
            String gatherJson = new Gson().toJson(gatData);
            File saveJsonFile = new File(Global.TXT_SAVE_PATH, Global.GATHER_TXT);
            if (saveJsonFile.exists()) {
                saveJsonFile.delete();
            }
            CommonUtil.saveStrToTxt(Global.TXT_SAVE_PATH, Global.GATHER_TXT, gatherJson);
        }

    }

    private GatherTakeClickListener gatherTakeClickListener;

    public void setGatherTakeClickListener(GatherTakeClickListener gatherTakeClickListener) {
        this.gatherTakeClickListener = gatherTakeClickListener;
    }


    public interface GatherTakeClickListener {
        void gatherTake();
    }


    /**
     * 接口回调
     * 返回拍摄的图片、视频总数及路径
     */
    @Override
    public void returnPhotoOrVideo(Context context, List<String> photoList) {
        picList.clear();
        picList.addAll(photoList);
        if (picList.size() > 0) {
            tv_picNum.setVisibility(View.VISIBLE);
            tv_picNum.setText("共" + picList.size() + "张");
        } else {
            tv_picNum.setVisibility(View.GONE);
        }
        //加载显示最后一次拍的图片或视频
        GlideUtils.loadNet(iv_pic, photoList.get(photoList.size() - 1), R.mipmap.zrzy);
    }


}
