package appler.com.example.module_tbtrack;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.oneMap.module.common.bean.BzData;
import com.oneMap.module.common.dao.BzDao;
import com.oneMap.module.common.global.Global;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.ToastUtils;

import java.io.File;
import java.util.List;

import appler.com.example.module_tbtrack.adapter.BzListAdapter;


/**
 * 标注列表
 */
public class LoadBZListLayout {

    private String TAG = getClass().getSimpleName();
    private Context context;
    private LinearLayout linearLayout;
    private WebView webView;

    private EditText et_search_input;
    private TextView tv_search_close;
    private ListView list_search;
    private View bzListView;
    private String filterData;

    public LoadBZListLayout(Context context, LinearLayout linearLayout, WebView webView) {
        this.context = context;
        this.linearLayout = linearLayout;
        this.webView = webView;
    }

    public void showTbListLayout() {

        //读取设置数据 列表展示
        setBzData();

    }

    private void bzListPop() {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        LinearLayout.LayoutParams lp;
        boolean screenChange = CommonUtil.isScreenChange();
        if (screenChange) {  //横屏
            lp = new LinearLayout.LayoutParams(width / 4, LinearLayout.LayoutParams.FILL_PARENT);
        } else {
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, height / 3);
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        bzListView = inflater.inflate(R.layout.bzlist_layout, null);
        bzListView.setLayoutParams(lp);
        linearLayout.addView(bzListView);

        //初始化控件
        initView(bzListView);
        //控件点击事件
        viewOnClick(bzListView);

    }

    private void setBzData() {
        //读取标注数据
        String bzJson = CommonUtil.readTxtFromSDCard(Global.TXT_SAVE_PATH, Global.BZ_TXT);
        if (!"".equals(bzJson)) {
            bzListPop();        //有数据时才显示pop

            List<BzData> bzDataList = new Gson().fromJson(bzJson, new TypeToken<List<BzData>>() {
            }.getType());
            if (null != bzDataList) {
                BzListAdapter bzListAdapter = new BzListAdapter(context, bzDataList);
                list_search.setAdapter(bzListAdapter);

                //输入框模糊查询
                et_search_input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        bzListAdapter.getFilter().filter(charSequence);
                        String s = charSequence.toString();
                        filterData = s;
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });

                //查看、删除点击事件
                bzListAdapter.setBzItemClickListener(new BzListAdapter.BzItemClickListener() {
                    @Override
                    public void bzItemClick(BzData bzData, int position) {
                        String polygon = bzData.getPolygon();
                        polygon = polygon.replace(", ", ",");
                        String name = bzData.getBzName();
                        String remark = bzData.getBzRemark();
                        String time = bzData.getBzTime();
                        String result = bzData.getBzResult();

                        if (bzData != null) {
                            String tubanType = bzData.getBzType();
                            String s = setBzShowJson(name, remark, time, result, tubanType);

                            if ("面".equals(tubanType)) {    //面
                                webView.loadUrl("javascript:drawBzPolygon('" + polygon + "'," + s + ")");
                            } else {                        //线
                                webView.loadUrl("javascript:drawBzLine('" + polygon + "'," + s + ")");
                            }
                        }
//                        linearLayout.removeView(bzListView);
                    }


                    @Override
                    public void deleteBz(BzData bzData, int position) {

                        //删除标注同步删除数据库
                        new MaterialDialog.Builder(context)
                                .title("删除标注")
                                .content("确定要删除当前标注吗")
                                .positiveText("确定")
                                .negativeText("取消")
                                .widgetColor(Color.BLUE)

                                .onAny(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        if (which == DialogAction.POSITIVE) {

                                            bzDataList.remove(position);
                                            BzDao.deleteBzById(bzData.getId());
                                            bzListAdapter.notifyDataSetChanged();

                                            //删除最后一条数据后关闭当前列表界面
                                            if (bzDataList.size() < 1) {
                                                webView.loadUrl("javascript:removeSelectedPolygon()");
                                                linearLayout.removeView(bzListView);
                                            }

                                            //保存文件
                                            List<BzData> bzDataList = BzDao.queryAll();
                                            String bzJson = new Gson().toJson(bzDataList);
                                            File saveJsonFile = new File(Global.TXT_SAVE_PATH, Global.BZ_TXT);
                                            if (saveJsonFile.exists()) {
                                                saveJsonFile.delete();
                                            }

                                            if (null != bzDataList) {
                                                CommonUtil.saveStrToTxt(Global.TXT_SAVE_PATH, Global.BZ_TXT, bzJson);
                                            }

                                            //处理模糊查询，再次调用标注按钮
                                            if (null != filterData) {
                                                linearLayout.removeAllViews();
                                                showTbListLayout();
                                                et_search_input.setText(filterData);
                                            }
                                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();


                                        } else if (which == DialogAction.NEGATIVE) {
                                            dialog.dismiss();
                                        }
                                    }
                                }).show();

                    }
                });

            }
        } else {
            ToastUtils.showShortToast("暂无历史标注");
        }
    }


    private void viewOnClick(View bzListView) {
        tv_search_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeView(bzListView);
                webView.loadUrl("javascript:removeSelectedPolygon()");

            }
        });

    }


    private void initView(View bzListView) {
        et_search_input = bzListView.findViewById(R.id.et_search_input);
        tv_search_close = bzListView.findViewById(R.id.tv_search_close);
        list_search = bzListView.findViewById(R.id.list_search);
    }


    /**
     * 拼接显示shp文件的json  传递对象
     */

    private String setBzShowJson(String name, String remark, String time, String result, String tubanType) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("名称", name);
        jsonObject.addProperty("描述", remark);
        jsonObject.addProperty("时间", time);
        if ("线".equals(tubanType)) {
            jsonObject.addProperty("周长", result);
        } else if ("面".equals(tubanType)) {
            jsonObject.addProperty("结果", result);
        }
        return jsonObject.toString();
    }

}
