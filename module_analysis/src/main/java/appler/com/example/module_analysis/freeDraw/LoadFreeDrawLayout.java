package appler.com.example.module_analysis.freeDraw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oneMap.module.common.bean.EventBusData;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.MainHandler;
import com.oneMap.module.common.utils.UploadDialogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import appler.com.example.module_analysis.Https.PcDataRequestHttps;
import appler.com.example.module_analysis.Interface.GetZuoBiao;
import appler.com.example.module_analysis.R;
import appler.com.example.module_analysis.bean.AnalysisData;
import appler.com.example.module_analysis.loadlayout.LoadAnalysisAllTypeLayout;

public class LoadFreeDrawLayout implements View.OnClickListener, GetZuoBiao {

    private String TAG = getClass().getSimpleName();
    private static Context context;
    private static LinearLayout linearLayout;
    private static WebView mWebView;

    private ImageButton ic_zhunxin;  //准心 按钮
    private ImageView mIvAnalysisMeasureClose;
    private TextView mTvAnalysisMeasurePost;
    private TextView mTvAnalysisMeasureArea;
    private TextView mTvAnalysisMeasureResult;
    private TextView mTvAnalysisMeasureBack;
    private TextView mTvAnalysisMeasureClear;
    private Button mBtnAnalysisMeasurePoint;
    private Button mBtnAnalysisMeasureHand;
    private ImageView mIvAnalysisMeasureDraw;
    private View analysisMeasureView;
    private static Dialog loadingDialog;
    private static List<AnalysisData> analysisDataList;
    private String TYPE_DRAW = "draw";
    private String measureFlag = "huamian"; //
    private String operationType = "";//准心或手绘
    private String ZHUNXIN = "zhunxin";  //准心
    private String SHOUHUI = "shouhui";  //手绘

    public LoadFreeDrawLayout(Context context, LinearLayout linearLayout, WebView mWebView, ImageButton ic_zhunxin) {
        LoadFreeDrawLayout.context = context;
        LoadFreeDrawLayout.linearLayout = linearLayout;
        LoadFreeDrawLayout.mWebView = mWebView;
        this.ic_zhunxin = ic_zhunxin;
        EventBus.getDefault().register(this);
    }


    public LoadFreeDrawLayout() {

    }

    public void showFreeDrawPop() {
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
        analysisMeasureView = inflater.inflate(R.layout.analysis_measure_layout, null);
        analysisMeasureView.setLayoutParams(lp);
        initView(analysisMeasureView);

        setButtonBcgAndColor(TYPE_DRAW, mBtnAnalysisMeasurePoint);
    }

    private void initView(View analysisMeasureView) {
        mIvAnalysisMeasureClose = analysisMeasureView.findViewById(R.id.iv_analysis_measure_close);
        mTvAnalysisMeasurePost = analysisMeasureView.findViewById(R.id.tv_analysis_measure_post);
        mTvAnalysisMeasureArea = analysisMeasureView.findViewById(R.id.tv_analysis_measure_area);
        mTvAnalysisMeasureResult = analysisMeasureView.findViewById(R.id.tv_analysis_measure_result);
        mTvAnalysisMeasureBack = analysisMeasureView.findViewById(R.id.tv_analysis_measure_back);
        mTvAnalysisMeasureClear = analysisMeasureView.findViewById(R.id.tv_analysis_measure_clear);
        mBtnAnalysisMeasurePoint = analysisMeasureView.findViewById(R.id.btn_analysis_measure_point);
        mBtnAnalysisMeasureHand = analysisMeasureView.findViewById(R.id.btn_analysis_measure_hand);
        mIvAnalysisMeasureDraw = analysisMeasureView.findViewById(R.id.iv_analysis_measure_draw);

        linearLayout.addView(analysisMeasureView);
        ic_zhunxin.setVisibility(View.VISIBLE);


        mIvAnalysisMeasureClose.setOnClickListener(this);
        mTvAnalysisMeasurePost.setOnClickListener(this);
        mTvAnalysisMeasureBack.setOnClickListener(this);
        mTvAnalysisMeasureClear.setOnClickListener(this);
        mBtnAnalysisMeasurePoint.setOnClickListener(this);
        mBtnAnalysisMeasureHand.setOnClickListener(this);
        mIvAnalysisMeasureDraw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        关闭
        if (id == R.id.iv_analysis_measure_close) {
            ic_zhunxin.setVisibility(View.GONE);
            linearLayout.removeView(analysisMeasureView);
            mWebView.loadUrl("javascript:measureClear()");
            EventBus.getDefault().unregister(this);
        }
//        分析提交
        if (id == R.id.tv_analysis_measure_post) {
            mWebView.loadUrl("javascript:measureSave('" + measureFlag + "','" + "analysis" + "')");
            mWebView.loadUrl("javascript:measureEnd()");
        }
//        撤销
        if (id == R.id.tv_analysis_measure_back) {
            mWebView.loadUrl("javascript:Draw_backout('" + measureFlag + "')");
        }
//        清除
        if (id == R.id.tv_analysis_measure_clear) {
            mTvAnalysisMeasureResult.setText("0平方米");
            mWebView.loadUrl("javascript:measureClear()");
        }
//        准心
        if (id == R.id.btn_analysis_measure_point) {
            operationType = ZHUNXIN;
            mWebView.loadUrl("javascript:mapTool.huamian('" + operationType + "')");
            setButtonBcgAndColor(TYPE_DRAW, mBtnAnalysisMeasurePoint);
            ic_zhunxin.setVisibility(View.VISIBLE);
            mIvAnalysisMeasureDraw.setImageResource(R.drawable.ic_point);
            mIvAnalysisMeasureDraw.setClickable(true);
        }
//        手绘
        if (id == R.id.btn_analysis_measure_hand) {
            operationType = SHOUHUI;
            mWebView.loadUrl("javascript:mapTool.huamian('" + operationType + "')");
            setButtonBcgAndColor(TYPE_DRAW, mBtnAnalysisMeasureHand);
            ic_zhunxin.setVisibility(View.GONE);
            mIvAnalysisMeasureDraw.setImageResource(R.drawable.ic_point_un);
            mIvAnalysisMeasureDraw.setClickable(false);
        }
//        准心绘制
        if (id == R.id.iv_analysis_measure_draw) {
            mWebView.loadUrl("javascript:getAccordinate('" + measureFlag + "')");
        }

    }

