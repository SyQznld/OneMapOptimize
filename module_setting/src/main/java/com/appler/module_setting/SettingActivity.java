package com.appler.module_setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.oneMap.module.common.bean.LoginData;
import com.oneMap.module.common.dao.LoginDao;
import com.oneMap.module.common.global.ArouterConstants;
import com.oneMap.module.common.utils.ClearCacheUtil;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.ToastUtils;
import com.oneMap.module.common.widget.ColorPickerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


@Route(path = ArouterConstants.TO_SETTINGACTIVITY)
public class SettingActivity extends Activity implements View.OnClickListener {

    private String TAG = getClass().getSimpleName();
    private ImageView iv_setting_back;
    private CircleImageView civ_setting_head;
    private TextView tv_setting_name;
    private TextView tv_setting_role;
    private TextView tv_version;
    private LinearLayout ll_setting_version;
    private TextView tv_setting_totalCache;
    private LinearLayout ll_setting_clear;
    private LinearLayout ll_setting_resetPwd;
    private LinearLayout ll_setting_exit;
    private LinearLayout ll_setting_theme;
    private TextView tv_setting_screen;
    private Switch switch_setting;

    private String SCREEN_LANDSCAPE = "横屏";
    private String SCREEN_PORTRAIT = "竖屏";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme();

        //获取屏幕方向并设置
//        getOrientation();

        setContentView(R.layout.activity_setting);

        initView();

