package appler.com.example.module_analysis.showAllXC;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.MainHandler;
import com.oneMap.module.common.utils.UploadDialogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import appler.com.example.module_analysis.Https.PcDataRequestHttps;
import appler.com.example.module_analysis.R;
import appler.com.example.module_analysis.adapter.AllXiangCunAdapter;
import appler.com.example.module_analysis.adapter.SearchXCAdapter;
import appler.com.example.module_analysis.bean.AllXiangcunData;
import appler.com.example.module_analysis.bean.AnalysisData;
import appler.com.example.module_analysis.bean.SaveCheckInfoBean;
import appler.com.example.module_analysis.loadlayout.LoadAnalysisAllTypeLayout;

public class LoadAnalysisAllXCLayout {
    private static final String TAG = "LoadAnalysisAllXCLayout";
    private Context context;
    private WebView mWebView;
    private LinearLayout linearLayout;//动态加载时使用
    private Dialog loadingDialog;
    private List<AllXiangcunData> allXiangcunDataList;
    private List<AnalysisData> analysisDataList;
    private AllXiangCunAdapter allXiangCunAdapter;
    private SaveCheckInfoBean saveCheckInfoBean = new SaveCheckInfoBean();
    private String ogr_geometry;
    private boolean showAllXc = true;
    ExpandableListView elv_allxc;
    TextView et_allxc_input;
    RecyclerView rv_allxc;
    ImageView iv_huijiqu_arrow;
    LinearLayout ll_allxc_huijiqu;
    TextView tv_allxc_close;
    TextView tv_allxc_fenxi;

    public LoadAnalysisAllXCLayout(Context context, WebView mWebView, LinearLayout linearLayout) {
        this.context = context;
        this.mWebView = mWebView;
        this.linearLayout = linearLayout;
    }


