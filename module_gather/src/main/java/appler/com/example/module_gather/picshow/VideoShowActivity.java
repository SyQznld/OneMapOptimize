package appler.com.example.module_gather.picshow;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.oneMap.module.common.global.ArouterConstants;
import com.oneMap.module.common.utils.ToastUtils;

import appler.com.example.module_gather.R;

/**
 * 视频播放
 * */
@Route(path = ArouterConstants.TO_VIDEOSHOWACTIVITY)
public class VideoShowActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = getClass().getSimpleName();
    private ImageView iv_videoAct_back;
    private FullScreenVideoView vv_video;

    @Autowired(name = "videoPath")
    String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_show);
        ARouter.getInstance().inject(this);

        initView();

        //播放视频
        videoPlay();

    }

    private void videoPlay() {
        vv_video.setVideoPath(videoPath);

        /**为控件设置焦点*/
        vv_video.requestFocus();
        /**
         * 为 VideoView 视图设置媒体控制器，设置了之后就会自动由进度条、前进、后退等操作
         */
        vv_video.setMediaController(new MediaController(this));
        vv_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                finish();
            }
        });

        vv_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                ToastUtils.showShortToast("视频播放错误");
                return false;
            }
        });
        vv_video.start();
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.iv_videoAct_back){
            finish();
        }
    }


    private void initView() {
        iv_videoAct_back = (ImageView) findViewById(R.id.iv_videoAct_back);
        iv_videoAct_back.setOnClickListener(this);
        vv_video = (FullScreenVideoView) findViewById(R.id.vv_video);
        vv_video.setOnClickListener(this);
    }

}
