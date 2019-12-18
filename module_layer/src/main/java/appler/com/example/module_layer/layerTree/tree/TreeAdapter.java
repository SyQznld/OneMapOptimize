package appler.com.example.module_layer.layerTree.tree;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneMap.module.common.bean.EventBusData;
import com.oneMap.module.common.global.Global;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.MainHandler;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import appler.com.example.module_layer.LayerConstants;
import appler.com.example.module_layer.R;


/**
 * Created by  on 2018/11/26.
 */
public class TreeAdapter extends RecyclerView.Adapter<TreeAdapter.ViewHolder> implements TreeStateChangeListener {
    private final static int ITEM_STATE_CLOSE = 0;
    private final static int ITEM_STATE_OPEN = 1;
    private Context mContext;
    private List<TreeItem> mList;
    private int layout_width;
    private static TextView beforeTextView;
    private static TreeItem beforeTreeItem;
    private WebView webView;
    private Map<String, Boolean> layerCheck = new HashMap<>();
    private int Reb = Color.rgb(178, 34, 34);
    private int Black = Color.rgb(0, 0, 0);
    private ShpJson ShpJson = new ShpJson();

    public TreeAdapter(Context context, List<TreeItem> list, WebView webView) {
        initList(list, 0);
        this.mList = new LinkedList<>();
        this.mContext = context;
        this.mList.addAll(list);
        this.webView = webView;
    }

