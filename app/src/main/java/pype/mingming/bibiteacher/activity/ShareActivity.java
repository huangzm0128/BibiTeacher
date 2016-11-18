package pype.mingming.bibiteacher.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pype.mingming.bibiteacher.Constant;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.utils.LogUtils;
import pype.mingming.bibiteacher.utils.ToastUtils;

/**
 * Created by mingming on 2016/10/13.
 */

public class ShareActivity extends BaseActivity {
    @BindView(R.id.myTopBar)
    TopBar myTopBar;
    @BindView(R.id.share_weixin)
    LinearLayout shareWeixin;
    @BindView(R.id.share_weixin_friends)
    LinearLayout shareWeixinFriends;
    @BindView(R.id.share_qq)
    LinearLayout shareQq;

    private static  final String  APP_ID = "wxe402a8176c32bf70";
    private Tencent mTencent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share;
    }

    @Override
    protected void initDada() {
        super.initDada();

        //微信api注册
        mIwapi = WXAPIFactory.createWXAPI(this, APP_ID, true);
        mIwapi.registerApp(APP_ID);
        //初始化Tencent
        mTencent = Tencent.createInstance(Constant.QQ_APPID, this.getApplicationContext());

        myTopBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        myTopBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        myTopBar.getRightButton().setVisibility(View.GONE);
    }

    @Override
    protected void setListener() {
        super.setListener();
        myTopBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                onBackPressed();
            }

            @Override
            public void rightButtonOnClick() {

            }
        });
    }

    @OnClick({R.id.share_weixin, R.id.share_weixin_friends, R.id.share_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_weixin:
                wechatShare(0);
                break;
            case R.id.share_weixin_friends:
                wechatShare(1);
                break;
            case R.id.share_qq:
                shareQQ();
                break;
        }
    }


    public void shareQQ()
    {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "大学生家教专用APP来啦！！！！");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "做专业的家教平台");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://www.qq.com/news/1.html"); //点击跳转地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://a2.qpic.cn/psb?/V12V2qgz1c06s3/2PYCVgYKGHA7k330xqZrskrYY.OBrz*TXAJz3LGkbu8!/b/dAwBAAAAAAAA&bo=bABqAAAAAAADByQ!&rf=viewer_4");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "Bibi家教");
        mTencent.shareToQQ(ShareActivity.this, params, new BaseUiListener());
    }


    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     * @param flag(0:分享到微信好友，1：分享到微信朋友圈)
     */
    private void wechatShare(int flag){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "baidu.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "大学生家教专用APP来啦";
        msg.description = "做专业的家教平台";
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_bibiteacher_night);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        mIwapi.sendReq(req);
    }




    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
                //V2.0版本，参数类型由JSONObject 改成了Object,具体类型参考api文档
                doComplete((JSONObject)response);
    }
    protected void doComplete(JSONObject values) {
        //分享成功
    }
    @Override
    public void onError(UiError e) {
        //在这里处理错误信息
        if(e.errorDetail == null){
            ToastUtils.showToast(ShareActivity.this,"出错了", Toast.LENGTH_SHORT);
        }else{
            ToastUtils.showToast(ShareActivity.this,
                    "出错了"+":"+e.errorDetail, Toast.LENGTH_SHORT);
        }

    }
    @Override
    public void onCancel() {
        //分享被取消
        ToastUtils.showToast(ShareActivity.this,"分享被取消",Toast.LENGTH_SHORT);
    }
}
}
