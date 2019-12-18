package appler.com.example.module_analysis.loadlayout;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneMap.module.common.global.Global;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.UploadDialogUtils;

import java.util.List;

import appler.com.example.module_analysis.Https.GetBasicDetail;
import appler.com.example.module_analysis.Interface.GetAnalysisDetail;
import appler.com.example.module_analysis.R;
import appler.com.example.module_analysis.bean.AnalysisData;
import appler.com.example.module_analysis.bean.AnalysisDetailData;

/**
 * 基础分析  第一步  分析出乡、县、面积等多种类型基本数据
 */
public class LoadAnalysisAllTypeLayout implements View.OnClickListener, GetAnalysisDetail {
    private String TAG = getClass().getSimpleName();
    private Context context;
    private WebView webView;
    private LinearLayout linearLayout;

    private ImageView iv_close;
    private static List<AnalysisData> data;
    private LinearLayout ll_cun;
    private LinearLayout ll_City_gh;
    private LinearLayout ll_tdlyztgh;
    private LinearLayout ll_jsydgzq;

    private TextView tv_mianji;
    private TextView tv_qu;
    private TextView tv_xiang;
    private TextView tv_cun;
    private LinearLayout ll_xzdl;
    private TextView tv_City_gh;
    private TextView tv_tdlyztgh;
    private TextView tv_jsydgzq;

    private View analysisView;
    private TextView tv_xzdl;
    private static String Coordinate;
    private String name;
    private Dialog loadingDialog;

    public LoadAnalysisAllTypeLayout(Context context, WebView webView, LinearLayout linearLayout) {
        this.context = context;
        this.webView = webView;
        this.linearLayout = linearLayout;
    }

    public LoadAnalysisAllTypeLayout(Context context, WebView webView, LinearLayout linearLayout,
                                     List<AnalysisData> data, String Coordinate) {
        this.context = context;
        this.webView = webView;
        this.linearLayout = linearLayout;
        LoadAnalysisAllTypeLayout.data = data;
        LoadAnalysisAllTypeLayout.Coordinate = Coordinate;
    }


    public void showAnalysisAllTypeLayout() {
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
        analysisView = inflater.inflate(R.layout.analysis_alltype_layout, null);
        analysisView.setLayoutParams(lp);
        linearLayout.addView(analysisView);

        //初始化布局控件
        initView(analysisView);

        //控件点击事件
        setViewOnClick();

//        填充界面的数据
        initData(data);


    }

    //        填充界面的数据
    private void initData(List<AnalysisData> data) {

        String City_gh = "";
        String xzdl = "";
        String tdlyztgh = "";
        String jsydgzq = "";
        Log.i(TAG, "initData: " + data);
        if (data == null) {
            tv_mianji.setText("2512.54平方米");
            tv_qu.setText("惠济区");
            tv_xiang.setText("迎宾路街道办");
            tv_cun.setText("木马村");
            tv_City_gh.setText("二类居住用地、防护绿地、公园绿地、河流水域");
            tv_xzdl.setText("水浇地、内陆滩涂");
            tv_tdlyztgh.setText("有林地");
            tv_jsydgzq.setText("有条件建设用地区、有条件建设用地区、有条件建设用地区、有条件建设用地区、有条件建设用地区");
        } else {
            String mianji = "";
            if (null != data.get(0).get面积().get(0).getArea()
                    && !"".equals(data.get(0).get面积().get(0).getArea())
                    && !" ".equals(data.get(0).get面积().get(0).getArea())) {
                mianji = data.get(0).get面积().get(0).getArea();
            }
            double mu = Double.valueOf(mianji) * 0.0015;
            tv_mianji.setText(String.format("%.2f", Double.parseDouble(mianji)) + "平方米" + " / " + String.format("%.2f", mu) + "亩");

            String qu = "";
            if (null != data.get(1).get区().get(0).getXZQMC()
                    && !"".equals(data.get(1).get区().get(0).getXZQMC())
                    && !" ".equals(data.get(1).get区().get(0).getXZQMC())) {
                qu = data.get(1).get区().get(0).getXZQMC();
            }
            tv_qu.setText(qu);
            String xiang = "";
            for (int i = 0; i < data.get(2).get乡().size(); i++) {
                if (null != data.get(2).get乡().get(0).getXZQMC()
                        && !"".equals(data.get(2).get乡().get(0).getXZQMC())
                        && !" ".equals(data.get(2).get乡().get(0).getXZQMC())) {
                    xiang = xiang + data.get(2).get乡().get(i).getXZQMC() + "、";
                }
            }
            if (!"".equals(xiang)) {
                xiang = xiang.substring(0, xiang.length() - 1);
            }
            tv_xiang.setText(xiang);
            String cun = "";
            for (int i = 0; i < data.get(3).get村().size(); i++) {
                if (null != data.get(3).get村().get(0).getXZQMC()
                        && !"".equals(data.get(3).get村().get(0).getXZQMC())
                        && !" ".equals(data.get(3).get村().get(0).getXZQMC())) {
                    cun = cun + data.get(3).get村().get(i).getXZQMC() + "、";
                }
            }
            if (!"".equals(cun)) {
                cun = cun.substring(0, cun.length() - 1);
            }
            tv_cun.setText(cun);
            for (int i = 0; i < data.get(4).get城市规划().size(); i++) {
                String ydmc = data.get(4).get城市规划().get(i).get用地名称();
                if (null != ydmc && !"".equals(ydmc) && !" ".equals(ydmc)) {
                    City_gh = City_gh + ydmc + "、";
                }
            }
            for (int i = 0; i < data.get(5).get现状地类().size(); i++) {
                String xzdl1 = data.get(5).get现状地类().get(i).get用地名称();
                if (null != xzdl1 && !"".equals(xzdl1) && !" ".equals(xzdl1)) {
                    xzdl = xzdl + xzdl1 + "、";
                }
            }
            for (int i = 0; i < data.get(6).get土地利用总体规划().size(); i++) {
                String tdlyghMc = data.get(6).get土地利用总体规划().get(i).get用地名称();
                if (null != tdlyghMc
                        && !"".equals(tdlyghMc)
                        && !" ".equals(tdlyghMc)) {
                    tdlyztgh = tdlyztgh + tdlyghMc + "、";
                }
            }
            for (int i = 0; i < data.get(7).get建设用地管制区().size(); i++) {
                String jsydgzqMc = data.get(7).get建设用地管制区().get(i).get用地名称();
                if (null != jsydgzqMc
                        && !"".equals(jsydgzqMc)
                        && !" ".equals(jsydgzqMc)) {
                    jsydgzq = jsydgzq + jsydgzqMc + "、";
                }
            }
            if (!"".equals(City_gh)) {
                City_gh = City_gh.substring(0, City_gh.length() - 1);
            }
            if (!"".equals(xzdl)) {
                xzdl = xzdl.substring(0, xzdl.length() - 1);
            }
            if (!"".equals(tdlyztgh)) {
                tdlyztgh = tdlyztgh.substring(0, tdlyztgh.length() - 1);
            }
            if (!"".equals(jsydgzq)) {
                jsydgzq = jsydgzq.substring(0, jsydgzq.length() - 1);
            }
            tv_City_gh.setText(City_gh);
            tv_xzdl.setText(xzdl);
            tv_tdlyztgh.setText(tdlyztgh);
            tv_jsydgzq.setText(jsydgzq);
        }

    }

