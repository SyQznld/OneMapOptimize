package com.appler.module_setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ThemeColorAdapter extends BaseQuickAdapter<ThemeColorData, BaseViewHolder> {
    private String TAG = getClass().getSimpleName();
    private Context context;
    private List<ThemeColorData> themeColorData;

    private View lastView;

    public ThemeColorAdapter(Context context, @Nullable List<ThemeColorData> themeColorData) {
        super(R.layout.theme_item, themeColorData);
        this.context = context;
        this.themeColorData = themeColorData;
    }

    @SuppressLint("Range")
    @Override
    protected void convert(BaseViewHolder helper, ThemeColorData item) {
        int colorBcg = item.getColorBcg();
        boolean selected = item.isSelected();

        LinearLayout ll_theme = helper.getView(R.id.ll_theme);
        ImageView iv_color = helper.getView(R.id.iv_theme_color);
        ImageView iv_sure = helper.getView(R.id.iv_theme_sure);
        TextView tv_theme_colorDes = helper.getView(R.id.tv_theme_colorDes);
        iv_color.setImageResource(colorBcg);
        tv_theme_colorDes.setText(item.getColorDes());
        String themeColor = item.getColor();
        String textColor = "";
        switch (themeColor) {
            case "yellow":
                textColor = "#ffee58";
                break;
            case "orange":
                textColor = "#ffa726";
                break;
            case "pink":
                textColor = "#ec407a";
                break;
            case "red":
                textColor = "#ef5350";
                break;
            case "blue":
                textColor = "#42a5f5";
                break;
            case "green":
                textColor = "#66bb6a";
                break;
            case "purple":
                textColor = "#ab47bc";
                break;
            case "black":
                textColor = "#000000";
                break;
        }
        tv_theme_colorDes.setTextColor(Color.parseColor(textColor));

        if (selected) {
            iv_sure.setVisibility(View.VISIBLE);
            lastView = iv_sure;
        }

        ll_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lastView == null) {
                    lastView = iv_sure;
                }
                if (lastView != null) {
                    if (iv_sure != lastView) {
                        lastView.setVisibility(View.GONE);
                        iv_sure.setVisibility(View.VISIBLE);
                        lastView = iv_sure;
                    } else {
                        iv_sure.setVisibility(View.GONE);
                    }
                }

                themeColorClickListener.selectTheme(helper.getLayoutPosition(), item);
            }
        });
    }


    private ThemeColorClickListener themeColorClickListener;

    public void setThemeColorClickListener(ThemeColorClickListener themeColorClickListener) {
        this.themeColorClickListener = themeColorClickListener;
    }

    public interface ThemeColorClickListener {
        void selectTheme(int position, ThemeColorData themeColorData);
    }
}

