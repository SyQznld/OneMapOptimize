package com.oneMap.module.main.loadlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneMap.module.common.bean.EventBusData;
import com.oneMap.module.common.utils.CommonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import appler.com.example.module_layer.R;
import appler.com.example.module_tbtrack.LoadBZRemarkLayout;

/**
 * 距离量算弹框动态加载显示  保留原有数据量算功能
 */
public class LoadMeasureLayout {

    private String TAG = getClass().getSimpleName();
    private String TYPE_MEASURE = "measure";
    private String TYPE_DRAW = "draw";
    private String MEASURE_FLAG_DISTANCE = "ceju";
    private String MEASURE_FLAG_AREA = "huamian";
    private String ZHUNXIN = "zhunxin";  //准心
    private String SHOUHUI = "shouhui";  //手绘

    private Button btn_measure_distance;
    private Button btn_measure_area;
    private Button btn_measure_point;
    private Button btn_measure_hand;
    private TextView tv_measure_back;
    private TextView tv_measure_clear;
    private TextView tv_measure_result;
    private ImageView iv_measure_draw;
    private ImageView iv_measure_close;
    private TextView tv_measure_save;


    private Context context;
    private LinearLayout linearLayout;
    private WebView webView;

    private ImageButton ib_zhunxin;  //准心 按钮
    private String measureFlag = "";//量算类型
    private String operationType = "";//准心或手绘
    private View measureView;   //用于量算返回的view

    public LoadMeasureLayout(Context context, LinearLayout linearLayout, WebView webView, ImageButton ib_zhunxin) {
        EventBus.getDefault().register(this);
        this.context = context;
        this.linearLayout = linearLayout;
        this.webView = webView;
        this.ib_zhunxin = ib_zhunxin;
    }

    public void showMeasureLayout() {
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
        measureView = inflater.inflate(R.layout.measure_layout, null);
        measureView.setLayoutParams(lp);
        linearLayout.addView(measureView);
        ib_zhunxin.setVisibility(View.VISIBLE);

        //初始化控件
        initView(measureView);

        //初始化默认操作  默认距离、准心
        initOperation();

        //控件点击事件
        viewOnClick(measureView);

    }

