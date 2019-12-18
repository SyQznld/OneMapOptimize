package appler.com.example.module_gather.tblist;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oneMap.module.common.bean.GatherData;
import com.oneMap.module.common.dao.GatherDao;
import com.oneMap.module.common.global.ArouterConstants;
import com.oneMap.module.common.global.Global;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import appler.com.example.module_gather.R;
import appler.com.example.module_gather.TubanData;

/**
 * 图斑列表页面
 */
public class TbListActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = getClass().getSimpleName();
    private ImageView iv_back;
    private RecyclerView rv_gather;
    private WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tblist);

        initView();

        setViewOnClick();

        showGatherList();


    }

    private void initView() {
        iv_back = findViewById(R.id.iv_tblist_close);
        rv_gather = findViewById(R.id.rv_tblist);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_tblist_close){
            finish();
        }
    }

    private void setViewOnClick() {
        iv_back.setOnClickListener(this);
    }

    private void showGatherList(){
        String gahterJson = CommonUtil.readTxtFromSDCard(Global.TXT_SAVE_PATH, Global.GATHER_TXT);
        if (!"".equals(gahterJson)) {
            //有数据时
            List<GatherData> gatherDataList = new Gson().fromJson(gahterJson, new TypeToken<List<GatherData>>() {
            }.getType());
            if (null != gatherDataList) {
                rv_gather.setLayoutManager(new LinearLayoutManager(TbListActivity.this));
                gatherDataList=GatherDao.descGatherList();
                TbListAdapter tbListAdapter = new TbListAdapter(TbListActivity.this, gatherDataList);

                rv_gather.setAdapter(tbListAdapter);
                tbListAdapter.setGatherItemClickListener(new TbListAdapter.GatherItemClickListener() {
                    @Override
                    public void gatherItemClick(int position, GatherData gatherData) {
                        ARouter.getInstance()
                                .build(ArouterConstants.TO_MAINACTIVITY)
                                .withParcelable("gatherData", gatherData)
                                .navigation();
                    }
                });
            }
        }else{
            ToastUtils.showLongToast("暂无图斑");
        }
    }


    private void tubanLocation(String ogr_geometry1) {
        mWebView.loadUrl("javascript:showFW('" + ogr_geometry1 + "')");
    }
}
