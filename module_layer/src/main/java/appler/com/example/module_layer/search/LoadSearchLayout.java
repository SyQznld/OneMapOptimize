package appler.com.example.module_layer.search;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oneMap.module.common.global.Global;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.ToastUtils;

import java.io.File;

import appler.com.example.module_layer.R;

/**
 * 地名地址搜索
 */
public class LoadSearchLayout implements View.OnClickListener {
    private String TAG = getClass().getSimpleName();
    private Context context;
    private WebView webView;
    private LinearLayout linearLayout;
    private View searchView;

    private EditText et_search_input;
    private TextView tv_search_close;
    private ListView lv_search;

    public LoadSearchLayout(Context context, WebView webView, LinearLayout linearLayout) {
        this.context = context;
        this.webView = webView;
        this.linearLayout = linearLayout;
    }


    public void showSearchLayout() {
        //读取本地配置文件
//        String storagePath = CommonUtil.getStoragePath(context, true);
        String storagePath = CommonUtil.getRWPath(context);
        //一张图配置--》地名地址
        String dmdzConfigPath = storagePath + File.separator + Global.CONFIG_FLODER + File.separator + Global.CONFIG_DZDZ + ".txt";
        if (new File(dmdzConfigPath).exists()) {
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
            searchView = inflater.inflate(R.layout.search_layout, null);
            searchView.setLayoutParams(lp);
            linearLayout.addView(searchView);

            //初始化布局控件
            initView(searchView);

            //控件点击事件
            setViewOnClick();


            //填充地名地址数据
            initSearchData(dmdzConfigPath);
        } else {
            ToastUtils.showShortToast("请检查地名地址文件是否存在");
        }


    }

    private void initSearchData(String dmdzConfigPath) {

        String dmdzJson = CommonUtil.readFile(dmdzConfigPath);
        Log.i(TAG, "dmdzJson: " + dmdzJson);
        if (!"".equals(dmdzJson)) {
            Gson gson = new Gson();
            DmdzData dmdzData = gson.fromJson(dmdzJson, new TypeToken<DmdzData>() {
            }.getType());
            et_search_input.setText("");
            final DmdzSearchAdapter searchAdapter = new DmdzSearchAdapter(context, dmdzData.getFeatures());
            lv_search.setAdapter(searchAdapter);

            et_search_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchAdapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            searchAdapter.setDmdzItemClickListener(new DmdzSearchAdapter.DmdzItemClickListener() {
                @Override
                public void dmdzItemClick(String geometry, int item) {
                    String substring = geometry.substring(1, geometry.length() - 1);
                    if (substring.contains(",")){
                    String[] split = substring.split(",");
                    webView.loadUrl("javascript:mapTool.dmdzLocation('" + split[1] + "','" + split[0] + "')");
                    }
                }
            });
        }

    }

    private void setViewOnClick() {
        tv_search_close.setOnClickListener(this);
    }


    private void initView(View searchView) {
        et_search_input = searchView.findViewById(R.id.et_search_input);
        tv_search_close = searchView.findViewById(R.id.tv_search_close);
        lv_search = searchView.findViewById(R.id.lv_search);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_search_close) {
            webView.loadUrl("javascript:mapTool.removeDmdzLocationMarker()");
            linearLayout.removeView(searchView);
        }

    }
}