    private void viewOnClick(View measureView) {
        iv_measure_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_zhunxin.setVisibility(View.GONE);
                linearLayout.removeView(measureView);
                webView.loadUrl("javascript:measureClear()");
            }
        });
        btn_measure_distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("javascript:measureClear()");
                tv_measure_result.setText("0米");
                measureFlag = MEASURE_FLAG_DISTANCE;
                setButtonBcgAndColor(TYPE_MEASURE, btn_measure_distance);
                webView.loadUrl("javascript:mapTool.ceju('" + operationType + "')");
            }
        });
        btn_measure_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("javascript:measureClear()");
                tv_measure_result.setText("0平方米");
                measureFlag = MEASURE_FLAG_AREA;
                setButtonBcgAndColor(TYPE_MEASURE, btn_measure_area);
                webView.loadUrl("javascript:mapTool.huamian('" + operationType + "')");
            }
        });
        btn_measure_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationType = ZHUNXIN;
                setButtonBcgAndColor(TYPE_DRAW, btn_measure_point);
                ib_zhunxin.setVisibility(View.VISIBLE);
                iv_measure_draw.setImageResource(R.drawable.ic_point);
                iv_measure_draw.setClickable(true);
                if (measureFlag.equals(MEASURE_FLAG_DISTANCE)) {
                    webView.loadUrl("javascript:mapTool.ceju('" + operationType + "')");
                } else if (measureFlag.equals(MEASURE_FLAG_AREA)) {
                    webView.loadUrl("javascript:mapTool.huamian('" + operationType + "')");
                }
            }
        });
        btn_measure_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationType = SHOUHUI;
                setButtonBcgAndColor(TYPE_DRAW, btn_measure_hand);
                ib_zhunxin.setVisibility(View.GONE);
                iv_measure_draw.setImageResource(R.drawable.ic_point_un);
                iv_measure_draw.setClickable(false);
                if (measureFlag.equals(MEASURE_FLAG_DISTANCE)) {
                    webView.loadUrl("javascript:mapTool.ceju('" + operationType + "')");
                } else if (measureFlag.equals(MEASURE_FLAG_AREA)) {
                    webView.loadUrl("javascript:mapTool.huamian('" + operationType + "')");
                }
            }
        });

        tv_measure_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("javascript:Draw_backout('" + measureFlag + "')");
            }
        });
        tv_measure_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("javascript:measureClear()");
                if (measureFlag.equals(MEASURE_FLAG_DISTANCE)) {
                    tv_measure_result.setText("0米");
                    webView.loadUrl("javascript:mapTool.ceju('" + operationType + "')");
                } else if (measureFlag.equals(MEASURE_FLAG_AREA)) {
                    tv_measure_result.setText("0平方米");
                    webView.loadUrl("javascript:mapTool.huamian('" + operationType + "')");
                }
            }
        });
        iv_measure_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("javascript:getAccordinate('" + measureFlag + "')");
            }
        });

        tv_measure_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeView(measureView);
                webView.loadUrl("javascript:measureSave('" + measureFlag + "','" + "measure" + "')");
                webView.loadUrl("javascript:measureEnd()");
            }
        });
    }


    private void initOperation() {
        setButtonBcgAndColor(TYPE_MEASURE, btn_measure_distance);
        setButtonBcgAndColor(TYPE_DRAW, btn_measure_point);
        measureFlag = MEASURE_FLAG_DISTANCE;
        operationType = ZHUNXIN;
        webView.loadUrl("javascript:mapTool.ceju('" + operationType + "')");
    }

    private void initView(View measureView) {
        iv_measure_close = measureView.findViewById(R.id.iv_measure_close);
        btn_measure_distance = measureView.findViewById(R.id.btn_measure_distance);
        btn_measure_area = measureView.findViewById(R.id.btn_measure_area);
        btn_measure_point = measureView.findViewById(R.id.btn_measure_point);
        btn_measure_hand = measureView.findViewById(R.id.btn_measure_hand);
        tv_measure_back = measureView.findViewById(R.id.tv_measure_back);
        tv_measure_clear = measureView.findViewById(R.id.tv_measure_clear);
        tv_measure_result = measureView.findViewById(R.id.tv_measure_result);
        iv_measure_draw = measureView.findViewById(R.id.iv_measure_draw);
        tv_measure_save = measureView.findViewById(R.id.tv_measure_save);
    }


    /**
     * 设置背景颜色以及字体颜色
     */
    private void setButtonBcgAndColor(String str, Button button) {
        if (TYPE_MEASURE.equals(str)) {
            setMeasureBtn(btn_measure_distance, btn_measure_area);
        } else if (TYPE_DRAW.equals(str)) {
            setDrawBtn(btn_measure_point, btn_measure_hand);
        }

        button.setBackgroundResource(R.drawable.shape_measure_selected);
//        button.setTextColor(Color.parseColor("#058AC5"));
        button.setTextColor(CommonUtil.getDarkColorPrimary(context));
    }

    //判断距离或者面积
    private void setMeasureBtn(Button btn_measure_distance, Button btn_measure_area) {
        btn_measure_distance.setBackgroundResource(R.drawable.shape_measure_unselected);
        btn_measure_area.setBackgroundResource(R.drawable.shape_measure_unselected);
        btn_measure_distance.setTextColor(context.getResources().getColor(R.color.text_908f94));
        btn_measure_area.setTextColor(context.getResources().getColor(R.color.text_908f94));
    }

    //判断准心或是手绘
    private void setDrawBtn(Button btn_measure_point, Button btn_measure_hand) {
        btn_measure_point.setBackgroundResource(R.drawable.shape_measure_unselected);
        btn_measure_hand.setBackgroundResource(R.drawable.shape_measure_unselected);
        btn_measure_point.setTextColor(context.getResources().getColor(R.color.text_908f94));
        btn_measure_hand.setTextColor(context.getResources().getColor(R.color.text_908f94));
    }


    /**
     * EventBus监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(EventBusData eventBusData) {
        switch (eventBusData.getMessage()) {
            case "measureResult":
                String measureResult = eventBusData.getMeasureResult();

                if ("0".equals(measureResult) || "".equals(measureResult)) {
                    if (measureFlag.equals(MEASURE_FLAG_DISTANCE)) {
                        measureResult = "0米";
                    }
                    if (measureFlag.equals(MEASURE_FLAG_AREA)) {
                        measureResult = "0平方米";
                    }
                }
                String s = CommonUtil.removeUnnecessarySpace(measureResult);
                if (s.contains(" ")) {
                    String[] s1 = s.split(" ");
                    if (s1.length > 0) {
                        String zc = "周长：" + s1[0] + "\r\n";
                        String pfm = "面积：" + s1[1] + "\r\n";
                        String mu = "            " + s1[2] + "\r\n";
                        String gq = "            " + s1[3];
                        s = zc + pfm + mu + gq;
                    }
                }
                tv_measure_result.setText(s);
                break;
            case "accordinates":
                linearLayout.removeAllViews();
                final String bzPolygon = eventBusData.getBzPolygon();
                LoadBZRemarkLayout tbRemarkLayout = new LoadBZRemarkLayout(context, linearLayout, webView, ib_zhunxin, measureFlag, tv_measure_result.getText().toString(), bzPolygon);
                tbRemarkLayout.showTbRemarkLayout();
//                EventBus.getDefault().unregister(this);
                break;
            case "bzMeasureBack":
                linearLayout.removeAllViews();
                //实现重载界面可继续绘制点
                linearLayout.addView(measureView);
                break;
        }
    }

}
