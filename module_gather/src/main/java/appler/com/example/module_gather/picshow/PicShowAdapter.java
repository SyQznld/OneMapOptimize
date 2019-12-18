package appler.com.example.module_gather.picshow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.oneMap.module.common.utils.GlideUtils;

import java.util.List;

import appler.com.example.module_gather.R;

public class PicShowAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private String TAG = "PicShowAdapter";
    private Context context;
    private List<String> data;

    public PicShowAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.picshow_item, data);
        this.context = context;
        this.data = data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        ImageView iv_photo = helper.getView(R.id.iv_picshow_item);
        ImageView iv_video = helper.getView(R.id.iv_picshow_video);
        if (item.contains(".mp4") || item.contains(".MP4")) {
            iv_video.setVisibility(View.VISIBLE);
        } else {
            iv_video.setVisibility(View.GONE);
        }

        iv_photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideUtils.loadNet(iv_photo, item, R.mipmap.zrzy);
        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picItemClickListener.toShowPic(item, helper.getLayoutPosition());
            }
        });

        iv_photo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                picItemClickListener.deletePic(item, helper.getLayoutPosition());
                return false;
            }
        });
    }

    public PicItemClickListener picItemClickListener;

    public void setPhotoItemClickListener(PicItemClickListener picItemClickListener) {
        this.picItemClickListener = picItemClickListener;
    }

    public interface PicItemClickListener {

        void toShowPic(String item, int position);

        void deletePic(String item, int position);
    }


}
