package appler.com.example.module_analysis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import appler.com.example.module_analysis.freeDraw.LoadFreeDrawLayout;
import appler.com.example.module_analysis.showAllXC.LoadAnalysisAllXCLayout;


/**
 *智能分析控件类
 */

public class LoadAnalysisPopLayout {
    private static final String TAG = "LoadAnalysisPopLayout";
    private Context context;
    private WebView mWebView;
    private ImageButton ic_zhunxin;


    private LinearLayout linearLayout;

    public LoadAnalysisPopLayout(Context context, WebView mWebView, LinearLayout linearLayout, ImageButton ic_zhunxin) {
        this.context = context;
        this.mWebView = mWebView;
        this.linearLayout = linearLayout;
        this.ic_zhunxin = ic_zhunxin;
    }



    /**
     * 显示分析弹框
     * 自定义绘制或者显示全部乡村点击
     */
    @SuppressLint("NewApi")
    public void showFenxiPop(View BtnView) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = li.inflate(R.layout.analysis_select_layout, null);
        PopupWindow mMorePopupWindow = new PopupWindow(content, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mMorePopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mMorePopupWindow.setOutsideTouchable(true);
        mMorePopupWindow.setTouchable(true);
        int height = BtnView.getHeight();
        //获取pop的宽
        content.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mMorePopupWindow.setContentView(content);
        int measuredWidth = mMorePopupWindow.getContentView().getMeasuredWidth();

        //左负右正      上负下正
        int x = -(measuredWidth + 5);
        int y = -height;
        mMorePopupWindow.showAsDropDown(BtnView, x, y);


        final View parent = mMorePopupWindow.getContentView();
        final PopupWindow finalMMorePopupWindow = mMorePopupWindow;
        //自由绘制
        parent.findViewById(R.id.tv_zyhz).setOnClickListener(new View.OnClickListener() {  //绘制
            @Override
            public void onClick(View view) {
                LoadFreeDrawLayout loadFreeDrawLayout = new LoadFreeDrawLayout(context, linearLayout, mWebView, ic_zhunxin);
                loadFreeDrawLayout.showFreeDrawPop();
                finalMMorePopupWindow.dismiss();
            }
        });
        final TextView tv_xzqh = parent.findViewById(R.id.tv_xzqh);
        tv_xzqh.setOnClickListener(new View.OnClickListener() {   //二级列表搜素搜索乡村
            @Override
            public void onClick(View view) {
                finalMMorePopupWindow.dismiss();
                LoadAnalysisAllXCLayout loadAnalysisAllXCLayout = new LoadAnalysisAllXCLayout(context, mWebView, linearLayout);
                loadAnalysisAllXCLayout.showAllXCPop();
            }
        });
    }

}





