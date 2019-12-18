package appler.com.example.module_gather.tbitem;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.oneMap.module.common.bean.GatherData;
import com.oneMap.module.common.global.ArouterConstants;
import com.oneMap.module.common.utils.CommonUtil;

import java.util.Arrays;
import java.util.List;

import appler.com.example.module_gather.R;
import appler.com.example.module_gather.tblist.LoadTbListLayout;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 点击单个图斑时，显示基本信息
 * 包括基本文字信息、图片列表、点击列表后在地图上显示角度
 */
public class LoadTbItemInfoLayout {
    private String TAG = getClass().getSimpleName();
    private Context context;
    private WebView webView;
    private LinearLayout linearLayout;
    private LinearLayout ll_photoList;

    private ImageView iv_gathershow_back;
    private TextView tv_gathershow_number;
    private TextView tv_gathershow_name;
    private TextView tv_gathershow_person;
    private TextView tv_gathershow_date;
    private TextView tv_gathershow_remark;
    private TextView tv_gathershow_edit;
    private RecyclerView rv_gathershow_img;
    private String polygon;
    private GatherData tbEditData;

    public LoadTbItemInfoLayout(Context context, WebView webView, LinearLayout linearLayout) {
        this.context = context;
        this.webView = webView;
        this.linearLayout = linearLayout;
    }


    public void showTbItemInfoLayout(GatherData gatherData) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        LinearLayout.LayoutParams lp;
        boolean screenChange = CommonUtil.isScreenChange();
        if (screenChange) {  //横屏
            lp = new LinearLayout.LayoutParams(width / 4, LinearLayout.LayoutParams.FILL_PARENT);
        } else {
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, WRAP_CONTENT);
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View tbItemInfoView = inflater.inflate(R.layout.tbitem_show_layout, null);
        tbItemInfoView.setLayoutParams(lp);
        linearLayout.addView(tbItemInfoView);

        //初始化布局控件
        initView(tbItemInfoView);


        //控件点击事件
        setViewOnClick(tbItemInfoView);


        //显示点击的条目的基本信息
        initGatherItemData(gatherData);


    }


    private void setViewOnClick(View tbItemInfoView) {

        iv_gathershow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("javascript:analysis_clearLocationPolygon()");
                webView.loadUrl("javascript:clearArrowMarker()");
                linearLayout.removeView(tbItemInfoView);
                LoadTbListLayout tbListLayout = new LoadTbListLayout(context, webView, linearLayout);
                tbListLayout.showTbListLayout();
            }
        });
        tv_gathershow_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("javascript:analysis_clearLocationPolygon()");
                webView.loadUrl("javascript:clearArrowMarker()");
                linearLayout.removeAllViews();
                ARouter.getInstance()
                        .build(ArouterConstants.TO_MAINACTIVITY)
                        .withParcelable("tbEditData", tbEditData)
                        .navigation();

            }
        });
    }


    private void initView(View analysisView) {
        iv_gathershow_back = analysisView.findViewById(R.id.iv_gathershow_back);
        tv_gathershow_number = analysisView.findViewById(R.id.tv_gathershow_number);
        tv_gathershow_name = analysisView.findViewById(R.id.tv_gathershow_name);
        tv_gathershow_person = analysisView.findViewById(R.id.tv_gathershow_person);
        tv_gathershow_date = analysisView.findViewById(R.id.tv_gathershow_date);
        tv_gathershow_remark = analysisView.findViewById(R.id.tv_gathershow_remark);
        rv_gathershow_img = analysisView.findViewById(R.id.rv_gathershow_img);
        tv_gathershow_edit = analysisView.findViewById(R.id.tv_gathershow_edit);
        ll_photoList = analysisView.findViewById(R.id.ll_photoList);
    }

    private void initGatherItemData(GatherData gatherData) {
        tbEditData = gatherData;
        tv_gathershow_number.setText(String.valueOf(gatherData.getId()));
        tv_gathershow_name.setText(gatherData.getGatherName());
        tv_gathershow_person.setText(gatherData.getGatherPeople());
        tv_gathershow_date.setText(gatherData.getGatherTime());
        tv_gathershow_remark.setText(gatherData.getGatherRemark());
        String photoPath = gatherData.getGatherPhotos();
        polygon = gatherData.getGatherPolygon();
        Log.i(TAG, "initGatherItemData: ====polygon======" + polygon);
//        polygon = "POLYGON ((" + polygon + "))";
        if (!polygon.startsWith("POLYGON")) {
            List<String> polygons = Arrays.asList((polygon += "," + polygon.split(",")[0]).split(","));
            polygon = "POLYGON ((" + TextUtils.join(", ", polygons) + "))";
        }
        webView.loadUrl("javascript:showFW('" + polygon + "')");
        if ("".equals(photoPath)) {
            ll_photoList.setVisibility(View.GONE);
        } else {
            List<String> photoList = Arrays.asList(photoPath.split(","));
            boolean screenChange = CommonUtil.isScreenChange();
            if (screenChange) {  //横屏
                rv_gathershow_img.setLayoutManager(new LinearLayoutManager(context));
            } else {
                rv_gathershow_img.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            }
            TbItemImgAdapter tbItemImgAdapter = new TbItemImgAdapter(webView, context, photoList);
            rv_gathershow_img.setAdapter(tbItemImgAdapter);

        }
    }
}
