package appler.com.example.module_analysis.Https;


import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oneMap.module.common.utils.MainHandler;

import appler.com.example.module_analysis.Interface.GetAnalysisDetail;
import appler.com.example.module_analysis.bean.AnalysisDetailData;

/***
 * 行政区划分析的网络请求类
 * 功能：请求
 * */

public class GetBasicDetail {
    private static final String TAG = "GetBasicDetail";
    private WebView mWebView;
    private Context context;

    public GetBasicDetail(WebView mWebView, Context context) {
        this.mWebView = mWebView;
        this.context = context;

    }

    /**
     * 智能分析详情
     */
    public void requestBasicDetail(String wkt, String table, String typeResult, GetAnalysisDetail getAnalysisDetail ) {
        Log.i(TAG, "requestBasicDetail wkt: " + wkt);
        Log.i(TAG, "requestBasicDetail table: " + table);
        Log.i(TAG, "requestBasicDetail typeResult: " + typeResult);
        PcDataRequestHttps.getInstance().requestZnfxBasicDetail(wkt, table, typeResult, new PcDataRequestHttps.ZnfxBasicDetailCallBack() {
            @Override
            public void onFailure() {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void ZnfxBasicDetailBack(final String string) {
                Log.i(TAG, "ZnfxBasicDetailBack: " + string);
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: "+string);
                        if (!"".equals(string)) {
                            if (!string.contains("<!DOCTYPE html>")) {
                                mWebView.loadUrl("javascript:analysis_measureClear()");
                                mWebView.loadUrl("javascript:loadQueryResultData('" + string + "')");
                                Gson gson = new Gson();
                                AnalysisDetailData analysisDetailData = gson.fromJson(string, new TypeToken<AnalysisDetailData>() {
                                }.getType());
                                getAnalysisDetail.getAnalysisDetail(analysisDetailData.getRows());
                            } else {
                                Toast.makeText(context, "暂无详情结果", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }
}





