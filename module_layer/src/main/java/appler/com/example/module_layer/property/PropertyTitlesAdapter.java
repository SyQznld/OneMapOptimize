package appler.com.example.module_layer.property;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import appler.com.example.module_layer.R;

public class PropertyTitlesAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<String> datas;
    private String title;

    private int selectedItem = -1;


    public PropertyTitlesAdapter(List<String> datas, Context context, String title) {
        this.datas = datas;
        this.title = title;
        inflater = LayoutInflater.from(context);
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    @Override
    public int getCount() {
        return datas.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.property_list_title, null);
            holder.tv_title = convertView.findViewById(R.id.list_tilename);
            holder.rl_titlename = convertView.findViewById(R.id.rl_titlename);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if ("".equals(title)) {
            holder.tv_title.setText(datas.get(position));
            if (selectedItem == position) {
                setSelectedTitle(holder, "#eeeeee", true, Typeface.BOLD);
            } else {
                setSelectedTitle(holder, "#ffffff", false, Typeface.NORMAL);
            }
        } else {
            if (datas.size() == 1){
                holder.tv_title.setText(datas.get(0));
                setSelectedTitle(holder, "#eeeeee", true, Typeface.BOLD);
            }else {
                if (datas.toString().contains(title)){
                    for (int i = 0; i < datas.size(); i++) {
                        if (title.equals(datas.get(i))) {
                            holder.tv_title.setText(datas.get(position));
                            if (position == i) {
                                setSelectedTitle(holder, "#eeeeee", true, Typeface.BOLD);
                            } else {
                                setSelectedTitle(holder, "#ffffff", false, Typeface.NORMAL);
                            }
                        }
                    }
                }else {
                    for (int i = 0; i < datas.size(); i++) {
                        holder.tv_title.setText(datas.get(position));
                        if (position == datas.size() - 1){
                            setSelectedTitle(holder, "#eeeeee", true, Typeface.BOLD);
                        }else {
                            setSelectedTitle(holder, "#ffffff", false, Typeface.NORMAL);
                        }

                    }
                }

            }
        }


        return convertView;
    }

    private void setSelectedTitle(ViewHolder holder, String s, boolean b, int bold) {
        holder.rl_titlename.setBackgroundColor(Color.parseColor(s));
        holder.tv_title.setSelected(b);
        //textview动态设置字体为粗体
        holder.tv_title.setTypeface(Typeface.defaultFromStyle(bold));
    }


    class ViewHolder {
        RelativeLayout rl_titlename;
        TextView tv_title;
    }


}

