package pype.mingming.bibiteacher.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;

import butterknife.BindView;
import butterknife.OnClick;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.ui.BiAnimView;


/**
 * 启动该软件的动画Activity
 * Created by mingming on 2016/6/15.
 */
public class SplashActivity extends BaseActivity {
    private boolean isSkip = false;
    @BindView(R.id.iv_my_wc)
    KenBurnsView welcomeImg;
    @BindView(R.id.animl_bi_view)
    BiAnimView circleAnimView;
    @BindView(R.id.app_tv)
    TextView appTv;
    private Handler mHandler=new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initDada() {
        super.initDada();

    }

    @Override
    protected void setListener() {
        super.setListener();
        circleAnimView.setCallback(new BiAnimView.MyCallback(){
            @Override
            public void revealEnd() {
                //当点击跳过后，不再执行动画完成时的页面自动跳转
                if(!isSkip){
//                    appTv.setVisibility(View.VISIBLE);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            isFirstStart();
                        }
                    });
                }
            }
        });

        welcomeImg.setTransitionListener(new KenBurnsView.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                //动画结束后跳转
            }
        });


    }

    private void animAppText(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(appTv, "alpha", 1f, 0f, 1f);
        //球运动时间
        animator.setDuration(3000);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                appTv.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        welcomeImg.resume();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animAppText();
            }
        }, 2000L);
    }

    @Override
    protected void onPause() {
        super.onPause();
        welcomeImg.pause();
    }

    @Override
    public void onBackPressed(){
       onDestroy();
    }


    private void isFirstStart(){
        boolean isFirstStart = false;
        SharedPreferences preferences = context.getSharedPreferences("isFirstStart",MODE_PRIVATE);
        isFirstStart = preferences.getBoolean("isFirstStart", true);
        if(isFirstStart){
            switchActivity(FirstStartActivity.class,true);
        }else {
            switchActivity(MainActivity.class, true);
        }
    }

    @OnClick(R.id.skip_tv) void skip(){
        isSkip = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                isFirstStart();
            }
        });
    }
}
