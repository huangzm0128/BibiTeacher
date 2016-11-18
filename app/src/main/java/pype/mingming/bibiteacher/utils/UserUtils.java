package pype.mingming.bibiteacher.utils;

import android.content.Context;

import cn.bmob.v3.listener.SaveListener;
import pype.mingming.bibiteacher.entity.User;


/**
 * bomb用户登录注册工具类
 *
 * 用户注册或登录时所用到的工具类
 * 因为等你注册或登陆的操作都需要把数据上传到后台服务器上
 * 这是耗时操作，所以需要用接口回调的方式处理注册或登录成功or失败时的操作
 * （因为你不能让用户干等着你登录或注册所耗费的时间，所以需要接口回调）
 *
 * 接口回调理解例子：
 * 大家都喜欢用打电话的例子，好吧，为了跟上时代，我也用这个例子好了，我这个例子采用异步加回调
 有一天小王遇到一个很难的问题，问题是“1 + 1 = ?”，就打电话问小李，小李一下子也不知道，
 就跟小王说，等我办完手上的事情，就去想想答案，小王也不会傻傻的拿着电话去等小李的答案吧，
 于是小王就对小李说，我还要去逛街，你知道了答案就打我电话告诉我，于是挂了电话，
 自己办自己的事情，过了一个小时，小李打了小王的电话，告诉他答案是2
 * Created by mingming on 2016/6/28.
 */
public class UserUtils {
    private static final String TAG = "UserUtils";

    private Context mContext;

    private SignUpListener mSignUpListener;

    private LoginListener mLoginListener;


    public UserUtils(Context context){
        this.mContext = context;
    }

    public void setSignUpListener(SignUpListener signUpListener){
        this.mSignUpListener = signUpListener;
    }

    public void setLoginListener(LoginListener loginListener){
        this.mLoginListener = loginListener;
    }

    /**
     * 注册时将所填信息上传到bmob中，并利用自定义接口回调
     * 执行注册成功或失败时的行为
     * @param userPhone 手机号码
     * @param userName 用户名
     * @param password 密码
     */
    public void signUp(boolean isStudent, String userPhone, String userName, String password){
        User user = new User();
        user.setIsStudent(isStudent);
        user.setMobilePhoneNumber(userPhone);
        user.setUsername(userName);
        user.setPassword(password);

        user.signUp(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                mSignUpListener.onSignUpSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                mSignUpListener.onSignUpFailure(s);
            }
        });
    }


    public void Login(String userName, String password){
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);

        user.login(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                mLoginListener.onLoginSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                mLoginListener.onLoginFailure(s);
            }
        });
    }


    public interface SignUpListener{
        void onSignUpSuccess();
        void onSignUpFailure(String s);
    }

    public interface LoginListener{
        void onLoginSuccess();
        void onLoginFailure(String s);
    }


}