    /**
     * 行政区划 显示所有乡村
     */
    public void showAllXCPop() {

        loadingDialog = UploadDialogUtils.createLoadingDialog(context, "正在加载");
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
        View popView = inflater.inflate(R.layout.analysis_allxc_layout, null);
        popView.setLayoutParams(lp);
        linearLayout.addView(popView);

        et_allxc_input = popView.findViewById(R.id.et_allxc_input);
        tv_allxc_fenxi = popView.findViewById(R.id.tv_allxc_fenxi);
        tv_allxc_close = popView.findViewById(R.id.tv_allxc_close);
        ll_allxc_huijiqu = popView.findViewById(R.id.ll_allxc_huijiqu);
        iv_huijiqu_arrow = popView.findViewById(R.id.iv_huijiqu_arrow);
        elv_allxc = popView.findViewById(R.id.elv_allxc);
        rv_allxc = popView.findViewById(R.id.rv_allxc);


        //获取所有乡村列表
        PcDataRequestHttps.getInstance().requestHuijiXiangCunList(new PcDataRequestHttps.HuijiXCunListBack() {
            @Override
            public void onFailure() {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        UploadDialogUtils.closeDialog(loadingDialog);
                        Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void getHuijiXiangCun(final String string) {
                Log.i(TAG, "getHuijiXiangCun: " + string);
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        UploadDialogUtils.closeDialog(loadingDialog);
                        if (!string.contains("<!DOCTYPE html>")) {
                            if (!"".equals(string)) {
//                                CommonUtil.SaveString(Global.TXT_SAVE_PATH+Global.ANALYSIS_CACHE,"11111");
                                Analysis_AllXCResult(string);
                            }
                        } else {
                            Toast.makeText(context, "接口错误", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });


        //控件点击事件
        et_allxc_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                List<AllXiangcunData.ChildrenBean> childrenList = new ArrayList<>();
                String input = et_allxc_input.getText().toString();

                if ("".equals(input) || null == input) {
                    elv_allxc.setVisibility(View.VISIBLE);
                    rv_allxc.setVisibility(View.GONE);
                } else {
                    elv_allxc.setVisibility(View.GONE);
                    rv_allxc.setVisibility(View.VISIBLE);
                    if (allXiangcunDataList.size() > 0) {
                        for (int i = 0; i < allXiangcunDataList.size(); i++) {
                            List<AllXiangcunData.ChildrenBean> children = allXiangcunDataList.get(i).getChildren();
                            for (int j = 0; j < children.size(); j++) {
                                String xzqmc = children.get(j).getXZQMC();
                                if (xzqmc.contains(input)) {
                                    childrenList.add(children.get(j));
                                }
                            }
                        }
                    }
                }

                rv_allxc.setLayoutManager(new LinearLayoutManager(context));
                SearchXCAdapter searchAdapter = new SearchXCAdapter(context, childrenList);
                rv_allxc.setAdapter(searchAdapter);

                searchAdapter.setSearchXCItemClickListener(new SearchXCAdapter.SearchXCItemClickListener() {
                    @Override
                    public void clickSearchXC(int position, AllXiangcunData.ChildrenBean dataBean) {
                        ogr_geometry = dataBean.getOgr_geometry();
                    }
                });

            }

        });

        tv_allxc_fenxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wkt = "";
                //MULTIPOLYGON空格(POLYGON,空格POLYGON,空格POLYGON)
                int size = saveCheckInfoBean.wktList.size();
                if (size > 1) {
                    for (int i = 0; i < size; i++) {
                        String s1 = saveCheckInfoBean.wktList.get(i);
                        String s11 = s1.substring(8);
                        if ("".equals(wkt)) {
                            wkt = s11;
                        } else {
                            wkt = wkt + ", " + s11;
                        }
                    }
                    wkt = "MULTIPOLYGON (" + wkt + ")";
                    ogr_geometry = wkt;
                } else {
                    ogr_geometry = saveCheckInfoBean.wktList.get(0);
                }
                //点击分析后显示选择的区域
                tubanLocation(ogr_geometry);

                //智能分析
                loadingDialog = UploadDialogUtils.createLoadingDialog(context, "正在加载");
                if (!"".equals(ogr_geometry)) {
                    requestFxBasicInfo(ogr_geometry);
                } else {
                    Toast.makeText(context, "请选择分析数据", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_allxc_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeAllViews();
            }
        });
        ll_allxc_huijiqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showAllXc == false) {
                    elv_allxc.setVisibility(View.VISIBLE);
                    showAllXc = true;
                    iv_huijiqu_arrow.setImageResource(R.drawable.ic_row_up);
                    ogr_geometry = "";
                } else {
                    elv_allxc.setVisibility(View.GONE);
                    showAllXc = false;
                    iv_huijiqu_arrow.setImageResource(R.drawable.ic_row_down);
                }
            }
        });


    }


    /**
     * 智能分析
     * 点击某一类型后 显示详情绘制出范围
     */
    private void tubanLocation(String ogr_geometry) {
        mWebView.loadUrl("javascript:showFW('" + ogr_geometry + "')");
    }


    /**
     * 智能分析 请求接口数据
     */
    private void requestFxBasicInfo(String Coordinate) {
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

            // 行政区划返回数据接口
            @Override
            public void AnalysisBasicListBack(final String string) {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        analysisDataList = gson.fromJson(string, new TypeToken<List<AnalysisData>>() {
                        }.getType());
                        LoadAnalysisAllTypeLayout allTypeLayout = new LoadAnalysisAllTypeLayout(context, mWebView, linearLayout, analysisDataList, Coordinate);
                        allTypeLayout.showAnalysisAllTypeLayout();
                        UploadDialogUtils.closeDialog(loadingDialog);

                    }
                });
            }
        });

    }

    private void Analysis_AllXCResult(String result) {
        Gson gson = new Gson();
        allXiangcunDataList = gson.fromJson(result, new TypeToken<List<AllXiangcunData>>() {
        }.getType());
        allXiangCunAdapter = new AllXiangCunAdapter(context, allXiangcunDataList, saveCheckInfoBean);
        elv_allxc.setAdapter(allXiangCunAdapter);


        //下次打开二级列表时 显示打开已选中条目并展开分组
        int size = saveCheckInfoBean.xzqhCheckedMap.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                Map<Integer, Boolean> map = saveCheckInfoBean.xzqhCheckedMap.get(i);
                for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {
                    if (entry.getValue()) {
                        elv_allxc.expandGroup(i);
                    }
                }
            }
        }

        elv_allxc.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long l) {
                AllXiangcunData allXiangcunData = allXiangcunDataList.get(position);
                ogr_geometry = allXiangcunData.getOgr_geometry();
                return false;
            }
        });

        allXiangCunAdapter.setCunItemClickListener(new AllXiangCunAdapter.CunItemClickListener() {
            @Override
            public void clickCun(int position, AllXiangcunData.ChildrenBean data) {
                ogr_geometry = data.getOgr_geometry();
                Toast.makeText(context, data.getXZQMC(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
