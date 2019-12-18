package appler.com.example.module_gather.selectpic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import appler.com.example.module_gather.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mImages;
    private LayoutInflater mInflater;

    //保存选中的图片
    private ArrayList<String> mSelectImages = new ArrayList<>();
    private OnImageSelectListener mSelectListener;
    private OnItemClickListener mItemClickListener;
    private int mMaxCount;
    private boolean isSingle;

    /**
     * @param maxCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     * @param isSingle 是否单选
     */
    public ImageAdapter(Context context, int maxCount, boolean isSingle) {
        mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        mMaxCount = maxCount;
        this.isSingle = isSingle;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.select_images_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String image = mImages.get(position);
        Glide.with(mContext).load(image)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(holder.ivImage);

//        setItemSelect(holder, mSelectImages.contains(image));
        setItemSelect(holder, mSelectImages.contains(image));
        //点击选中/取消选中图片
        holder.ivSelectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectImages.contains(image)) {
                    //如果图片已经选中，就取消选中
                    unSelectImage(image);
                    setItemSelect(holder, false);
                } else if (isSingle) {
                    //如果是单选，就先清空已经选中的图片，再选中当前图片
                    clearImageSelect();
                    selectImage(image);
                    setItemSelect(holder, true);
                } else if (mMaxCount <= 0 || mSelectImages.size() < mMaxCount) {
                    //如果不限制图片的选中数量，或者图片的选中数量
                    // 还没有达到最大限制，就直接选中当前图片。
                    selectImage(image);
                    setItemSelect(holder, true);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    Log.i("", "onClick: " + image);
                    mItemClickListener.OnItemClick(image, holder.getAdapterPosition());
                }
            }
        });
    }

    /**
     * 选中图片
     *
     * @param image
     */
    private void selectImage(String image) {
        mSelectImages.add(image);
        if (mSelectListener != null) {
            mSelectListener.OnImageSelect(image, true, mSelectImages.size());
        }
    }

    /**
     * 取消选中图片
     *
     * @param image
     */
    private void unSelectImage(String image) {
        mSelectImages.remove(image);
        if (mSelectListener != null) {
            mSelectListener.OnImageSelect(image, false, mSelectImages.size());
        }
    }

    @Override
    public int getItemCount() {
        return mImages == null ? 0 : mImages.size();
    }

    public ArrayList<String> getData() {
        return mImages;
    }

    public void refresh(ArrayList<String> data) {
        mImages = data;
        notifyDataSetChanged();
    }

    /**
     * 设置图片选中和未选中的效果
     */
    private void setItemSelect(ViewHolder holder, boolean isSelect) {
        if (isSelect) {
            holder.ivSelectIcon.setImageResource(R.drawable.ic_image_select);
            holder.ivMasking.setAlpha(0.5f);
        } else {
            holder.ivSelectIcon.setImageResource(R.drawable.ic_image_unselect);
            holder.ivMasking.setAlpha(0.2f);
        }
    }

    private void clearImageSelect() {
        if (mImages != null && mSelectImages.size() == 1) {
            int index = mImages.indexOf(mSelectImages.get(0));
            if (index != -1) {
                mSelectImages.clear();
                notifyItemChanged(index);
            }
        }
    }

    public void setSelectedImages(ArrayList<String> selected) {
        if (mImages != null && selected != null) {
            for (String path : selected) {
                if (isFull()) {
                    return;
                }
                for (String image : mImages) {
                    if (path.equals(image)) {
                        if (!mSelectImages.contains(image)) {
                            mSelectImages.add(image);
                        }
                        break;
                    }
                }
            }
            notifyDataSetChanged();
        }
    }


    private boolean isFull() {
        if (isSingle && mSelectImages.size() == 1) {
            return true;
        } else if (mMaxCount > 0 && mSelectImages.size() == mMaxCount) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<String> getSelectImages() {
        return mSelectImages;
    }

    public void setOnImageSelectListener(OnImageSelectListener listener) {
        this.mSelectListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        ImageView ivSelectIcon;
        ImageView ivMasking;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            ivSelectIcon = itemView.findViewById(R.id.iv_select);
            ivMasking = itemView.findViewById(R.id.iv_masking);
        }
    }

    public interface OnImageSelectListener {
        void OnImageSelect(String image, boolean isSelect, int selectCount);
    }

    public interface OnItemClickListener {
        void OnItemClick(String image, int position);
    }
}