package appler.com.example.module_gather.gather;


import android.content.Context;

import java.util.List;

//获取拍摄的图片或视频 路径
/**
 * 上下文
 * 图片集合
 * */
public interface PhotoOrVideoReturnListener {
    void returnPhotoOrVideo(Context context, List<String> photoList);
}