    //    自由绘制，请求数据接口
    private void getAnalysisData(String Coordinate) {
        loadingDialog = UploadDialogUtils.createLoadingDialog(context, "正在加载");
        PcDataRequestHttps.getInstance().requestZnfxBasicInfoList(Coordinate, new PcDataRequestHttps.AnalysisBasicListCallBack() {
            @Override
            public void onFailure() {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        UploadDialogUtils.closeDialog(loadingDialog);
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void AnalysisBasicListBack(String string) {
                Log.i(TAG, "ZnfxBasicListBack: string" + string);
                Gson gson = new Gson();
                analysisDataList = gson.fromJson(string, new TypeToken<List<AnalysisData>>() {
                }.getType());
                Activity activity = (Activity) context;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadAnalysisAllTypeLayout allTypeLayout = new LoadAnalysisAllTypeLayout(context, mWebView, linearLayout, analysisDataList, Coordinate);
                        allTypeLayout.showAnalysisAllTypeLayout();
                        UploadDialogUtils.closeDialog(loadingDialog);
                        EventBus.getDefault().unregister(this);
                    }
                });
            }
        });

    }


    /**
     * 设置背景颜色以及字体颜色
     */
    private void setButtonBcgAndColor(String str, Button button) {
        if (TYPE_DRAW.equals(str)) {
            setDrawTypeBtn(mBtnAnalysisMeasurePoint, mBtnAnalysisMeasureHand);
        }
        button.setBackgroundResource(R.drawable.shape_measure_selected);
//        button.setTextColor(Color.parseColor("#058AC5"));
        button.setTextColor(CommonUtil.getDarkColorPrimary(context));
    }

    //判断准心或是手绘
    private void setDrawTypeBtn(Button mBtnAnalysisMeasurePoint, Button mBtnAnalysisMeasureHand) {
        mBtnAnalysisMeasurePoint.setBackgroundResource(R.drawable.shape_measure_unselected);
        mBtnAnalysisMeasureHand.setBackgroundResource(R.drawable.shape_measure_unselected);
        mBtnAnalysisMeasurePoint.setTextColor(Color.parseColor("#000000"));
        mBtnAnalysisMeasureHand.setTextColor(Color.parseColor("#000000"));
    }

    //回调JS的接口，获得面的坐标值
    @Override
    public void getZuoBiao(String Coordinate) {
        Log.i(TAG, "GetZuoBiao: GetZuoBiao" + Coordinate);
        if (!"".equals(Coordinate) && !"null".equals(Coordinate) && null != Coordinate) {
            mWebView.loadUrl("javascript:showFW('" + Coordinate + "')");
            getAnalysisData(Coordinate);
        }
    }

    /**
     * EventBus监听
     */
    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(EventBusData eventBusData) {
        Log.i(TAG, "onMoonEvent: " + eventBusData.getMessage());
        if ("getAnalysisArea".equals(eventBusData.getMessage())) {
            mTvAnalysisMeasureResult.setText(eventBusData.getAnalysisArea());
        }
    }


}
