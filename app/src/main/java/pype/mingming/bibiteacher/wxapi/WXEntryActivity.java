package pype.mingming.bibiteacher.wxapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.datatype.BmobFile;
import pype.mingming.bibiteacher.activity.BaseActivity;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.utils.ActivityUtils;
import pype.mingming.bibiteacher.utils.HttpCallbackListener;
import pype.mingming.bibiteacher.utils.HttpUtil;
import pype.mingming.bibiteacher.utils.LogUtils;
import pype.mingming.bibiteacher.utils.ToastUtils;
import pype.mingming.bibiteacher.utils.UserUtils;

/**
 * 接入微信登录类
 * Created by mingming on 2016/9/21.
 */

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private static final String WECHAT_APP_ID = "wxe402a8176c32bf70";
    private static final String WECHAT_APP_SECRET = "4be421ac32d66e91b7dbd87f6741e00c";
    // 请求access_token地址格式，要替换里面的APPID，SECRET还有CODE
    public static String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?"
            + "appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    // 请求unionid地址格式，要替换里面的ACCESS_TOKEN和OPENID
    public static String GetUnionIDRequest = "https://api.weixin.qq.com/sns/userinfo?"
            + "access_token=ACCESS_TOKEN&openid=OPENID";

    private String newGetCodeRequest = "";
    private String newGetUnionIDRequest = "";
    private String mOpenId = "";
    private String mAccess_token = "";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    //这个实体类是我自定义的实体类，用来保存第三方的数据的实体类
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.addActivity(WXEntryActivity.this);
        BaseActivity.mIwapi.handleIntent(getIntent(), WXEntryActivity.this);  //必须调用此句话

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        BaseActivity.mIwapi.handleIntent(intent, WXEntryActivity.this);//必须调用此句话
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    /**
     * 发送到微信请求的响应结果
     * <p>
     * （1）用户同意授权后得到微信返回的一个code，将code替换到请求地址GetCodeRequest里的CODE，同样替换APPID和SECRET
     * （2）将新地址newGetCodeRequest通过HttpClient去请求，解析返回的JSON数据
     * （3）通过解析JSON得到里面的openid （用于获取用户个人信息）还有 access_token
     * （4）同样地，将openid和access_token替换到GetUnionIDRequest请求个人信息的地址里
     * （5）将新地址newGetUnionIDRequest通过HttpUtil去请求，解析返回的JSON数据
     * （6）通过解析JSON得到该用户的个人信息，包括unionid
     */
    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == 2) {
            finish();
        }
        if (baseResp.getType() == 1) {
            switch (baseResp.errCode) {
                // 同意授权
                case BaseResp.ErrCode.ERR_OK:

//                    SendAuth.Resp respLogin = (SendAuth.Resp) baseResp;
//                    // 获得code
////                        String code = respLogin.code;
//                    String code = respLogin.token;
//
//                    // 把code，APPID，APPSECRET替换到要请求的地址里，成为新的请求地址
//                    newGetCodeRequest = getCodeRequest(code);
//                    // 请求新的地址，解析相关数据，包括openid，acces_token等
//                    HttpUtil.sendHttpRequest(newGetCodeRequest, new HttpCallbackListener() {
//                        @Override
//                        public void onFinish(String response) {
//                            // Log.d("WXActivity", response);
//                            parseAccessTokenJSON(response);
//                            // 将解析得到的access_token和openid在请求unionid地址里替换
//                            newGetUnionIDRequest = getUnionID(mAccess_token, mOpenId);
//                            // 请求新的unionid地址，解析出返回的unionid等数据
//                            HttpUtil.sendHttpRequest(newGetUnionIDRequest, new HttpCallbackListener() {
//                                @Override
//                                public void onFinish(String response) {
//                                    parseUnionIdJson(response);
//                                }
//
//                                @Override
//                                public void onError(Exception e) {
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                        }
//                    });
//                    Timer timer = new Timer();
//                    TimerTask task = new TimerTask() {
//                        @Override
//                        public void run() {
//                            WXEntryActivity.this.finish();
//                        }
//                    };
//                    timer.schedule(task, 2000);
                    break;
                // 拒绝授权
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    finish();
                    break;
                // 取消操作
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    finish();
                    break;
                default:
                    break;
            }
        }

    }


    /**
     * * 替换GetCodeRequest 将APP ID，APP SECRET，code替换到链接里 * * @param code *
     * 授权时，微信回调给的 * @return URL
     */
    public static String getCodeRequest(String code) {
        String result = null;
        GetCodeRequest = GetCodeRequest.replace("APPID", urlEnodeUTF8(WECHAT_APP_ID));
        GetCodeRequest = GetCodeRequest.replace("SECRET", urlEnodeUTF8(WECHAT_APP_SECRET));
        GetCodeRequest = GetCodeRequest.replace("CODE", urlEnodeUTF8(code));
        result = GetCodeRequest;
        return result;
    }

    /**
     * * 替换GetUnionID * * @param access_token * @param open_id * @return
     */
    public static String getUnionID(String access_token, String open_id) {
        String result = null;
        GetUnionIDRequest = GetUnionIDRequest.replace("ACCESS_TOKEN", urlEnodeUTF8(access_token));
        GetUnionIDRequest = GetUnionIDRequest.replace("OPENID", urlEnodeUTF8(open_id));
        result = GetUnionIDRequest;
        return result;
    }

    public static String urlEnodeUTF8(String code) {
        String result = code;
        try {
            result = URLEncoder.encode(code, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * * 解析access_token返回的JSON数据 * * @param response
     */
    private void parseAccessTokenJSON(String response) {
        // TODO Auto-generated method stubtry
        {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                mAccess_token = jsonObject.getString("access_token");
                String expiresIn = jsonObject.getString("expires_in");
                String refreshToken = jsonObject.getString("refresh_token");
                mOpenId = jsonObject.getString("openid");
                String scope = jsonObject.getString("scope");
                //将获取到的数据写进SharedPreferences里
                editor.putString("access_token", mAccess_token);
                editor.putString("expires_in", expiresIn);
                editor.putString("refresh_token", refreshToken);
                editor.putString("openid", mOpenId);
                editor.putString("scope", scope);
                editor.commit();
                LogUtils.d("WXActivity", "access_token is " + mAccess_token);
                LogUtils.d("WXActivity", "expires_in is " + expiresIn);
                LogUtils.d("WXActivity", "refresh_token is " + refreshToken);
                LogUtils.d("WXActivity", "openid is " + mOpenId);
                LogUtils.d("WXActivity", "scope is " + scope);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * * 解析unionid数据 * @param response
     */
    private void parseUnionIdJson(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            String openid = jsonObject.getString("openid");
            String nickname = jsonObject.getString("nickname");
            String sex = jsonObject.getString("sex");
            String province = jsonObject.getString("province");
            String city = jsonObject.getString("city");
            String country = jsonObject.getString("country");
            String headimgurl = jsonObject.getString("headimgurl");
            String unionid = jsonObject.getString("unionid");

            user = new User();
            user.setUsername(nickname);
            if(sex.equals("男")){
                user.setSex(true);
            }else {
                user.setSex(false);
            }
            BmobFile avatar = new BmobFile(new File(headimgurl));
            user.setAvatar(avatar);
            LogUtils.d("WXActivity ", " openid is " + openid);
            LogUtils.d("WXActivity", "nickname is " + nickname);
            LogUtils.d("WXActivity", "sex is " + sex);
            LogUtils.d("WXActivity", "province is " + province);
            LogUtils.d("WXActivity", "city is " + city);
            LogUtils.d("WXActivity", "country is " + country);
            LogUtils.d("WXActivity", "headimgurl is " + headimgurl);
            LogUtils.d("WXActivity", "unionid is " + unionid);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
