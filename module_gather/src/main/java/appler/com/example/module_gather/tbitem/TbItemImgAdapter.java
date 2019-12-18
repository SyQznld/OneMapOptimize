package appler.com.example.module_gather.tbitem;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.oneMap.module.common.global.ArouterConstants;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.GlideUtils;

import java.util.List;

import appler.com.example.module_gather.R;

public class TbItemImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private String TAG = getClass().getSimpleName();
    private Context context;
    private List<String> data;
    private WebView mWebView;
    private View LastView;

    public TbItemImgAdapter(WebView mWebView, Context context, @Nullable List<String> data) {
        super(R.layout.tb_img_item, data);
        this.mWebView = mWebView;
        this.context = context;
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView iv_tbitem_img = helper.getView(R.id.iv_tbitem_img);
        ImageView iv_tbitem_video = helper.getView(R.id.iv_tbitem_video);


        iv_tbitem_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideUtils.loadNet(iv_tbitem_img, item.trim(), R.mipmap.zrzy);
        if (item.contains("jpg")) {
            iv_tbitem_video.setVisibility(View.GONE);
        } else if (item.contains("mp4")) {
            iv_tbitem_video.setVisibility(View.VISIBLE);
        }
        iv_tbitem_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.contains("jpg")) {
                    final String[] photo_pathArray = item.split("&|.jpg");
                    String lnglat = photo_pathArray[1].replace('$', ',');
                    String formatvalues = photo_pathArray[2];
                    mWebView.loadUrl("javascript:showTb_picAngle('" + item + "','" + lnglat + "','" + formatvalues + "')");
                    if (LastView == null) {
                        LastView = view;
                    }
                    if (LastView != null) {
                        if (view != LastView) {
                            LastView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            view.setBackgroundColor(CommonUtil.getDarkColorPrimary(context));
                            LastView = view;
                        } else {
                            view.setBackgroundColor(CommonUtil.getDarkColorPrimary(context));
                        }
                    }
                } else if (item.contains("mp4")) {
                    ARouter.getInstance()
                            .build(ArouterConstants.TO_VIDEOSHOWACTIVITY)
                            .withString("videoPath", item)
                            .navigation();
                }
            }
        });
    }

}


