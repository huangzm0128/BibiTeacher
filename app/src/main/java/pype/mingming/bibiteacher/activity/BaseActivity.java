package pype.mingming.bibiteacher.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.sdk.openapi.IWXAPI;

import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.db.SharePreferencesHelper;
import pype.mingming.bibiteacher.utils.ActivityUtils;
import pype.mingming.bibiteacher.utils.LogUtils;


/**
 * activity基类
 * Created by mingming on 2016/6/10.
 */
public abstract class BaseActivity extends AppCompatActivity{
    public Context context;
    public static IWXAPI mIwapi; //IWXAPI 是第三方app和微信通信的openapi接口

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        //设置主题
        //现在我没有自定义主题，所以用默认主题
        //多个主题的话要判断要用哪个主题，现在我这if else语句是只有白天和夜间两种主题的写法
        //并且夜间主题的配色还没有自定义，所以用的是系统的默认主题
        if(!SharePreferencesHelper.getTheme(this)){
            setTheme(R.style.AppTheme);
        }else {
            setTheme(R.style.AppTheme);
        }

        //this.getApplicationContext()取得的是整个应用的上下文
        context = getApplicationContext();

        setWindow();
        //设置该Activity的布局
        setContentView(getLayoutId());
        //使用ButterKnife注解
        ButterKnife.bind(this);
        //第一：默认初始化
        Bmob.initialize(this, "3e30cd20ea8b71c5a19ab0dcef1f9cdd");
        initDada();
        setListener();
        LogUtils.i("BaseActivity", getClass().getSimpleName());
        ActivityUtils.addActivity(this);
    }

    /**
     * 全屏
     */
    protected void setWindow() {
    }

    /*
    获得布局id
     */
    protected abstract int getLayoutId();

    /*
    初始化数据
     */
    protected  void initDada(){}

    /*
    设置监听器
     */
    protected  void setListener(){}

    /**
     * 开启另一个Activity
     * @param cls 要开启的Activity
     * @param finished 是否结束当前Activity
     */
    protected void switchActivity(Class cls, boolean finished){
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        if(finished){
            this.finish();
        }
    }


    /**
     * 开启Fragment
     * @param from 从哪个Fragment开启
     * @param to 转到哪个Fragment
     * @param id Fragment to的id
     * @param tab String: Optional tag name for the fragment, to later retrieve the fragment with FragmentManager.findFragmentByTag(String).

     */
    protected void switchFragment(Fragment from, Fragment to, int id, String tab){
        if(to == null){
            return;
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if(from == null){
            transaction.add(id, to, tab);
        }else {
            transaction.hide(from);
            if(to.isAdded()){
                transaction.show(to);
            }else {
                transaction.add(id, to, tab);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.removeActivity(this);
    }
}
