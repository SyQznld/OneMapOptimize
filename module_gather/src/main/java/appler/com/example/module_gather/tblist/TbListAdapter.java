package appler.com.example.module_gather.tblist;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.oneMap.module.common.bean.GatherData;

import java.util.List;

import appler.com.example.module_gather.R;

/**
 * 采集的图斑列表 适配器
 */
public class TbListAdapter extends BaseQuickAdapter<GatherData, BaseViewHolder> {
    private String TAG = getClass().getSimpleName();
    private Context context;
    private List<GatherData> data;

    public TbListAdapter(Context context, @Nullable List<GatherData> data) {
        super(R.layout.tb_item_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GatherData item) {
        CardView cv_gather = helper.getView(R.id.cv_gather);
        TextView tv_gather_number = helper.getView(R.id.tv_gather_number);
        TextView tv_gather_name = helper.getView(R.id.tv_gather_name);
        TextView tv_gather_person = helper.getView(R.id.tv_gather_person);
        TextView tv_gather_date = helper.getView(R.id.tv_gather_date);
        tv_gather_number.setText(String.valueOf(item.getId()));
        tv_gather_name.setText(item.getGatherName());
        tv_gather_person.setText(item.getGatherPeople());
        tv_gather_date.setText(item.getGatherTime());

        cv_gather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatherItemClickListener.gatherItemClick(helper.getLayoutPosition(),item);
            }
        });
    }



    private GatherItemClickListener gatherItemClickListener;

    public void setGatherItemClickListener(GatherItemClickListener gatherItemClickListener) {
        this.gatherItemClickListener = gatherItemClickListener;
    }

    public  interface GatherItemClickListener{
        void  gatherItemClick(int position, GatherData gatherData);
    }
}
