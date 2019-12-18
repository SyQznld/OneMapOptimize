package appler.com.example.module_analysis.Https;

import android.util.Log;

import com.oneMap.module.common.global.Global;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PcDataRequestHttps {


    public static PcDataRequestHttps instance;
    private String TAG = getClass().getSimpleName();

    private PcDataRequestHttps() {
    }

    public static PcDataRequestHttps getInstance() {
        if (null == instance) {
            synchronized (PcDataRequestHttps.class) {
                if (null == instance) {
                    instance = new PcDataRequestHttps();
                }
            }
        }
        return instance;
    }

    public void requestZnfxBasicInfoList(String zuobiao, final AnalysisBasicListCallBack znfxBasicListCallBack) {
        Log.i(TAG, "ZnfxBasicListBack: " + zuobiao);
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(120, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("flag", "analysis_base");
        requestBody.addFormDataPart("wkt", zuobiao);

        Request request = new Request.Builder()
                .url(Global.URL)
                .post(requestBody.build())
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + e.getMessage());
                znfxBasicListCallBack.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i(TAG, "onResponse: onResponseonResponse" + string);
                if ("".equals(string) || "error".equals(string) || "Error".equals(string)) {
                    znfxBasicListCallBack.onFailure();
                } else if (string.contains("<!DOCTYPE html>")) {
                    znfxBasicListCallBack.onFailure();
                } else {
                    znfxBasicListCallBack.AnalysisBasicListBack(string);
                }
            }
        });
    }





    public void requestZnfxBasicDetail(String wkt, String table, String typeResult, final ZnfxBasicDetailCallBack znfxBasicDetailCallBack) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("flag", "analysis_details");
        requestBody.addFormDataPart("wkt", wkt);
        requestBody.addFormDataPart("table", table);
        requestBody.addFormDataPart("mingcheng", typeResult);

        Request request = new Request.Builder()
                .url(Global.URL)
                .post(requestBody.build())
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + e.getMessage());
                znfxBasicDetailCallBack.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i(TAG, "onResponse: " + string);
                if ("".equals(string) || "error".equals(string) || "Error".equals(string)) {
                    znfxBasicDetailCallBack.onFailure();
                } else {
                    znfxBasicDetailCallBack.ZnfxBasicDetailBack(string);
                }

            }
        });
    }

    public void requestHuijiXiangCunList(final HuijiXCunListBack huijiXCunListBack) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("flag", "xzqJson");

        Request request = new Request.Builder()
                .url(Global.URL)
                .post(requestBody.build())
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, " getHuijiXiangCun onFailure: " + e.getMessage());
                huijiXCunListBack.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i(TAG, "getHuijiXiangCun onResponse: " + string);
                if ("".equals(string) || "error".equals(string) || "Error".equals(string)) {
                    huijiXCunListBack.onFailure();
                } else {
                    huijiXCunListBack.getHuijiXiangCun(string);
                }

            }
        });
    }

    /**
     * 智能分析
     * 得到基本信息  各种类型
     */
    public interface AnalysisBasicListCallBack {
        void onFailure();

        void AnalysisBasicListBack(String string);
    }


    /**
     * 智能分析
     * 得到基本信息详情
     * wkt坐标值 图层表名 返回某种类型结果字符串
     */
    public interface ZnfxBasicDetailCallBack {
        void onFailure();

        void ZnfxBasicDetailBack(String string);
    }

    /**
     * 行政区划
     * 得到惠济区乡村
     */
    public interface HuijiXCunListBack {
        void onFailure();

        void getHuijiXiangCun(String string);
    }
}
