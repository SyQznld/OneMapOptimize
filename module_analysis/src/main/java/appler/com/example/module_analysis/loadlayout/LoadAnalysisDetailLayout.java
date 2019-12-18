package appler.com.example.module_analysis.loadlayout;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.oneMap.module.common.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import appler.com.example.module_analysis.R;
import appler.com.example.module_analysis.adapter.AnalysisResultAdapter;
import appler.com.example.module_analysis.bean.AnalysisDetailData;
import appler.com.example.module_analysis.manager.CombinedChartManager;

/**
 * 智能分析 结果 第二步  包含列表显示以及饼状图显示
 */
public class LoadAnalysisDetailLayout {
    private String TAG = getClass().getSimpleName();
    private static Context context;
    private WebView webView;
    private LinearLayout linearLayout;

    private ImageView iv_analysis_close;
    private ImageView iv_analysisresult_back;
    private TextView tv_analysisresult_type;
    private RecyclerView rv_analysis_result;
    private PieChart pieChart;
    private CombinedChart combinedChart;
    private Switch SwitchButton;
    private List<PieEntry> entries = new ArrayList<>();

    private View analysisView;


    public LoadAnalysisDetailLayout(Context context, WebView webView, LinearLayout linearLayout) {
        this.context = context;
        this.webView = webView;
        this.linearLayout = linearLayout;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showAnalysisDetailLayout(String name, List<AnalysisDetailData.RowsBean> data) {
        linearLayout.removeAllViews();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();

        LinearLayout.LayoutParams lp;
        boolean screenChange = CommonUtil.isScreenChange();
        if (screenChange) {  //横屏
            lp = new LinearLayout.LayoutParams(width / 4, LinearLayout.LayoutParams.FILL_PARENT);
        } else {
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, height / 3);
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        analysisView = inflater.inflate(R.layout.analysis_result_layout, null);
        analysisView.setLayoutParams(lp);
        linearLayout.addView(analysisView);

        //初始化布局控件
        initView(analysisView);
        tv_analysisresult_type.setText(name);

        //控件点击事件
        setViewOnClick();

        initData(data);
        initRecyclerViewData(data, context);

    }

    private void initData(List<AnalysisDetailData.RowsBean> data) {
        entries.clear();

        for (int i = 0; i < data.size(); i++) {
            entries.add(new PieEntry(Float.valueOf(data.get(i).getReArea()), data.get(i).get用地名称()));
        }

        //饼状图填充数据
        initPieEntry(entries);
        initBarchart(data);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setViewOnClick() {
        iv_analysisresult_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeAllViews();
                LoadAnalysisAllTypeLayout allTypeLayout = new LoadAnalysisAllTypeLayout(context, webView, linearLayout);
                allTypeLayout.showAnalysisAllTypeLayout();
                webView.loadUrl("javascript:clearQueryPolygons()");
            }
        });

        iv_analysis_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeView(analysisView);
//                webView.loadUrl("javascript:analysis_clearLocationPolygon()");
                webView.loadUrl("javascript:clearQueryPolygons()");

            }
        });

        SwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "onCheckedChanged: " + isChecked);
                if (isChecked) {
                    pieChart.setVisibility(View.GONE);
                    combinedChart.setVisibility(View.VISIBLE);
                } else {
                    combinedChart.setVisibility(View.GONE);
                    pieChart.setVisibility(View.VISIBLE);

                }
            }
        });


    }


    private void initPieEntry(List<PieEntry> entries) {

        pieChart.setUsePercentValues(true); //设置是否显示数据实体(百分比，true:以下属性才有意义)
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 5, 5, 5);//饼形图上下左右边距
        pieChart.setDrawSliceText(false);
        pieChart.setDragDecelerationFrictionCoef(0.95f);//设置pieChart图表转动阻力摩擦系数[0,1]

        //pieChart.setCenterTextTypeface(mTfLight);//设置所有DataSet内数据实体（百分比）的文本字体样式
        pieChart.setCenterText("分析结果");//设置PieChart内部圆文字的内容
        pieChart.setCenterTextSize(12);

        pieChart.setDrawHoleEnabled(true);//是否显示PieChart内部圆环(true:下面属性才有意义)
        pieChart.setHoleColor(Color.WHITE);//设置PieChart内部圆的颜色

        pieChart.setTransparentCircleColor(Color.WHITE);//设置PieChart内部透明圆与内部圆间距(31f-28f)填充颜色
        pieChart.setTransparentCircleAlpha(110);//设置PieChart内部透明圆与内部圆间距(31f-28f)透明度[0~255]数值越小越透明
        pieChart.setHoleRadius(28f);//设置PieChart内部圆的半径(这里设置28.0f)
        pieChart.setTransparentCircleRadius(31f);//设置PieChart内部透明圆的半径(这里设置31.0f)

        pieChart.setDrawCenterText(true);//是否绘制PieChart内部中心文本（true：下面属性才有意义）

        pieChart.setRotationAngle(0);//设置pieChart图表起始角度
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);//设置pieChart图表是否可以手动旋转
        pieChart.setHighlightPerTapEnabled(true);//设置piecahrt图表点击Item高亮是否可用

        //默认动画
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        pieChart.animateXY(1400, 1400);

        // pieChart.spin(2000, 0, 360);

        // 获取pieCahrt图列
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(10f); //设置图例实体之间延X轴的间距（setOrientation = HORIZONTAL有效）
        l.setYEntrySpace(0f); //设置图例实体之间延Y轴的间距（setOrientation = VERTICAL 有效）
        l.setYOffset(0f);//设置比例块Y轴偏移量


        // entry label styling
        pieChart.setEntryLabelColor(Color.BLACK);//设置pieChart图表文本字体颜色
