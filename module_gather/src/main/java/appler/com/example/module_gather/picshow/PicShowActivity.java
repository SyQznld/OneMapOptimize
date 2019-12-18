package appler.com.example.module_gather.picshow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.oneMap.module.common.global.ArouterConstants;

import java.util.ArrayList;
import java.util.List;

import appler.com.example.module_gather.R;
import appler.com.example.module_gather.selectpic.MyPhotoViewPager;


/**
 *
 * */
@Route(path = ArouterConstants.TO_PICSHOWACTIVITY)
public class PicShowActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = getClass().getSimpleName();
    private ImageView iv_picAct_back;
    private MyPhotoViewPager vp_show_photo;
    private TextView tv_photo_position;
    private TextView tv_photo_count;

    private MyPhotoShowAdapter adapter;
    private List<String> photoList = new ArrayList<>(); //当前选择的所有图片

    @Autowired(name = "position")
    int currentPosition;
    @Autowired(name = "picList")
    List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_show);
        ARouter.getInstance().inject(this);

        initView();

        //图片显示
        setPicData();
    }

    private void setPicData() {
        data = getIntent().getExtras().getStringArrayList("picList");   //不加这句获取不到值
        for (int i = 0; i < data.size(); i++) {
            String s = data.get(i);
            if (s.endsWith(".jpg") || s.endsWith(".png") || s.endsWith(".jpeg")) {
                photoList.add(s);
            }
        }

        adapter = new MyPhotoShowAdapter(photoList, this);
        vp_show_photo.setAdapter(adapter);
        vp_show_photo.setCurrentItem(currentPosition, false);
        tv_photo_position.setText(currentPosition + 1 + "");
        tv_photo_count.setText(photoList.size() + "");

        vp_show_photo.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                tv_photo_position.setText(currentPosition + 1 + "");
                tv_photo_count.setText(photoList.size() + "");
            }
        });
    }

    private void initView() {
        iv_picAct_back = (ImageView) findViewById(R.id.iv_picAct_back);
        iv_picAct_back.setOnClickListener(this);
        vp_show_photo = (MyPhotoViewPager) findViewById(R.id.vp_show_photo);
        vp_show_photo.setOnClickListener(this);
        tv_photo_position = (TextView) findViewById(R.id.tv_photo_position);
        tv_photo_position.setOnClickListener(this);
        tv_photo_count = (TextView) findViewById(R.id.tv_photo_count);
        tv_photo_count.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.iv_picAct_back) {
            finish();
        }
    }
}

