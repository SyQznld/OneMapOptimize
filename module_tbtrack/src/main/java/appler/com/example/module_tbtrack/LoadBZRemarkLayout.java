package appler.com.example.module_tbtrack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oneMap.module.common.bean.BzData;
import com.oneMap.module.common.bean.EventBusData;
import com.oneMap.module.common.dao.BzDao;
import com.oneMap.module.common.global.Global;
import com.oneMap.module.common.utils.CommonUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 距离量算完成后 保存时需要填写的字段信息弹框
 */
public class LoadBZRemarkLayout {

    private String TAG = getClass().getSimpleName();
    private Context context;
    private LinearLayout linearLayout;
    private WebView webView;
    private ImageButton ib_zhunxin;

    private ImageView iv_tbremark_back;
    private TextView tv_tbremark_save;
    private TextView tv_tbremark_time;
    private TextView tv_tbremark_type;
    private TextView tv_tbremark_result;
    private EditText et_tbremark_name;
    private EditText et_tbremark_remark;

    private String tbType = "";
    private String tbResult = "";

    private String bzPolygon;//js回传的图斑坐标点
    private View tbRemarkView;

    public LoadBZRemarkLayout(Context context, LinearLayout linearLayout, WebView webView, ImageButton ib_zhunxin, String tbType, String tbResult, String bzPolygon) {
        this.context = context;
        this.linearLayout = linearLayout;
        this.webView = webView;
        this.ib_zhunxin = ib_zhunxin;
        this.tbType = tbType;
        this.tbResult = tbResult;
        this.bzPolygon = bzPolygon;
    }

    public void showTbRemarkLayout() {
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
        tbRemarkView = inflater.inflate(R.layout.tbremark_layout, null);
        tbRemarkView.setLayoutParams(lp);
        linearLayout.addView(tbRemarkView);


        //初始化控件
        initView(tbRemarkView);

        //控件点击事件
        viewOnClick(tbRemarkView);

    }



    private void viewOnClick(View tbRemarkView) {
        iv_tbremark_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回上一级
                EventBusData event = new EventBusData("bzMeasureBack");
                EventBus.getDefault().post(event);
            }
        });
        tv_tbremark_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bzSave();
            }
        });

    }

    //标注保存
    private void bzSave() {

        String bzTime = tv_tbremark_time.getText().toString();
        String bzType = tv_tbremark_type.getText().toString();
        String bzResult = tv_tbremark_result.getText().toString();
        String bzName = et_tbremark_name.getText().toString();
        String bzRemark = et_tbremark_remark.getText().toString();

        if ("".equals(bzName)) {
            Toast.makeText(context, "标注名称不能为空", Toast.LENGTH_SHORT).show();
        } else {
            BzData bzData = new BzData();
            bzData.setBzTime(bzTime);
            bzData.setBzType(bzType);
            bzData.setBzResult(bzResult);
            bzData.setBzName(bzName);
            bzData.setBzRemark(bzRemark);
            bzData.setPolygon(bzPolygon);


            //先判断txt是否存在      不存在则删除数据库中的数据
            String bzJsonTxt = CommonUtil.readTxtFromSDCard(Global.TXT_SAVE_PATH, Global.BZ_TXT);
            if ("".equals(bzJsonTxt)) {
                BzDao.deleteAllData();
            }

            BzDao.insertBz(bzData);
            webView.loadUrl("javascript:measureClear()");
            Toast.makeText(context, "标注保存成功", Toast.LENGTH_SHORT).show();
            linearLayout.removeView(tbRemarkView);
            ib_zhunxin.setVisibility(View.GONE);


            List<BzData> bzDataList = BzDao.queryAll();
            if (null != bzDataList) {
                String bzJson = new Gson().toJson(bzDataList);
                File saveJsonFile = new File(Global.TXT_SAVE_PATH, Global.BZ_TXT);
                if (saveJsonFile.exists()) {
                    saveJsonFile.delete();
                }
                CommonUtil.saveStrToTxt(Global.TXT_SAVE_PATH, Global.BZ_TXT, bzJson);
            }

        }

    }


    private void initView(View tbRemarkView) {
        iv_tbremark_back = tbRemarkView.findViewById(R.id.iv_tbremark_back);
        tv_tbremark_save = tbRemarkView.findViewById(R.id.tv_tbremark_save);
        tv_tbremark_time = tbRemarkView.findViewById(R.id.tv_tbremark_time);
        tv_tbremark_type = tbRemarkView.findViewById(R.id.tv_tbremark_type);
        tv_tbremark_result = tbRemarkView.findViewById(R.id.tv_tbremark_result);
        et_tbremark_name = tbRemarkView.findViewById(R.id.et_tbremark_name);
        et_tbremark_remark = tbRemarkView.findViewById(R.id.et_tbremark_remark);

        if (tbType.equals("ceju")) {
            tbType = "线";
        } else if (tbType.equals("huamian")) {
            tbType = "面";
        }
        @SuppressLint("SimpleDateFormat") String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        tv_tbremark_type.setText(tbType);
        tv_tbremark_time.setText(time);
        tv_tbremark_result.setText(tbResult);
    }


}