        //设置个人相关属性值
        initData();
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.iv_setting_back) {
            ARouter.getInstance().build(ArouterConstants.TO_MAINACTIVITY).navigation();
            finish();
        }
        if (viewId == R.id.ll_setting_version) {   //版本更新
        }
        if (viewId == R.id.ll_theme) {   //主题颜色设置

            setThemeColor();
        }
        if (viewId == R.id.ll_setting_clear) {     //清除缓存
            clearCache();
        }
        if (viewId == R.id.ll_setting_resetPwd) {    //重置密码

            resetPassword();
        }
        if (viewId == R.id.ll_setting_exit) {         //退出登录
            loginOut();
        }
    }

    private void loginOut() {
        new MaterialDialog.Builder(SettingActivity.this)
                .title("退出登录！")
                .content("确定退出当前用户！")
                .positiveText("确定")
                .negativeText("取消")
                .widgetColor(Color.BLUE)//不再提醒的checkbox 颜色

                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            new Handler().postDelayed(new Runnable() {
                                @SuppressLint("ApplySharedPref")
                                @Override
                                public void run() {
                                    LoginData loginData = LoginDao.getLoginData();
                                    if (null != loginData) {
                                        LoginDao.deleteLoginData(loginData.getId());
                                        LoginDao.deleAllData();
                                        finish();
                                        ARouter.getInstance().build(ArouterConstants.TO_LOGINACTIVITY).navigation();
                                        //清除保存的屏幕方向数据
                                        SharedPreferences sPreferences = getSharedPreferences("onemap", MODE_PRIVATE);
                                        sPreferences.edit().clear().commit();
                                        //清除保存的主题颜色数据
                                        SharedPreferences themeSpf = getSharedPreferences("theme", MODE_PRIVATE);
                                        themeSpf.edit().clear().commit();
                                    }
                                }
                            }, 500);

                        } else if (which == DialogAction.NEGATIVE) {
                            dialog.dismiss();
                        }
                    }
                }).show();
    }

    private void clearCache() {
        try {
            new MaterialDialog.Builder(SettingActivity.this)
                    .title("清除缓存")
                    .content("确定清除" + ClearCacheUtil.getTotalCacheSize(SettingActivity.this) + "缓存！")
                    .positiveText("确定")
                    .negativeText("取消")
                    .widgetColor(Color.BLUE)//不再提醒的checkbox 颜色

                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (which == DialogAction.POSITIVE) {
                                ClearCacheUtil.clearAllCache(SettingActivity.this);
                                tv_setting_totalCache.setText("0.0KB");

                            } else if (which == DialogAction.NEGATIVE) {
                                dialog.dismiss();
                            }

                        }
                    }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置颜色
     */
    private void setThemeColor() {
        final String[] themeColor = new String[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.theme_color_layout, null);
        RecyclerView rv_theme = dialog.findViewById(R.id.rv_theme);
        TextView tv_theme_cancel = dialog.findViewById(R.id.tv_theme_cancel);
        TextView tv_theme_sure = dialog.findViewById(R.id.tv_theme_sure);
        TextView tv_theme_custom = dialog.findViewById(R.id.tv_theme_custom);


        List<ThemeColorData> themeList = new ArrayList<>();
        themeList.add(new ThemeColorData("yellow", "黄色", R.drawable.yellow_circle, false));
        themeList.add(new ThemeColorData("orange", "橙色", R.drawable.orange_circle, false));
        themeList.add(new ThemeColorData("pink", "粉色", R.drawable.pink_circle, false));
        themeList.add(new ThemeColorData("red", "红色", R.drawable.red_circle, false));
        themeList.add(new ThemeColorData("blue", "蓝色", R.drawable.blue_circle, false));
        themeList.add(new ThemeColorData("green", "绿色", R.drawable.green_circle, false));
        themeList.add(new ThemeColorData("purple", "紫色", R.drawable.purple_circle, false));
        themeList.add(new ThemeColorData("black", "黑色", R.drawable.black_circle, false));


        SharedPreferences sPreferences = getSharedPreferences("theme", MODE_PRIVATE);
        String color = sPreferences.getString("themeColor", "");
        if ("".equals(color) || null == color || "null".equals(color)) {
            color = "black";
        }
        for (int i = 0; i < themeList.size(); i++) {
            String s = themeList.get(i).getColor();
            if (color.equals(s)) {
                themeList.get(i).setSelected(true);
            } else {
                themeList.get(i).setSelected(false);
            }
        }
        rv_theme.setLayoutManager(new GridLayoutManager(SettingActivity.this, 4));
        ThemeColorAdapter adapter = new ThemeColorAdapter(SettingActivity.this, themeList);
        rv_theme.setAdapter(adapter);
        adapter.setThemeColorClickListener(new ThemeColorAdapter.ThemeColorClickListener() {
            @Override
            public void selectTheme(int position, ThemeColorData themeColorData) {
                themeColor[0] = themeColorData.getColor();
            }
        });

        builder.setView(dialog);
        AlertDialog show = builder.show();
        tv_theme_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("theme", MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("themeColor", themeColor[0]);
                edit.commit();
                show.dismiss();
                //设置完颜色值后，跳转到主页
                ARouter.getInstance().build(ArouterConstants.TO_MAINACTIVITY).navigation();
                finish();
            }
        });
        tv_theme_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.dismiss();
            }
        });

        tv_theme_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //自定义颜色  色盘选择
                show.dismiss();

                //弹框色盘选择
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                View dialog = LayoutInflater.from(SettingActivity.this).inflate(R.layout.theme_colorpicker_layout, null);
                final ImageView img_color = dialog.findViewById(R.id.theme_img_color);
                final ImageView img_picker = dialog.findViewById(R.id.theme_img_picker);
                final ColorPickerView colorPickerView = dialog.findViewById(R.id.theme_color_picker);
                colorPickerView.setImgPicker(SettingActivity.this, img_picker, 25); //最后一个参数是该颜色指示圈的大小(dp)
                colorPickerView.setColorChangedListener(new ColorPickerView.onColorChangedListener() {
                    @Override
                    public void colorChanged(int red, int blue, int green) {
                        int argb = Color.argb(255, red, green, blue);
                        img_color.setColorFilter(argb);
                        //十六进制的颜色字符串
                        String rgbColor = "#" + CommonUtil.toBrowserHexValue(red) + CommonUtil.toBrowserHexValue(green) + CommonUtil.toBrowserHexValue(blue);
                    }

                    @Override
                    public void stopColorChanged(int red, int blue, int green) {

                    }
                });
                builder.setTitle("颜色选择！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setView(dialog);
                builder.show();

            }
        });

    }

    //自定义布局显示  重置密码 请求接口
    private void resetPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.reset_password_layout, null);
        final EditText et_pwd_old = dialog.findViewById(R.id.et_pwd_old);
        final EditText et_pwd_new = dialog.findViewById(R.id.et_pwd_new);
        final EditText et_pwd_new_sure = dialog.findViewById(R.id.et_pwd_new_sure);

        builder.setTitle("重置密码！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String oldStr = et_pwd_old.getText().toString();
                String newStr = et_pwd_new.getText().toString();
                String newSureStr = et_pwd_new_sure.getText().toString();
                if ("".equals(oldStr)) {
                    ToastUtils.showShortToast("旧密码不能为空~");
                } else if ("".equals(newStr)) {
                    ToastUtils.showShortToast("新密码不能为空~");
                } else if ("".equals(newSureStr)) {
                    ToastUtils.showShortToast("请输入新密码确认~");
                } else if (!newStr.equals(newSureStr)) {
                    ToastUtils.showShortToast("两次输入密码不同~");
                } else if (newStr.equals(oldStr) || newSureStr.equals(oldStr)) {
                    ToastUtils.showShortToast("新密码与旧密码一样~");
                } else {
//                        versionUpdatePresenter.resetPassword(FlagConstant.FLAG_UpdataPW, UserDao.getUserID(), oldStr, newStr);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setView(dialog);
        builder.show();
    }

    private void initData() {
        LoginData loginData = LoginDao.getLoginData();
        if (null != loginData) {
            String realname = loginData.getRealname();
            String role = loginData.getRole();
            tv_setting_name.setText(realname);
            tv_setting_role.setText(role);
        }

        //显示用户此前保存的数据
        SharedPreferences sPreferences = getSharedPreferences("onemap", MODE_PRIVATE);
        String screenOrientation = sPreferences.getString("screen", "");
        if (!"".equals(screenOrientation) && null != screenOrientation) {
            if (SCREEN_LANDSCAPE.equals(screenOrientation)) {
                tv_setting_screen.setText("设置竖屏");
                spSaveScreenData(sPreferences, SCREEN_PORTRAIT);

            } else if (SCREEN_PORTRAIT.equals(screenOrientation)) {
                tv_setting_screen.setText("设置横屏");
                spSaveScreenData(sPreferences, SCREEN_LANDSCAPE);
            }
        }


        //缓存
        try {
            String totalCacheSize = ClearCacheUtil.getTotalCacheSize(SettingActivity.this);
            tv_setting_totalCache.setText(totalCacheSize);

            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            tv_version.setText(versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void spSaveScreenData(SharedPreferences sPreferences, String orientation) {
        switch_setting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SharedPreferences.Editor editor = sPreferences.edit();
                    //保存屏幕方向
                    editor.putString("screen", orientation);
                    //切记最后要使用commit方法将数据写入文件
                    editor.commit();
                } else {
                    String orienStr = null;
                    if ("横屏".equals(orientation)) {
                        orienStr = "竖屏";
                    } else if ("竖屏".equals(orientation)) {
                        orienStr = "横屏";
                    }
                    SharedPreferences.Editor editor = sPreferences.edit();
                    //保存屏幕方向
                    editor.putString("screen", orienStr);
                    //切记最后要使用commit方法将数据写入文件
                    editor.commit();
                }
                //选择后 直接返回到主页面
                finish();
            }
        });
    }


    private void initView() {
        iv_setting_back = findViewById(R.id.iv_setting_back);
        iv_setting_back.setOnClickListener(this);
        civ_setting_head = findViewById(R.id.civ_setting_head);
        civ_setting_head.setOnClickListener(this);
        tv_setting_name = findViewById(R.id.tv_setting_name);
        tv_setting_name.setOnClickListener(this);
        tv_setting_role = findViewById(R.id.tv_setting_role);
        tv_setting_role.setOnClickListener(this);
        tv_version = findViewById(R.id.tv_version);
        tv_version.setOnClickListener(this);
        ll_setting_version = findViewById(R.id.ll_setting_version);
        ll_setting_version.setOnClickListener(this);
        tv_setting_totalCache = findViewById(R.id.tv_setting_totalCache);
        tv_setting_totalCache.setOnClickListener(this);
        ll_setting_clear = findViewById(R.id.ll_setting_clear);
        ll_setting_clear.setOnClickListener(this);
        ll_setting_resetPwd = findViewById(R.id.ll_setting_resetPwd);
        ll_setting_resetPwd.setOnClickListener(this);
        ll_setting_exit = findViewById(R.id.ll_setting_exit);
        ll_setting_exit.setOnClickListener(this);
        ll_setting_theme = findViewById(R.id.ll_theme);
        ll_setting_theme.setOnClickListener(this);
        tv_setting_screen = findViewById(R.id.tv_setting_screen);
        switch_setting = findViewById(R.id.switch_setting);
    }


    //获取屏幕方向并设置
    private void getOrientation() {
        SharedPreferences sPreferences = getSharedPreferences("onemap", MODE_PRIVATE);
        String screen = sPreferences.getString("screen", "");
        if ("竖屏".equals(screen)) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//固定屏幕方向
            }
        } else if ("横屏".equals(screen)) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//固定屏幕方向
            }
        }
    }

    /**
     * 设置主题颜色
     */
    private void setTheme() {
        SharedPreferences sPreferences = getSharedPreferences("theme", MODE_PRIVATE);
        String themeColor = sPreferences.getString("themeColor", "");
        if (!"".equals(themeColor) && null != themeColor && !"null".equals(themeColor)) {
            switch (themeColor) {
                case "yellow":
                    setTheme(R.style.YellowAppTheme);
                    break;
                case "orange":
                    setTheme(R.style.OrangeAppTheme);
                    break;
                case "pink":
                    setTheme(R.style.PinkAppTheme);
                    break;
                case "red":
                    setTheme(R.style.RedAppTheme);
                    break;
                case "blue":
                    setTheme(R.style.BlueAppTheme);
                    break;
                case "green":
                    setTheme(R.style.GreenAppTheme);
                    break;
                case "purple":
                    setTheme(R.style.PurpleAppTheme);
                    break;
                case "black":
                    setTheme(R.style.BlckAppTheme);
                    break;
            }
        } else {
            setTheme(R.style.BlckAppTheme);
        }
    }


    /**
     * 点击两次退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            ARouter.getInstance().build(ArouterConstants.TO_MAINACTIVITY).navigation();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
