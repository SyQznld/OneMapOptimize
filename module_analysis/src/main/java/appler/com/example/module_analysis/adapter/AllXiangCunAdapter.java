package appler.com.example.module_analysis.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appler.com.example.module_analysis.R;
import appler.com.example.module_analysis.bean.AllXiangcunData;
import appler.com.example.module_analysis.bean.SaveCheckInfoBean;


/**
 * 智能分析的行政区划模块适配器
 */

public class AllXiangCunAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<AllXiangcunData> modelLayerDataList;

    private SaveCheckInfoBean saveCheckInfoBean;
    private XiangItemClickListener xiangItemClickListener;
    private CunItemClickListener cunItemClickListener;


    public AllXiangCunAdapter() {

    }

    /**
     * context：调用部分的上下文
     * modelLayerDataList：填充的数据源
     * saveCheckInfoBean：保存选中状态的对象
     */
    public AllXiangCunAdapter(Context context, List<AllXiangcunData> modelLayerDataList, SaveCheckInfoBean saveCheckInfoBean) {
        this.context = context;
        this.modelLayerDataList = modelLayerDataList;
        this.saveCheckInfoBean = saveCheckInfoBean;
        layoutInflater = LayoutInflater.from(context);

        Log.i("    AllXiangCunAdapter", "AllXiangCunAdapter: " + saveCheckInfoBean.xzqhCheckedMap.size());
        //默认选中第一个
        if (saveCheckInfoBean.xzqhCheckedMap.size() == 0) {
            for (int i = 0; i < modelLayerDataList.size(); i++) {
                Map<Integer, Boolean> map = new HashMap<>();
                for (int j = 0; j < modelLayerDataList.get(i).getChildren().size(); j++) {
                    map.put(j, false);
                }
                saveCheckInfoBean.xzqhCheckedMap.add(i, map);
            }

        }
    }

    @Override
    public int getGroupCount() {
        return modelLayerDataList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return modelLayerDataList.get(i).getChildren().size();
    }

    @Override
    public Object getGroup(int i) {
        return modelLayerDataList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return modelLayerDataList.get(i).getChildren().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    private List<GroupViewHolder> groupViewHolderList = new ArrayList<>();

    /**
     * 一级目录
     */
    @Override
    public View getGroupView(final int groupPosition, boolean b, View view, ViewGroup viewGroup) {

        GroupViewHolder groupViewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.analysis_item_group, viewGroup, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tv_groupname = view.findViewById(R.id.tv_group);
            groupViewHolder.cb_group = view.findViewById(R.id.cb_group);
            groupViewHolder.ll_group = view.findViewById(R.id.ll_group);

            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        groupViewHolderList.add(groupPosition, groupViewHolder);
        final AllXiangcunData allXiangcunData = modelLayerDataList.get(groupPosition);

        List<AllXiangcunData.ChildrenBean> cunDataList = allXiangcunData.getChildren();

        groupViewHolder.tv_groupname.setText(allXiangcunData.getXZQMC());
        groupViewHolder.cb_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupViewHolder.cb_group.isChecked()) {
                    for (int i = 0; i < modelLayerDataList.get(groupPosition).getChildren().size(); i++) {
                        saveCheckInfoBean.xzqhCheckedMap.get(groupPosition).put(i, true);
                        saveCheckInfoBean.wktList.add(modelLayerDataList.get(groupPosition).getChildren().get(i).getOgr_geometry());
                        if (groupViewHolderList.get(groupPosition).childViewHolderList.size() > i) {
                            groupViewHolderList.get(groupPosition).childViewHolderList.get(i).cb_child.setChecked(true);
                        }

                    }
                } else {
                    for (int i = 0; i < modelLayerDataList.get(groupPosition).getChildren().size(); i++) {
                        saveCheckInfoBean.xzqhCheckedMap.get(groupPosition).put(i, false);
                        saveCheckInfoBean.wktList.remove(modelLayerDataList.get(groupPosition).getChildren().get(i).getOgr_geometry());
                        if (groupViewHolderList.get(groupPosition).childViewHolderList.size() > i) {
                            groupViewHolderList.get(groupPosition).childViewHolderList.get(i).cb_child.setChecked(false);
                        }

                    }
                }

            }
        });
        notifyDataSetChanged();

        return view;
    }

    /**
     * 二级目录
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean b, View view, ViewGroup viewGroup) {
        final ChildViewHolder childViewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.analysis_item_child, viewGroup, false);

            childViewHolder = new ChildViewHolder();
            childViewHolder.cb_child = view.findViewById(R.id.cb_child);
            childViewHolder.tv_child = view.findViewById(R.id.tv_child);
            childViewHolder.ll_child = view.findViewById(R.id.ll_child);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }

        AllXiangcunData allXiangcunData = modelLayerDataList.get(groupPosition);
        List<AllXiangcunData.ChildrenBean> cunDataList = allXiangcunData.getChildren();

        final AllXiangcunData.ChildrenBean childrenBean = cunDataList.get(childPosition);
        String xzqmc = childrenBean.getXZQMC();
        childViewHolder.tv_child.setText(xzqmc);
        groupViewHolderList.get(groupPosition).childViewHolderList.add(childPosition, childViewHolder);
        childViewHolder.cb_child.setOnCheckedChangeListener(null);
        childViewHolder.cb_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (childViewHolder.cb_child.isChecked()) {
                    saveCheckInfoBean.xzqhCheckedMap.get(groupPosition).put(childPosition, true);
                    saveCheckInfoBean.wktList.add(childrenBean.getOgr_geometry());
                } else {
                    //取消选中状态 false
                    saveCheckInfoBean.xzqhCheckedMap.get(groupPosition).put(childPosition, false);
                    saveCheckInfoBean.wktList.remove(childrenBean.getOgr_geometry());
                }

                notifyDataSetChanged();
            }
        });

        //判断是否选中状态
        Map<Integer, Boolean> map = saveCheckInfoBean.xzqhCheckedMap.get(groupPosition);
        if (null != map && map.size() > 0) {
            Boolean aBoolean = map.get(childPosition);
            if (aBoolean) {
                //选中状态 置顶可见
                childViewHolder.cb_child.setChecked(true);
            } else {
                //未选中 置顶不可见
                childViewHolder.cb_child.setChecked(false);
            }
        }
        return view;
    }

    public void setXiangItemClickListener(XiangItemClickListener xiangItemClickListener) {
        this.xiangItemClickListener = xiangItemClickListener;
    }

    public void setCunItemClickListener(CunItemClickListener cunItemClickListener) {
        this.cunItemClickListener = cunItemClickListener;
    }

    public interface XiangItemClickListener {
        void clickXiang(int position, AllXiangcunData data);
    }

    public interface CunItemClickListener {
        void clickCun(int position, AllXiangcunData.ChildrenBean data);
    }

    class GroupViewHolder {
        TextView tv_groupname;
        CheckBox cb_group;
        LinearLayout ll_group;

        List<ChildViewHolder> childViewHolderList = new ArrayList<>();
    }

    class ChildViewHolder {
        CheckBox cb_child;
        TextView tv_child;
        LinearLayout ll_child;
    }


}
