package com.oneMap.module.main.moreOperation;


import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.oneMap.module.common.global.ArouterConstants;
import com.oneMap.module.common.global.Global;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.main.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import appler.com.example.module_gather.tblist.LoadTbListLayout;
import appler.com.example.module_tbtrack.LoadBZListLayout;

/**
 * 主页更多操作 弹框显示
 */
public class ShowMoreOperationPop extends PopupWindow {

    private String TAG = getClass().getSimpleName();
    private Activity context;
    private TextView textView;
    private LinearLayout linearLayout;
    private WebView webView;

    private View contentView;
    private PopupWindow popWindow;
    private ImageButton ib_zhunxin;

    public ShowMoreOperationPop(Activity context, TextView textView, LinearLayout linearLayout, WebView webView, ImageButton ib_zhunxin) {
        super(context);
        this.context = context;
        this.textView = textView;
        this.linearLayout = linearLayout;
        this.webView = webView;
        this.ib_zhunxin = ib_zhunxin;

        contentView = LayoutInflater.from(context).inflate(R.layout.main_more_pop, null);
        popWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    /**
     * 主页更多操作 弹框显示
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void showPop(TextView tv_more) {
        RecyclerView rv_main_more = contentView.findViewById(R.id.rv_main_more);
        rv_main_more.setLayoutManager(new GridLayoutManager(context, 3));
        List<MoreOperationData> dataList = new ArrayList<>();
//        dataList.add(new MoreOperationData(R.drawable.ic_pointloft, "放样"));
        dataList.add(new MoreOperationData(R.drawable.ic_biaozhu, "标注"));
        dataList.add(new MoreOperationData(R.drawable.ic_tblist, "图斑"));
        dataList.add(new MoreOperationData(R.drawable.ic_setting, "设置"));
        dataList.add(new MoreOperationData(R.drawable.ic_map_jia, "放大"));
        dataList.add(new MoreOperationData(R.drawable.ic_map_jian, "缩小"));
        //判断简要介绍文档是否存在，不存在，隐藏说明按钮
        String docDecPath = CommonUtil.getRWPath(context) + File.separator + Global.CONFIG_DOCDESCRIBE + ".docx";
        if (new File(docDecPath).exists()) {
            dataList.add(new MoreOperationData(R.drawable.ic_docdes, "手册"));
        }

//        dataList.add(new MoreOperationData(R.drawable.ic_setting, "设置"));
        MoreOperationAdapter moreOperationAdapter = new MoreOperationAdapter(context, dataList);
        rv_main_more.setAdapter(moreOperationAdapter);
        moreOperationAdapter.setMoreOperationItemClickListener(new MoreOperationAdapter.MoreOperationItemClickListener() {
            @Override
            public void operationItemClick(int position, MoreOperationData operationData) {
                String operationText = operationData.getOperationText();
                switch (operationText){
                    case "放样":
                        ib_zhunxin.setVisibility(View.GONE);
                        break;
                    case "设置":
                        ib_zhunxin.setVisibility(View.GONE);
                        webView.loadUrl("javascript:measureClear()");
                        webView.loadUrl("javascript:removeSelectedPolygon()");
                        linearLayout.removeAllViews();
                        ARouter.getInstance().build(ArouterConstants.TO_SETTINGACTIVITY).navigation();
                        break;
                    case "标注":
                        linearLayout.removeAllViews();
                        ib_zhunxin.setVisibility(View.GONE);
                        LoadBZListLayout bzListLayout = new LoadBZListLayout(context, linearLayout, webView);
                        bzListLayout.showTbListLayout();
                        popWindow.dismiss();
                        break;
                    case "图斑":
                        ib_zhunxin.setVisibility(View.GONE);
                        webView.loadUrl("javascript:measureClear()");
                        webView.loadUrl("javascript:removeSelectedPolygon()");
                        linearLayout.removeAllViews();
                        LoadTbListLayout tbListLayout = new LoadTbListLayout(context, webView, linearLayout);
                        tbListLayout.showTbListLayout();
                        popWindow.dismiss();
                        break;
                    case "手册":
                        ib_zhunxin.setVisibility(View.GONE);
                        webView.loadUrl("javascript:measureClear()");
                        webView.loadUrl("javascript:removeSelectedPolygon()");
                        linearLayout.removeAllViews();
                        String docPath = CommonUtil.getRWPath(context) + File.separator + Global.CONFIG_DOCDESCRIBE + ".docx";
                        if (new File(docPath).exists()) {
                            CommonUtil.openFile(new File(docPath), context);
                        }
                        break;
                    case "放大":
                        webView.loadUrl("javascript:mapTool.fangda()");
                        break;
                    case "缩小":
                        webView.loadUrl("javascript:mapTool.suoxiao()");
                        break;
                }
            }
        });

        int popupHeight = popWindow.getHeight();
        int height = tv_more.getHeight();
        //获取pop的宽
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popWindow.setContentView(contentView);
        int measuredWidth = popWindow.getContentView().getMeasuredWidth();

        //左负右正      上负下正
        int x = -(measuredWidth + 5);
        int y = -((popupHeight / 2) + (height));

        popWindow.setAnimationStyle(R.style.MainMorePop);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setOutsideTouchable(true);
        popWindow.showAsDropDown(tv_more, x, y);
    }
}
