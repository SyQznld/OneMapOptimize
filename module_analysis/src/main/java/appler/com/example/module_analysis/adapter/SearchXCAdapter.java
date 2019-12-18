package appler.com.example.module_analysis.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import appler.com.example.module_analysis.R;
import appler.com.example.module_analysis.bean.AllXiangcunData;

/**
 * 模糊匹配搜索出来的乡村
 */

public class SearchXCAdapter extends BaseQuickAdapter<AllXiangcunData.ChildrenBean, BaseViewHolder> {
    private Context context;
    private List<AllXiangcunData.ChildrenBean> data;
    private SearchXCItemClickListener searchXCItemClickListener;

    public SearchXCAdapter(Context context, @Nullable List<AllXiangcunData.ChildrenBean> data) {
        super(R.layout.analysis_item_group, data);
        this.context = context;
        this.data = data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AllXiangcunData.ChildrenBean item) {
        String xzqmc = item.getXZQMC();
        TextView tv_name = helper.getView(R.id.tv_group);
        LinearLayout ll_name = helper.getView(R.id.ll_group);

        tv_name.setText(xzqmc);
        ll_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchXCItemClickListener.clickSearchXC(helper.getLayoutPosition(), item);
            }
        });

    }

    public void setSearchXCItemClickListener(SearchXCItemClickListener searchXCItemClickListener) {
        this.searchXCItemClickListener = searchXCItemClickListener;
    }

    public interface SearchXCItemClickListener {
        void clickSearchXC(int position, AllXiangcunData.ChildrenBean dataBean);
    }
}
