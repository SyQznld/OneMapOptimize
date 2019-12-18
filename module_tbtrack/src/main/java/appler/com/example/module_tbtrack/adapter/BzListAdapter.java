package appler.com.example.module_tbtrack.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneMap.module.common.bean.BzData;

import java.util.ArrayList;
import java.util.List;

import appler.com.example.module_tbtrack.R;

public class BzListAdapter extends BaseAdapter implements Filterable {
    private String TAG = getClass().getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private List<BzData> datas;

    private ArrayFilter mFilter;

    private final Object mLock = new Object();
    private ArrayList<BzData> mOriginalValues;


    public BzListAdapter(Context context, List<BzData> datas) {
        this.context = context;
        this.datas = datas;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final BzData bzData = datas.get(position);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.bz_list_item, null);
            holder.iv_type = view.findViewById(R.id.iv_tuban_type);
            holder.tv_position = view.findViewById(R.id.tv_tuban_position);
            holder.tv_name = view.findViewById(R.id.tv_tuban_name);
            holder.tv_chakan = view.findViewById(R.id.tv_tuban_chakan);
            holder.tv_delete = view.findViewById(R.id.tv_tuban_delete);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String type = bzData.getBzType();
        if ("面".equals(type)) {
            holder.iv_type.setImageResource(R.drawable.ic_polygon);
        } else {
            holder.iv_type.setImageResource(R.drawable.ic_line);
        }
        holder.tv_position.setText(String.valueOf(position + 1));
        holder.tv_name.setText(bzData.getBzName());

        holder.tv_chakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bzItemClickListener.bzItemClick(bzData, position);
            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bzItemClickListener.deleteBz(bzData, position);
            }
        });


        return view;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    class ViewHolder {

        TextView tv_position;
        TextView tv_name;
        TextView tv_chakan;
        TextView tv_delete;
        ImageView iv_type;

    }

    public interface BzItemClickListener {
        void bzItemClick(BzData bzData, int position);

        void deleteBz(BzData bzData, int position);
    }

    private BzItemClickListener bzItemClickListener;

    public void setBzItemClickListener(BzItemClickListener bzItemClickListener) {
        this.bzItemClickListener = bzItemClickListener;
    }

    private class ArrayFilter extends Filter {
        //执行刷选
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();//过滤的结果
            //原始数据备份为空时，上锁，同步复制原始数据
            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(datas);
                }
            }
            //当首字母为空时
            if (prefix == null || prefix.length() == 0) {
                ArrayList<BzData> list;
                synchronized (mLock) {//同步复制一个原始备份数据
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();//此时返回的results就是原始的数据，不进行过滤
            } else {
                String prefixString = prefix.toString().toLowerCase();//转化为小写

                ArrayList<BzData> values;
                synchronized (mLock) {//同步复制一个原始备份数据
                    values = new ArrayList<>(mOriginalValues);
                }
                final int count = values.size();
                final List<BzData> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final BzData value = values.get(i);//从List<User>中拿到User对象
//                    final String valueText = value.toString().toLowerCase();
                    final String valueText = value.getBzName().toLowerCase();//User对象的name属性作为过滤的参数
//                    final String valueid = value.getProperties().getId().toLowerCase();//User对象的name属性作为过滤的参数
                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString) || valueText.indexOf(prefixString.toString()) != -1
//                            || valueid.startsWith(prefixString) || valueid.indexOf(prefixString.toString()) != -1
                    ) {//第一个字符是否匹配
                        newValues.add(value);//将这个item加入到数组对象中
                    } else {//处理首字符是空格
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {//一旦找到匹配的就break，跳出for循环
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;//此时的results就是过滤后的List<User>数组
                results.count = newValues.size();
            }
            return results;
        }

        //刷选结果
        @Override
        protected void publishResults(CharSequence prefix, FilterResults results) {
            //noinspection unchecked
            datas = (List<BzData>) results.values;//此时，Adapter数据源就是过滤后的Results
            if (results.count > 0) {
                notifyDataSetChanged();//这个相当于从mDatas中删除了一些数据，只是数据的变化，故使用notifyDataSetChanged()
            } else {
                /**
                 * 数据容器变化 ----> notifyDataSetInValidated

                 容器中的数据变化  ---->  notifyDataSetChanged
                 */
                notifyDataSetInvalidated();//当results.count<=0时，此时数据源就是重新new出来的，说明原始的数据源已经失效了
            }
        }
    }
}
