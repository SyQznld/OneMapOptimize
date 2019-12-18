package com.oneMap.module.main.moreOperation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.oneMap.module.main.R;

import java.util.List;

/**
 * 更多操作 适配器
 */
public class MoreOperationAdapter extends BaseQuickAdapter<MoreOperationData, BaseViewHolder> {

    private String TAG = getClass().getSimpleName();
    private Context context;
    private List<MoreOperationData> data;

    public MoreOperationAdapter(Context context, @Nullable List<MoreOperationData> data) {
        super(R.layout.main_more_item, data);
        this.context = context;
        this.data = data;

    }

    @Override
    protected void convert(BaseViewHolder helper, MoreOperationData item) {
        LinearLayout ll_more_item = helper.getView(R.id.ll_more_item);
        ImageView iv_more_img = helper.getView(R.id.iv_more_img);
        TextView tv_more_text = helper.getView(R.id.tv_more_text);
        int operationImg = item.getOperationImg();
        iv_more_img.setImageResource(operationImg);
        String operationText = item.getOperationText();
        tv_more_text.setText(operationText);

        ll_more_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreOperationItemClickListener.operationItemClick(helper.getLayoutPosition(), item);
            }
        });
    }


    private MoreOperationItemClickListener moreOperationItemClickListener;

    public interface MoreOperationItemClickListener {
        void operationItemClick(int position, MoreOperationData operationData);
    }

    public void setMoreOperationItemClickListener(MoreOperationItemClickListener moreOperationItemClickListener) {
        this.moreOperationItemClickListener = moreOperationItemClickListener;
    }
}