//        pieChart.setEntryLabelTypeface(mTfRegular);//设置pieChart图表文本字体样式
        pieChart.setEntryLabelTextSize(8f);//设置pieChart图表文本字体大小

        PieDataSet dataSet = new PieDataSet(entries, "数据说明");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(5f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.BLACK);
//        data.setValueTypeface(mTfLight);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                for (int i = 0; i < entries.size(); i++) {
                    if (entries.get(i).getValue() == e.getY()) {
                        Toast.makeText(context, entries.get(i).getLabel(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });


    }


    //    直方图和曲线图初始化
    private void initBarchart(List<AnalysisDetailData.RowsBean> data) {

        //x轴数据
        List<String> xData = new ArrayList<>();
        //直方图y轴数据集合
        List<List<Float>> yBarDatas = new ArrayList<>();
        //y轴数
        List<Float> yBarData = new ArrayList<>();

        //线形图y轴数据集合
        List<List<Float>> yLineDatas = new ArrayList<>();
        //y轴数
        List<Float> yLineData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
//            xData.add(String.valueOf(i + 1));
            String ydmc = data.get(i).get用地名称();
            if(null==ydmc||"".equals(ydmc)){
                xData.add("");
            }else {
                xData.add(ydmc);
            }
            double bardata = Float.parseFloat(data.get(i).getReArea()) * 0.0015;
            double linedata = bardata / 2;
            yBarData.add((float) bardata);
            yLineData.add((float) linedata);
        }
        yBarDatas.add(yBarData);
        yLineDatas.add(yLineData);


        //颜色集合
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#058AC5"));
        colors.add(Color.parseColor("#fb8c00"));


        //管理类
        CombinedChartManager combineChartManager1 = new CombinedChartManager(combinedChart);
        combineChartManager1.showCombinedChart(xData, yBarDatas.get(0), yLineDatas.get(0),
                "直方图", "线性图", colors.get(0), colors.get(1));
    }


    private void initView(View analysisView) {
        iv_analysisresult_back = analysisView.findViewById(R.id.iv_analysisresult_back);
        iv_analysis_close = analysisView.findViewById(R.id.iv_analysisresult_close);
        tv_analysisresult_type = analysisView.findViewById(R.id.tv_analysisresult_type);
        rv_analysis_result = analysisView.findViewById(R.id.rv_analysis_result);
        pieChart = analysisView.findViewById(R.id.piechart_analysis);
        combinedChart = (CombinedChart) analysisView.findViewById(R.id.CombinedChart_analysis);
        SwitchButton = analysisView.findViewById(R.id.switchButton_analysis);
    }

    private void initRecyclerViewData(List<AnalysisDetailData.RowsBean> analysisDetailDataList, Context context) {
        rv_analysis_result.setLayoutManager(new LinearLayoutManager(context));
        rv_analysis_result.setAdapter(new AnalysisResultAdapter(context, analysisDetailDataList, webView));
    }


}