    private void setViewOnClick() {
        iv_close.setOnClickListener(this);
//        ll_cun.setOnClickListener(this);
        ll_City_gh.setOnClickListener(this);
        ll_xzdl.setOnClickListener(this);
        ll_tdlyztgh.setOnClickListener(this);
        ll_jsydgzq.setOnClickListener(this);
    }


    private void initView(View analysisView) {
        iv_close = analysisView.findViewById(R.id.iv_analysistype_close);
        ll_cun = analysisView.findViewById(R.id.ll_cun);
        ll_City_gh = analysisView.findViewById(R.id.ll_City_gh);
        ll_xzdl = analysisView.findViewById(R.id.ll_xzdl);
        ll_tdlyztgh = analysisView.findViewById(R.id.ll_tdlyztgh);
        ll_jsydgzq = analysisView.findViewById(R.id.ll_jsydgzq);

        tv_mianji = analysisView.findViewById(R.id.tv_mianji);
        tv_qu = analysisView.findViewById(R.id.tv_qu);
        tv_xiang = analysisView.findViewById(R.id.tv_xiang);
        tv_cun = analysisView.findViewById(R.id.tv_cun);
        tv_City_gh = analysisView.findViewById(R.id.tv_City_gh);
        tv_xzdl = analysisView.findViewById(R.id.tv_xzdl);
        tv_tdlyztgh = analysisView.findViewById(R.id.tv_tdlyztgh);
        tv_jsydgzq = analysisView.findViewById(R.id.tv_jsydgzq);

    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        String wkt = Coordinate;
        String table = "";
        String typeResult = "";

        if (i == R.id.iv_analysistype_close) {
            linearLayout.removeView(analysisView);
            webView.loadUrl("javascript:analysis_clearLocationPolygon()");
        } else {
//            if (i == R.id.ll_cun) {
//                table = Global.TABLE_CUN;
//                typeResult = tv_cun.getText().toString();
//                name = "村";
//            }
            if (i == R.id.ll_City_gh) {
                table = Global.TABLE_CSGH;
                typeResult = tv_City_gh.getText().toString();
                name = "城市规划";
            }
            if (i == R.id.ll_xzdl) {
                table = Global.TABLE_XZDL;
                typeResult = tv_xzdl.getText().toString();
                name = "现状地类";
            }
            if (i == R.id.ll_tdlyztgh) {
                table = Global.TABLE_TDLYZTGH;
                typeResult = tv_tdlyztgh.getText().toString();
                name = "土地利用总体规划";
            }
            if (i == R.id.ll_jsydgzq) {
                table = Global.TABLE_JSYDGZQ;
                typeResult = tv_jsydgzq.getText().toString();
                name = "建设用地管制区";
            }
            getBasicDetail(wkt, table, typeResult);
        }
    }


    private void getBasicDetail(String wkt, String table, String typeResult) {
        String[] typeResultList = typeResult.split("、");
        String allTypeResult = "";
        for (int i = 0; i < typeResultList.length; i++) {
            if (i == typeResultList.length - 1) {
                allTypeResult = allTypeResult + typeResultList[i];
            } else {
                allTypeResult = allTypeResult + typeResultList[i] + ",";
            }
        }
        if (!"".equals(allTypeResult)) {
            loadingDialog = UploadDialogUtils.createLoadingDialog(context, "正在加载");
            GetBasicDetail getBasicDetail = new GetBasicDetail(webView, context);
            getBasicDetail.requestBasicDetail(wkt, table, allTypeResult, this);
        } else {
            Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void getAnalysisDetail(List<AnalysisDetailData.RowsBean> detail) {
        UploadDialogUtils.closeDialog(loadingDialog);
        LoadAnalysisDetailLayout loadAnalysisDetailLayout = new LoadAnalysisDetailLayout(context, webView, linearLayout);
        loadAnalysisDetailLayout.showAnalysisDetailLayout(name, detail);
    }

}
