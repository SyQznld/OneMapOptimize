package appler.com.example.module_analysis.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import appler.com.example.module_analysis.R;
import appler.com.example.module_analysis.bean.AnalysisDetailData;

/**
 * 智能分析 adapter
 */

public class AnalysisResultAdapter extends BaseQuickAdapter<AnalysisDetailData.RowsBean, BaseViewHolder> {
    private Context context;
    private List<AnalysisDetailData.RowsBean> data;
    private WebView webView;
    public AnalysisResultAdapter(Context context, @Nullable List<AnalysisDetailData.RowsBean> data, WebView webView) {
        super(R.layout.analysis_resultlist_item, data);
        this.context = context;
        this.data = data;
        this.webView=webView;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AnalysisDetailData.RowsBean item) {
        TextView basic_detail_position = helper.getView(R.id.tv_analysisresult_position);
        TextView tv_analysisresult_label = helper.getView(R.id.tv_analysisresult_label);
        TextView tv_analysisresult_value = helper.getView(R.id.tv_analysisresult_value);
        LinearLayout ll_analysisresult=helper.getView(R.id.ll_analysisresult);
        basic_detail_position.setText(String.valueOf(helper.getLayoutPosition() + 1));
        tv_analysisresult_label.setText(item.get用地名称());
        String area = String.format("%.2f", Double.parseDouble(item.getReArea()) * 0.0015);
        tv_analysisresult_value.setText(area + "亩");

        ll_analysisresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:showFW('" + item.getOgr_geometry() + "')");
            }
        });

    }



}
