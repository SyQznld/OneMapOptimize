package appler.com.example.module_gather.tblist;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oneMap.module.common.bean.GatherData;
import com.oneMap.module.common.global.Global;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.ToastUtils;

import java.util.List;

import appler.com.example.module_gather.R;
import appler.com.example.module_gather.tbitem.LoadTbItemInfoLayout;

/**
 * 图斑列表  动态布局加载
 */
public class LoadTbListLayout implements View.OnClickListener {
    private String TAG = getClass().getSimpleName();
    private Context context;
    private WebView webView;
    private LinearLayout linearLayout;

    private ImageView iv_close;
    private RecyclerView rv_gather;

    private View tbListView;

    public LoadTbListLayout(Context context, WebView webView, LinearLayout linearLayout) {
        this.context = context;
        this.webView = webView;
        this.linearLayout = linearLayout;
    }


    public void showTbListLayout() {
        initTbListData();
    }

    private void showGather() {
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
        tbListView = inflater.inflate(R.layout.activity_tblist, null);
        tbListView.setLayoutParams(lp);
        linearLayout.addView(tbListView);

        //初始化布局控件
        initView(tbListView);

        //控件点击事件
        setViewOnClick();
    }


    private void initTbListData() {
        String gahterJson = CommonUtil.readTxtFromSDCard(Global.TXT_SAVE_PATH, Global.GATHER_TXT);
        if (!"".equals(gahterJson)) {
            //有数据时
            showGather();
            Log.i(TAG, "initTbListData: "+gahterJson);
            List<GatherData> gatherDataList = new Gson().fromJson(gahterJson, new TypeToken<List<GatherData>>() {
            }.getType());
            if (null != gatherDataList) {
                rv_gather.setLayoutManager(new LinearLayoutManager(context));
//                gatherDataList=GatherDao.descGatherList();
                TbListAdapter tbListAdapter = new TbListAdapter(context, gatherDataList);
                rv_gather.setAdapter(tbListAdapter);
                tbListAdapter.setGatherItemClickListener(new TbListAdapter.GatherItemClickListener() {
                    @Override
                    public void gatherItemClick(int position, GatherData gatherData) {
                        linearLayout.removeAllViews();
                        LoadTbItemInfoLayout gatherItemLayout = new LoadTbItemInfoLayout(context, webView, linearLayout);
                        gatherItemLayout.showTbItemInfoLayout(gatherData);
                    }
                });
            }
        }else {
            ToastUtils.showLongToast("暂无图斑");
        }
    }


    private void setViewOnClick() {
        iv_close.setOnClickListener(this);

    }


    private void initView(View tbListView) {
        iv_close = tbListView.findViewById(R.id.iv_tblist_close);
        rv_gather = tbListView.findViewById(R.id.rv_tblist);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_tblist_close) {
            linearLayout.removeView(tbListView);
            webView.loadUrl("javascript:m_c()");
        }
    }
}
