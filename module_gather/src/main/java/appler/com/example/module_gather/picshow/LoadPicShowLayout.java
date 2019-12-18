package appler.com.example.module_gather.picshow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.launcher.ARouter;
import com.oneMap.module.common.global.ArouterConstants;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import appler.com.example.module_gather.R;
import appler.com.example.module_gather.gather.LoadGatherLayout;

/**
 * 采集模块拍摄的图片、视频等展示
 */
public class LoadPicShowLayout implements View.OnClickListener {
    private String TAG = getClass().getSimpleName();
    private Activity context;
    private WebView webView;
    private LinearLayout linearLayout;

    private ImageView iv_back;
    private RecyclerView rv_pics;

    private View picshowView;
    private List<String> picList;

    public LoadPicShowLayout(Activity context, WebView webView, LinearLayout linearLayout) {
        this.context = context;
        this.webView = webView;
        this.linearLayout = linearLayout;
    }


    public void showPicShowLayout(List<String> data) {
        picList = new ArrayList<>();
        picList.addAll(data);
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        LinearLayout.LayoutParams lp;
        boolean screenChange = CommonUtil.isScreenChange();
        if (screenChange) {  //横屏
            lp = new LinearLayout.LayoutParams(width / 3, LinearLayout.LayoutParams.FILL_PARENT);
        } else {
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        picshowView = inflater.inflate(R.layout.picshow_layout, null);
        picshowView.setLayoutParams(lp);
        linearLayout.addView(picshowView);

        //初始化布局控件
        initView();


        //控件点击事件
        setViewOnClick();


        if (screenChange) {  //横屏
            rv_pics.setLayoutManager(new LinearLayoutManager(context));
        } else {
            rv_pics.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }
        PicShowAdapter picShowAdapter = new PicShowAdapter(context, data);
        rv_pics.setAdapter(picShowAdapter);
        picShowAdapter.setPhotoItemClickListener(new PicShowAdapter.PicItemClickListener() {
            @Override
            public void toShowPic(String item, int position) {
                //点击查看图片或视频
                if (data.get(position).endsWith("jpg")
                        || data.get(position).endsWith("png")
                        || data.get(position).endsWith("jpeg")
                        || data.get(position).endsWith("JPG")
                        || data.get(position).endsWith("PNG")
                        || data.get(position).endsWith("JPEG")) {       //图片
                    ARouter.getInstance()
                            .build(ArouterConstants.TO_PICSHOWACTIVITY)
                            .withInt("position", position)
                            .withStringArrayList("picList", (ArrayList<String>) data)
                            .navigation();
                } else {            //视频
                    String videoPath = data.get(position);
                    if (!"".equals(videoPath) && !"null".equals(videoPath)) {
                        ARouter.getInstance()
                                .build(ArouterConstants.TO_VIDEOSHOWACTIVITY)
                                .withString("videoPath", videoPath)
                                .navigation();
                    } else {
                        ToastUtils.showShortToast("无效视频，请检查路径重新播放");
                    }
                }
            }


            @Override
            public void deletePic(String item, int position) {
                //长按删除图片
                new MaterialDialog.Builder(context)
                        .title("删除照片！")
                        .content("确定删除当前图片！")
                        .positiveText("确定")
                        .negativeText("取消")
                        .widgetColor(Color.BLUE)//不再提醒的checkbox 颜色

                        .onAny(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (which == DialogAction.POSITIVE) {

                                    String s = data.get(position);
                                    data.remove(position);
                                    new File(s).delete();
                                    picShowAdapter.notifyDataSetChanged();

                                    picList.clear();
                                    picList.addAll(data);

                                    dialog.dismiss();
                                } else if (which == DialogAction.NEGATIVE) {
                                    dialog.dismiss();
                                }
                            }
                        }).show();
            }
        });

    }


    private void initView() {
        iv_back = picshowView.findViewById(R.id.iv_picshow_back);
        rv_pics = picshowView.findViewById(R.id.rv_picshow_img);
    }

    private void setViewOnClick() {
        iv_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_picshow_back) {
            String picStr = TextUtils.join("#", picList);
            webView.loadUrl("javascript:gatherMeasureBack('" + picStr+ "')");
        }
    }
}
