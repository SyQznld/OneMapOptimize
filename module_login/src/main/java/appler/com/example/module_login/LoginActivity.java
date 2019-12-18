package appler.com.example.module_login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oneMap.module.common.base.BaseRetrofit;
import com.oneMap.module.common.bean.LoginData;
import com.oneMap.module.common.dao.LoginDao;
import com.oneMap.module.common.global.ApiService;
import com.oneMap.module.common.global.ArouterConstants;
import com.oneMap.module.common.global.InterfaceConstants;
import com.oneMap.module.common.utils.CommonUtil;
import com.oneMap.module.common.utils.Permission;
import com.oneMap.module.common.utils.ToastUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import appler.com.example.module_login.widget.CobWebView;
import appler.com.example.module_login.widget.SubmitButton;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

@Route(path = ArouterConstants.TO_LOGINACTIVITY)
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = getClass().getSimpleName();
    private RelativeLayout rl_login;
    private CobWebView cobWebview;
    private FrameLayout fl_cobWebview;
    /**
     * 请输入用户名
     */
    private EditText et_username;
    /**
     * 请输入密码
     */
    private EditText et_password;
    private LinearLayout ll_login;
    /**
     * 登录
     */
    private SubmitButton sBtnLoading, sBtnProgress;

    private TextView textView;
    private TextView tv_login_version;
    private GifImageView mGivLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //设置权限
        Permission permission = new Permission();
        permission.setPermission(this);
        Log.i(TAG, "onCreate: getAppProcessName" + CommonUtil.getAppProcessName(this));
        //初始化view
        initView();

        //设置登录背景
        setLoginBackground();

        //设置网状背景
        initCobWebView();

        et_username.setText("admin");
        et_password.setText("admin");

        //如果已经登录过，直接跳转到主页面
        LoginData loginData = LoginDao.getLoginData();
        if (null != loginData) {
            toMainActivity();
        }
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.sbtn_loading) {
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();
            String msg = TextUtils.isEmpty(username) ? "账号不能为空！" : TextUtils.isEmpty(password) ? "密码不能为空！" : "";
            if (!TextUtils.isEmpty(msg)) {
                ToastUtils.showShortToast(msg);
                sBtnLoading.reset();
            } else {
                requestLogin(InterfaceConstants.FLAG_LOGIN, username, password);

            }
        }
        if (viewId == R.id.giv_login) {
            GifDrawable gifDrawable = (GifDrawable) mGivLogin.getDrawable();
            if (gifDrawable.isRunning()) {
                gifDrawable.stop();
            } else if (!gifDrawable.isRunning()) {
                gifDrawable.start();
            }
        }
    }


    private void initView() {
        rl_login = findViewById(R.id.rl_login);
        cobWebview = findViewById(R.id.cobWebview);
        cobWebview.setOnClickListener(this);
        fl_cobWebview = findViewById(R.id.fl_cobWebview);
        fl_cobWebview.setOnClickListener(this);
        et_username = findViewById(R.id.et_username);
        et_username.setOnClickListener(this);
        et_password = findViewById(R.id.et_password);
        et_password.setOnClickListener(this);
        ll_login = findViewById(R.id.ll_login);
        ll_login.setOnClickListener(this);

        sBtnLoading = findViewById(R.id.sbtn_loading);
        sBtnProgress = findViewById(R.id.sbtn_progress);
        sBtnLoading.setOnClickListener(this);
        sBtnProgress.setOnClickListener(this);
        tv_login_version = findViewById(R.id.tv_login_version);
        textView = findViewById(R.id.textView);


        sBtnLoading.setOnResultEndListener(new SubmitButton.OnResultEndListener() {
            @Override
            public void onResultEnd() {
                sBtnLoading.reset();
            }
        });
        mGivLogin = (GifImageView) findViewById(R.id.giv_login);
        mGivLogin.setOnClickListener(this);
    }

    //设置登录背景
    @SuppressLint("SetTextI18n")
    private void setLoginBackground() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("HH");
        String str = df.format(date);
        int a = Integer.parseInt(str);
        if (a > 6 && a <= 18) {
            rl_login.setBackgroundResource(R.drawable.good_morning_img);
            textView.setText("Good   Day ! ");
        }
        if (a > 18 && a <= 24 || a >= 0 && a <= 6) {
            rl_login.setBackgroundResource(R.drawable.good_night_img);
            textView.setText("Good   Night ! ");
        }

    }


    //网状背景 基本设置
    private void initCobWebView() {
        cobWebview.setPointNum(70);
        cobWebview.setLineWidth(6);
        cobWebview.restart();
    }


    /**
     * 登录接口请求
     */
    public void requestLogin(String flag, String username, String userpass) {
        ApiService apiService = BaseRetrofit.getApiService();
        apiService.getLogin(flag, username, userpass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subject<ResponseBody>() {
                    @Override
                    public boolean hasObservers() {
                        return false;
                    }

                    @Override
                    public boolean hasThrowable() {
                        return false;
                    }

                    @Override
                    public boolean hasComplete() {
                        return false;
                    }

                    @Override
                    public Throwable getThrowable() {
                        return null;
                    }

                    @Override
                    protected void subscribeActual(Observer<? super ResponseBody> observer) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String string = responseBody.string();
                            Log.i(TAG, "login: " + string);
                            if ("账号或密码错误！".equals(string)) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sBtnLoading.doResult(false);
                                        ToastUtils.showShortToast("账号或密码错误,请重新登录");
                                        sBtnLoading.reset();
                                    }
                                }, 1000);
                            } else if (!"".equals(string) && string.startsWith("[{")) {
                                List<LoginData> list = new Gson().fromJson(string, new TypeToken<List<LoginData>>() {
                                }.getType());

                                LoginData loginData = list.get(0);
                                LoginData user = LoginDao.getLoginData();
                                if (user == null) {   //为空，插入
                                    user = new LoginData();
                                    saveLoginUserInfo(loginData, user);
                                    LoginDao.insertLoginData(user);
                                } else {    //已经存在，更新
                                    saveLoginUserInfo(loginData, user);
                                    LoginDao.updateLoginData(user);
                                }
                                //页面跳转
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sBtnLoading.doResult(true);
                                        toMainActivity();
                                    }
                                }, 1000);
                            } else {
                                sBtnLoading.reset();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.i(TAG, "onNext login: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        sBtnLoading.reset();
                        Log.i(TAG, "onNext login: " + e.getMessage());
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    //请求接口登录成功后跳转到主页面
    private void toMainActivity() {

        ARouter.getInstance()
                .build(ArouterConstants.TO_MAINACTIVITY)
                .navigation();
        finish();
    }


    /**
     * 保存登录人员基本信息
     */
    private void saveLoginUserInfo(LoginData loginData, LoginData user) {
        user.setUserId(loginData.getUserId());
        user.setRole(loginData.getRole());
        user.setPower(loginData.getPower());
        user.setUsermanager(loginData.getUsermanager());
        user.setNote(loginData.getNote());
        user.setUsername(loginData.getUsername());
        user.setPassword(loginData.getPassword());
        user.setRealname(loginData.getRealname());
        user.setCompany(loginData.getCompany());
        user.setRoleid(loginData.getRoleid());
        user.setTelephone(loginData.getTelephone());
        user.setLayers(loginData.getLayers());
        user.setMokuai(loginData.getMokuai());
    }


}
