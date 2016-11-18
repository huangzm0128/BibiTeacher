package pype.mingming.bibiteacher.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import pype.mingming.bibiteacher.Constant;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.ui.CircleImageView;
import pype.mingming.bibiteacher.utils.CustomProgressDialog;
import pype.mingming.bibiteacher.utils.LogUtils;
import pype.mingming.bibiteacher.utils.ToastUtils;
import pype.mingming.bibiteacher.utils.UserUtils;


/**
 * 登录和注册页面
 * Created by mingming on 2016/7/14.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, UserUtils.LoginListener, UserUtils.SignUpListener {

    /**
     * 登录模式
     */
    private final int MODE_LOGIN = 1;
    /**
     * 注册模式
     */
    private final int MODE_REGISTER = 2;
    /**
     * 当前模式
     */
    private int operation = MODE_LOGIN;
    /**
     * 登录注册类
     */
    private UserUtils userUtils;

    private boolean isStudent = false;

    private final int PhoneNumber = 11;

    private Tencent mTencent;


    private IUiListener listener;

    private User user;

    private boolean isOtherLoginUp = false; // 初始状态没有弹出
    ViewTreeObserver vto; //动态设置控件宽高

    private static  final String  APP_ID = "wxe402a8176c32bf70";

    CustomProgressDialog dialog;


    /**
     * 类名
     */
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.myTopBar)
    TopBar topBar;
    @BindView(R.id.float_label_user)
    LinearLayout userLL;
    @BindView(R.id.user_parents)
    TextView userParents;
    @BindView(R.id.user_student)
    TextView userStudent;
    @BindView(R.id.login_user_phone)
    EditText loginUserPhone;
    @BindView(R.id.float_label_phone)
    TextInputLayout floatLabelPhone;
    @BindView(R.id.user_icon)
    CircleImageView userIcon;
    @BindView(R.id.login_user_name)
    EditText loginUserName;
    @BindView(R.id.login_user_password)
    EditText loginUserPassword;
    @BindView(R.id.register_tv)
    TextView registerTv;
    @BindView(R.id.back_login)
    TextView backLogin;
    @BindView(R.id.find_lost_password)
    TextView findLostPassword;
    @BindView(R.id.register_button)
    Button registerButton;
    @BindView(R.id.float_label_name)
    TextInputLayout floatLabelName;
    @BindView(R.id.float_label_password)
    TextInputLayout floatLabelPassword;

    @BindView(R.id.login_container)
    LinearLayout container; //父框
    //其它登录方式
    @BindView(R.id.login_qq)
    TextView loginQq;
    @BindView(R.id.login_weixin)
    TextView loginWeixin;
    @BindView(R.id.login_xinlang)
    TextView loginXinlang;
    @BindView(R.id.login_zhifubao)
    TextView loginZhifubao;

    //其它登录方式的弹出框的父容器
    @BindView(R.id.popup_view)
    LinearLayout popupView;
    @BindView(R.id.neddmiss)
    LinearLayout otherLoginneddmiss; //新浪，支付宝登录隐藏
    @BindView(R.id.login_click_up_click_down) // 点击弹出下面
    ImageView loginCUPCD;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initDada() {
        super.initDada();
        initToolbar();
        userUtils = new UserUtils(this);

        //动态设置控件宽高
        vto = container.getViewTreeObserver();

        //微信api注册
        mIwapi = WXAPIFactory.createWXAPI(this, APP_ID, true);
        mIwapi.registerApp(APP_ID);
        //初始化Tencent
       mTencent = Tencent.createInstance(Constant.QQ_APPID, this.getApplicationContext());

        dialog = new CustomProgressDialog(LoginActivity.this,"网络君正在奔跑..", R.drawable.myprogressframe);

    }

    /**
     * 设置标题栏
     */
    private void initToolbar() {
        topBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        topBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
    }

    @Override
    protected void setListener() {
        super.setListener();
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                if(operation == MODE_REGISTER){
                    try {
                        switchUI(MODE_LOGIN);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    onBackPressed();
                }
            }

            @Override
            public void rightButtonOnClick() {
                //无
            }
        });

        userParents.setOnClickListener(this);
        userStudent.setOnClickListener(this);
        registerTv.setOnClickListener(this);
        backLogin.setOnClickListener(this);
        findLostPassword.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        //添加文本改变监听
        loginUserName.addTextChangedListener(new MyTextWatcher(loginUserName));
        loginUserPassword.addTextChangedListener(new MyTextWatcher(loginUserPassword));
        loginUserPhone.addTextChangedListener(new MyTextWatcher(loginUserPhone));

        //动态设置控件宽高
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int h = otherLoginneddmiss.getHeight();
                FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) popupView.getLayoutParams();
                lp.bottomMargin = -h;
                return true;
            }
        });

        //其它登录方式
        loginCUPCD.setOnClickListener(this);
        loginQq.setOnClickListener(this);
        loginWeixin.setOnClickListener(this);
        loginXinlang.setOnClickListener(this);
        loginZhifubao.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode,resultCode,data,listener);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_parents:
                userParents.setBackgroundResource(R.drawable.login_select_user_bg);
                userParents.setTextColor(getResources().getColor(R.color.default_tab_view_selected_color));
                userStudent.setBackgroundResource(R.drawable.login_other_login_bg);
                userStudent.setTextColor(getResources().getColor(R.color.white));
                isStudent = false;
                break;

            case R.id.user_student:
                userStudent.setBackgroundResource(R.drawable.login_select_user_bg);
                userStudent.setTextColor(getResources().getColor(R.color.default_tab_view_selected_color));
                userParents.setBackgroundResource(R.drawable.login_other_login_bg);
                userParents.setTextColor(getResources().getColor(R.color.white));
                isStudent = true;
                break;

            case R.id.register_tv:
                if (operation == MODE_LOGIN) {
                    try {
                        switchUI(MODE_REGISTER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.back_login:
                if (operation == MODE_REGISTER) {
                    try {
                        switchUI(MODE_LOGIN);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.register_button:
                hideSoftKeyBoard(loginUserPassword);
                if (operation == MODE_LOGIN) {
                    if (!TextUtils.isEmpty(loginUserName.getText()) && !TextUtils.isEmpty(loginUserPassword.getText())) {
                        userUtils.setLoginListener(this);

                        userUtils.Login(loginUserName.getText().toString().trim(),
                                loginUserPassword.getText().toString().trim());

                        if(!dialog.isShowing())
                            dialog.show();
                    }
                } else if (operation == MODE_REGISTER) {
                    if (!TextUtils.isEmpty(loginUserName.getText()) && !TextUtils.isEmpty(loginUserPassword.getText())
                            && !TextUtils.isEmpty(loginUserPhone.getText())) {

                        if(loginUserPhone.getText().toString().length() == PhoneNumber){
                            if(loginUserPassword.length() > 5 && loginUserPassword.length() < 19){
                                if(loginUserName.getText().toString().length() >= 3){
                                    userUtils.setSignUpListener(this);
                                    userUtils.signUp(isStudent,
                                            loginUserPhone.getText().toString().trim(),
                                            loginUserName.getText().toString().trim(),
                                            loginUserPassword.getText().toString().trim());
                                }else {
                                    ToastUtils.showToast(context, "用户名不能少于3个字符", Toast.LENGTH_SHORT);
                                }
                            }else {
                                ToastUtils.showToast(context, "密码长度应为6--18位", Toast.LENGTH_SHORT);
                            }
                        }else {
                            ToastUtils.showToast(context, "手机号码输入错误", Toast.LENGTH_SHORT);
                        }
                    }
                }
                break;
            case R.id.login_click_up_click_down:
                if(isOtherLoginUp){
                    translationYanimlRunDown(popupView);
                    loginCUPCD.setImageResource(R.mipmap.navibar_arrow_up);
                }else {
                    translationYanimlRunUp(popupView);
                    loginCUPCD.setImageResource(R.mipmap.navibar_arrow_down);
                }
                isOtherLoginUp = !isOtherLoginUp;
                break;

            case R.id.login_weixin:
//                //发起登陆
//                if(!mIwapi.isWXAppInstalled()){
//                    Toast.makeText(this, "请先安装微信", Toast.LENGTH_SHORT).show();
//                }else {
//                    SendAuth.Req req = new SendAuth.Req();
//                    //授权读取用户信息
//                    req.scope = "snsapi_userinfo";
//                    req.state = "Moke";
//                    mIwapi.sendReq(req);
//                }
                ToastUtils.showToast(this,"微信接入登录尚未完成",Toast.LENGTH_SHORT);
                break;
            case R.id.login_qq:
                if(!dialog.isShowing())
                    dialog.show();
                // QQ发起登陆
                LogUtils.i("1111", "QQ");
                mTencent.login(this, "all", new IUiListener() {

                    @Override
                    public void onCancel() {
                        LogUtils.i("1111", "onCancel");
                        if(dialog.isShowing())
                            dialog.dismiss();
                    }

                    @Override
                    public void onComplete(Object arg0) {
                        if(dialog.isShowing())
                            dialog.dismiss();
                        //登陆成功的回调，在此处可以获取用户信息
                        //加载dialog等颂亮
//                        MyProgressDialog. showDialogWithFalse(LoginActivity.this, "登陆", "正在获取用户信息" );
                        LogUtils.i("1111", "QQ接入登录");
                        initOpenidAndToken((JSONObject) arg0);
                    }

                    @Override
                    public void onError(UiError arg0) {
                        LogUtils.i("1111", "onError");
                        if(dialog.isShowing())
                            dialog.dismiss();
                    }

                });
                break;
            case R.id.login_xinlang:
                ToastUtils.showToast(this,"新浪接入登录尚未完成",Toast.LENGTH_SHORT);
                break;
            case R.id.login_zhifubao:
                ToastUtils.showToast(this,"支付宝接入登录尚未完成",Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }

    }

    /**
     * 切换页面文本
     *
     * @param mode 要切换为的模式
     */
    public void switchUI(int mode) {
        switch (mode) {
            case MODE_REGISTER:
                LogUtils.i(TAG, "MODE_REGISTER");
                userLL.setVisibility(View.VISIBLE);
                floatLabelPhone.setVisibility(View.VISIBLE);
                floatLabelPhone.requestFocus();
                findLostPassword.setVisibility(View.GONE);
                registerTv.setVisibility(View.GONE);
                backLogin.setVisibility(View.VISIBLE);
                userIcon.setVisibility(View.GONE);
                popupView.setVisibility(View.GONE);
                registerButton.setText("注册");
                topBar.getTvTitle().setText("注册");
                loginUserPhone.setText("");
                loginUserName.setText("");
                loginUserPassword.setText("");
                operation = MODE_REGISTER;
                break;
            case MODE_LOGIN:
                LogUtils.i(TAG, "MODE_LOGIN");
                userLL.setVisibility(View.GONE);
                floatLabelPhone.setVisibility(View.GONE);
                loginUserName.requestFocus();
                findLostPassword.setVisibility(View.VISIBLE);
                registerTv.setVisibility(View.VISIBLE);
                backLogin.setVisibility(View.GONE);
                userIcon.setVisibility(View.VISIBLE);
                popupView.setVisibility(View.VISIBLE);
                registerButton.setText("登录");
                topBar.getTvTitle().setText("登录");
                loginUserName.setText("");
                loginUserPassword.setText("");
                operation = MODE_LOGIN;
        }
    }

    @Override
    public void onLoginSuccess() {
        LogUtils.i(TAG, "onLoginSuccess");
        ToastUtils.showToast(this, "登录成功", Toast.LENGTH_SHORT);
        User user = BmobUser.getCurrentUser(context, User.class);
        user.update(getApplicationContext(), new UpdateListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
        //
        Intent intent = new Intent();
        intent.putExtra("isLogin", true);
        //给MainActivity返回数据
        setResult(RESULT_OK, intent);
        if(dialog.isShowing())
            dialog.dismiss();
        finish();
    }

    @Override
    public void onLoginFailure(String s) {
        if(dialog.isShowing())
            dialog.dismiss();
        ToastUtils.showToast(getApplicationContext(), "用户名或密码错误"+s, Toast.LENGTH_SHORT);
    }

    @Override
    public void onSignUpSuccess() {
        LogUtils.i(TAG, "onSignUpSuccess");
        switchUI(MODE_LOGIN);
        ToastUtils.showToast(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT);
        operation = MODE_LOGIN;

    }

    @Override
    public void onSignUpFailure(String s) {
        ToastUtils.showToast(getApplicationContext(), "注册失败" + s, Toast.LENGTH_SHORT);
    }

    public void onBackPressed() {
        super.onBackPressed();
        LogUtils.i(TAG, "onBackPressed");
    }

    /**
     * 隐藏软键盘
     *
     * @param e 在该控件上输出的软键盘将隐藏
     */
    private void hideSoftKeyBoard(EditText e) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(e.getWindowToken(), 0);
    }

    /**
     * 点击向上跳动
     * @param view
     */
    private void translationYanimlRunUp(View view){
        int h = otherLoginneddmiss.getHeight();
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", 0.0f, -h);
        anim.setDuration(1000); //持续时间
        anim.setInterpolator(new BounceInterpolator());
        anim.start();
    }

    /**
     * 向下跳动
     * @param view
     */
    private void translationYanimlRunDown(View view){
        int h = otherLoginneddmiss.getHeight();
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", -h, 0.0f);
        anim.setDuration(1000); //持续时间
        anim.setInterpolator(new BounceInterpolator());
        anim.start();
    }

    /**
     * @Title: initOpenidAndToken
     * @Description: 初始化OPENID以及TOKEN身份验证。
     * @param @param jsonObject
     * @return void
     * @throws
     */
    private void initOpenidAndToken (JSONObject jsonObject) {
        LogUtils.i("1111", "initOpenidAndToken");
        try {
            //这里的Constants类，是 com.tencent.connect.common.Constants类，下面的几个参数也是固定的
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN );
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN );
            //OPENID,作为唯一身份标识
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID );
            if (!TextUtils. isEmpty(token) && !TextUtils.isEmpty(expires)&& !TextUtils. isEmpty(openId)) {
                //设置身份的token
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);

            }
            if(!dialog.isShowing())
                dialog.show();
            BmobUser.loginWithAuthData(context, new BmobUser.BmobThirdUserAuth("qq", token, expires, openId), new OtherLoginListener() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    LogUtils.i(TAG, "onLoginSuccess");
                    user = BmobUser.getCurrentUser(context,User.class);
                    //非首次使用QQ登录
                    if(user.getAvatar() != null){
                        Intent intent = new Intent();
                        intent.putExtra("isLogin", true);
                        //给MainActivity返回数据
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    //首次使用QQ登录
                    updateUserInfo();

                }

                @Override
                public void onFailure(int i, String s) {
                    if(dialog.isShowing())
                        dialog.dismiss();
                    ToastUtils.showToast(context, "登录失败", Toast.LENGTH_SHORT);

                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * @Title: updateUserInfo
     * @Description: 在回调里面可以获取用户信息数据了
     * @param
     * @return void
     * @throws
     */
    private void updateUserInfo() {
        LogUtils.i("1111", "updateUserInfo");
        if ( mTencent != null && mTencent.isSessionValid()) {
            UserInfo mInfo = new UserInfo(LoginActivity. this,  mTencent.getQQToken());
            listener = new IUiListener() {

                @Override
                public void onError(UiError e) {
                    if(dialog.isShowing())
                        dialog.dismiss();
                }

                // 用户的信息回调在此处
                @Override
                public void onComplete( final Object response) {
                    LogUtils.i("1111", "onComplete");
                    // 返回Bitmap对象。
                    try {
                        JSONObject obj = new JSONObject(response.toString());
                        user = BmobUser.getCurrentUser(context,User.class);
                        user.setUsername(obj.optString( "nickname"));
                        //判断用户数据库是否为空（为空即用户第一次登陆）
                        if(user.getAvatar() == null){
                            download(obj.optString( "figureurl_qq_2"));
                            LogUtils.i("1111", "download   figureurl_qq_2");
                        }
                        if(user.getSex() == null){
                            if(obj.optString( "gender").equals("男")){
                                user.setSex(true);
                            }else {
                                user.setSex(false);
                            }
                        }
                        LogUtils.i("1111", obj.optString( "figureurl_qq_2"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtils.showToast(context, "登录失败", Toast.LENGTH_SHORT);
                    }

                }

                @Override
                public void onCancel() {
                    if(dialog.isShowing())
                        dialog.dismiss();
                }
            };

            mInfo.getUserInfo(listener);

        }
    }

    private void download(String path) {

        //Target
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                String imageName = "avatarByQQ.jpg";

                //  获取当前程序路径
                String save_path = getApplicationContext().getFilesDir().getAbsolutePath() + "/avatar/";
                File file = new File(save_path);
                if(!file.exists()){
                    file.mkdir();
                }

                File mFile = new File(save_path, imageName);

                FileOutputStream ostream = null;
                try {
                    ostream = new FileOutputStream(mFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //上传头像
                final BmobFile userAvatarFile = new BmobFile(mFile);
                user.setAvatar(userAvatarFile);
                userAvatarFile.upload(LoginActivity.this, new UploadFileListener() {
                    @Override
                    public void onSuccess() {
                        user.update(LoginActivity.this, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                LogUtils.i("1111", "更新用户数据成功");
                                if(dialog.isShowing())
                                    dialog.dismiss();
                                Intent intent = new Intent();
                                intent.putExtra("isLogin", true);
                                //给MainActivity返回数据
                                setResult(RESULT_OK, intent);
                                finish();

                            }

                            @Override
                            public void onFailure(int i, String s) {
                                LogUtils.i("1111", s);
                                if(dialog.isShowing())
                                    dialog.dismiss();

                            }
                        });
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        LogUtils.i("1111", s);
                        if(dialog.isShowing())
                            dialog.dismiss();
                        ToastUtils.showToast(context, s, Toast.LENGTH_SHORT);
                    }
                });

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }



            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        //Picasso下载
        Picasso.with(this).load(path).into(target);

    }



    //动态监听输入过程
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (operation) {
                case MODE_REGISTER:
                    if (!TextUtils.isEmpty(loginUserName.getText()) && !TextUtils.isEmpty(loginUserPassword.getText())
                            && !TextUtils.isEmpty(loginUserPhone.getText())) {
                        registerButton.setEnabled(true);
                        registerButton.setAlpha(1.0f);
                    } else {
                        registerButton.setEnabled(false);
                        registerButton.setAlpha(0.3f);
                    }
                    break;
                case MODE_LOGIN:
                    if (!TextUtils.isEmpty(loginUserName.getText()) && !TextUtils.isEmpty(loginUserPassword.getText())) {
                        registerButton.setEnabled(true);
                        registerButton.setAlpha(1.0f);
                    } else {
                        registerButton.setEnabled(false);
                        registerButton.setAlpha(0.3f);
                    }
                    break;
                default:
                    break;
            }


            //动态加载用户头像
            if(loginUserName.getText().length() >= 3 && loginUserName.getText().toString().length() < 10){

                //通过用户名获取用户头像
                BmobQuery<User> query = new BmobQuery<User>();
                query.addWhereEqualTo("username", loginUserName.getText().toString().trim());
                query.findObjects(context, new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> list) {
                        if(list.size() != 0){
                            if(list.get(0).getAvatar() != null){
                                String uri = list.get(0).getAvatar().getFileUrl(context);
                                Picasso.with(context)
                                        .load(uri)
                                        .centerCrop()
                                        .resize(72,72)
                                        .placeholder(R.mipmap.default_user_avatar)
                                        .into(userIcon);
                            }
                        }else {
                            userIcon.setImageResource(R.mipmap.default_user_avatar);
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });

            }else {
                userIcon.setImageResource(R.mipmap.default_user_avatar);
            }

        }
    }
}