    private void initList(List<TreeItem> list, int level) {
        if (list == null || list.size() <= 0) return;
        for (TreeItem item : list) {
            item.itemLevel = level;
            if (item.layerItemData != null && item.layerItemData.size() > 0) {
                initList(item.layerItemData, level + 1);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        layout_width = viewGroup.getWidth();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_tree, viewGroup, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        //一张图配置--》点查json--》txt
        String storagePath = CommonUtil.getRWPath(mContext);
        String propertyTxtpath = storagePath + File.separator + Global.CONFIG_FLODER + File.separator + Global.CONFIG_PROPERTY_FOLDER + File.separator;
        final TreeItem treeItem = mList.get(position);
        if (layout_width != 0) {
            int width = 30 * treeItem.itemLevel;
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(layout_width, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = width;
            viewHolder.item_parents.setLayoutParams(layoutParams);
            float textSize = 18 - 2 * treeItem.itemLevel;
            viewHolder.mTextView.setTextSize(textSize);
        }
        viewHolder.mTextView.setText(treeItem.Layer_name);
        viewHolder.cb_layer.setChecked(treeItem.check);
        if (treeItem.isCheck()) {
            viewHolder.tv_zhiding.setTextColor(treeItem.getZhiDing_RGB());
            if (Reb == treeItem.getZhiDing_RGB()) {
                beforeTextView = viewHolder.tv_zhiding;
                beforeTreeItem = treeItem;
                webView.loadUrl("javascript:setLayerTop('" + treeItem.getLayer_name() + "')");
            }
            viewHolder.tv_zhiding.setVisibility(View.VISIBLE);
//            viewHolder.layer_color.setVisibility(View.VISIBLE);
            viewHolder.layer_color.setVisibility(View.GONE);
        } else {
            viewHolder.tv_zhiding.setVisibility(View.GONE);
//            viewHolder.layer_color.setVisibility(View.INVISIBLE);
            viewHolder.layer_color.setVisibility(View.GONE);
        }


        if (treeItem.layerItemData != null && treeItem.layerItemData.size() > 0) {
            viewHolder.tvState.setVisibility(View.VISIBLE);
            viewHolder.tv_zhiding.setVisibility(View.GONE);
            viewHolder.cb_layer.setClickable(false);
            viewHolder.layer_color.setVisibility(View.GONE);
            if (treeItem.itemState == ITEM_STATE_OPEN) {
                viewHolder.tvState.setText("-");
            } else {
                viewHolder.tvState.setText("+");
            }
        } else {
            viewHolder.tvState.setVisibility(View.INVISIBLE);
            viewHolder.cb_layer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = ((CheckBox) v).isChecked();
                    layerCheck.put(treeItem.getLayer_name(), isChecked);
                    treeItem.setCheck(isChecked);
                    //拼接封装shp json  包括路径、名称、显示颜色等true
                    String shpJson = ShpJson.setShpJson(treeItem.getIsDiancha(), propertyTxtpath, treeItem.getLayer_name(),
                            treeItem.getLayer_SDName(), treeItem.getShpColor(), treeItem.getShpBZKey(), isChecked,treeItem.getLayerViewCenter());
                    Log.i("", "chro  onClick: " + shpJson);
                    webView.loadUrl("javascript:loadLayers('" + treeItem.getLayer_Type() + "'," + shpJson + ")");
                    if (isChecked) {
                        String layer_type = treeItem.getLayer_Type();
                        if (LayerConstants.LAYER_TYPE_SHP.equals(layer_type)) {   //矢量 显示色盘选择
//                            viewHolder.layer_color.setVisibility(View.VISIBLE);
                            viewHolder.layer_color.setVisibility(View.GONE);
                        }
                        viewHolder.tv_zhiding.setVisibility(View.VISIBLE);
                        setTopLayer(viewHolder.tv_zhiding, treeItem, null);
                    } else {
//                        viewHolder.layer_color.setVisibility(View.INVISIBLE);
                        viewHolder.layer_color.setVisibility(View.GONE);
                        viewHolder.tv_zhiding.setVisibility(View.GONE);
                    }
                }
            });
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (treeItem.itemState == ITEM_STATE_CLOSE) {
                    onOpen(treeItem, viewHolder.getAdapterPosition());
                } else {
                    onClose(treeItem, viewHolder.getAdapterPosition());
                }
            }
        });

        viewHolder.tv_zhiding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTopLayer(viewHolder.tv_zhiding, treeItem, v);
            }
        });
    }

    private void setTopLayer(TextView tv_zhiding, TreeItem treeItem, View view) {
        if (null == beforeTextView) {
            tv_zhiding.setTextColor(Reb);
            treeItem.setZhiDing_RGB(Reb);
            beforeTextView = tv_zhiding;
            beforeTreeItem = treeItem;
        } else if (null != view && view == beforeTextView) {
            beforeTextView.setTextColor(Reb);
            treeItem.setZhiDing_RGB(Reb);
            beforeTreeItem = treeItem;
        } else if (null == view && beforeTextView == tv_zhiding) {
            beforeTextView.setTextColor(Reb);
            beforeTreeItem.setZhiDing_RGB(Reb);
            beforeTextView = tv_zhiding;
            beforeTreeItem = treeItem;
        } else {
            tv_zhiding.setTextColor(Reb);
            treeItem.setZhiDing_RGB(Reb);
            beforeTextView.setTextColor(Black);
            beforeTreeItem.setZhiDing_RGB(Black);
            beforeTextView = tv_zhiding;
            beforeTreeItem = treeItem;
        }
        webView.loadUrl("javascript:setLayerTop('" + treeItem.getLayer_name() + "')");
        MainHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                EventBusData event = new EventBusData("TopLayer");
                event.setTopLayer(treeItem.getLayer_name());
                EventBus.getDefault().post(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onOpen(TreeItem treeItem, int position) {
        if (treeItem.layerItemData != null && treeItem.layerItemData.size() > 0) {
            for (int i = 0; i < treeItem.layerItemData.size(); i++) {
                if (layerCheck.containsKey(treeItem.layerItemData.get(i).getLayer_name())) {
                    treeItem.layerItemData.get(i).setCheck(layerCheck.get(treeItem.layerItemData.get(i).getLayer_name()));
                }
            }
            mList.addAll(position + 1, treeItem.layerItemData);
            treeItem.itemState = ITEM_STATE_OPEN;
            notifyItemRangeInserted(position + 1, treeItem.layerItemData.size());
            notifyItemChanged(position);
        }
    }

    @Override
    public void onClose(TreeItem treeItem, int position) {
        if (treeItem.layerItemData != null && treeItem.layerItemData.size() > 0) {
            int nextSameOrHigherLevelNodePosition = mList.size() - 1;
            if (mList.size() > position + 1) {
                for (int i = position + 1; i < mList.size(); i++) {
                    if (mList.get(i).itemLevel <= mList.get(position).itemLevel) {
                        nextSameOrHigherLevelNodePosition = i - 1;
                        break;
                    }
                }
                closeChild(mList.get(position));
                if (nextSameOrHigherLevelNodePosition > position) {
                    mList.subList(position + 1, nextSameOrHigherLevelNodePosition + 1).clear();
                    treeItem.itemState = ITEM_STATE_CLOSE;
                    notifyItemRangeRemoved(position + 1, nextSameOrHigherLevelNodePosition - position);
                    notifyItemChanged(position);
                }
            }
        }
    }

    private void closeChild(TreeItem treeItem) {
        if (treeItem.layerItemData != null) {
            for (TreeItem child : treeItem.layerItemData) {
                child.itemState = ITEM_STATE_CLOSE;
                closeChild(child);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb_layer;
        TextView tvState;
        TextView mTextView;
        ImageView layer_color;
        TextView tv_zhiding;
        LinearLayout item_parents;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cb_layer = itemView.findViewById(R.id.cb_layer);
            tvState = itemView.findViewById(R.id.tvState);
            mTextView = itemView.findViewById(R.id.tv_Name);
            layer_color = itemView.findViewById(R.id.iv_layer_color);
            item_parents = itemView.findViewById(R.id.item_parents);
            tv_zhiding = itemView.findViewById(R.id.tv_zhiding);
        }
    }
}
