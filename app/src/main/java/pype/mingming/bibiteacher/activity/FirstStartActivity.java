package pype.mingming.bibiteacher.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.fragment.FirstStartFragment;
import pype.mingming.bibiteacher.utils.ActivityUtils;

/**
 * 安装该软件后首次启动的画面
 * Created by mingming on 2016/8/1.
 */
public class FirstStartActivity extends BaseActivity {

    //页数
    static final int NUM_PAGES = 4;

    ViewPager pager;
    PagerAdapter pagerAdapter;
    //指示器布局
    LinearLayout circles;
    Button skip;
    ImageButton next;
    boolean isOpaque = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_first_start;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        skip = (Button) findViewById(R.id.first_start_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTutorial();
            }
        });

        next = (ImageButton) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            }
        });


        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        //设置适配器
        pager.setAdapter(pagerAdapter);
        //设置滑动效果（自定义）
        pager.setPageTransformer(true, new CrossfadePageTransformer());
        //页面改变监听器
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == NUM_PAGES - 1 && positionOffset > 0) {
                    if (isOpaque) {
                        pager.setBackgroundColor(Color.TRANSPARENT);
                        isOpaque = false;
                    }
                } else {
                    if (!isOpaque) {
                        pager.setBackgroundColor(getResources().getColor(R.color.primary_material_light));
                        isOpaque = true;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                //设置指示器的点
                setIndicator(position);
                if (position == NUM_PAGES - 1) {
                    skip.setVisibility(View.VISIBLE);
                    next.setVisibility(View.INVISIBLE);
                } else if (position < NUM_PAGES - 1) {
                    skip.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //创建ViewPager指示点
        buildCircles();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(pager!=null){
            //清除监听器
            pager.clearOnPageChangeListeners();
        }
    }

    /**
     * 创建指示器
     */
    private void buildCircles(){
        //找布局
        circles = LinearLayout.class.cast(findViewById(R.id.circles));

        //这是获取手机屏幕参数,后面的density就是屏幕的密度
        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (5 * scale + 0.5f);

        for(int i = 0 ; i < NUM_PAGES; i++){
            ImageView circle = new ImageView(this);
            circle.setImageResource(R.mipmap.ic_swipe_indicator_white_18dp);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            circles.addView(circle);
        }

        setIndicator(0);
    }

    /**
     * 设置指示点显示位置
     * @param index
     */
    private void setIndicator(int index){
        if(index < NUM_PAGES){
            for(int i = 0 ; i < NUM_PAGES ; i++){
                ImageView circle = (ImageView) circles.getChildAt(i);
                if(i == index){
                    circle.setColorFilter(getResources().getColor(R.color.gary2));
                }else {
                    circle.setColorFilter(getResources().getColor(android.R.color.transparent));
                }
            }
        }
    }

    /**
     * 跳过并启动主页
     */
    private void endTutorial(){
        //第一次启动
        SharedPreferences preferences = context.getSharedPreferences("isFirstStart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstStart", false);
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
            ActivityUtils.finishAll();

        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    /**
     * 自定义适配器，根据getItem(int position)的 position 动态加载Fragment
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            FirstStartFragment tp = null;
            switch(position){
                case 0:
                    tp = FirstStartFragment.newInstance(R.layout.first_start_fragment1);
                    break;
                case 1:
                    tp = FirstStartFragment.newInstance(R.layout.first_start_fragment2);
                    break;
                case 2:
                    tp = FirstStartFragment.newInstance(R.layout.first_start_fragment3);
                    break;
                case 3:
                    tp = FirstStartFragment.newInstance(R.layout.first_start_fragment4);
                    break;
            }

            return tp;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


    /**
     * 自定义页面滚动动画
     */
    public class CrossfadePageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();

            View backgroundView = page.findViewById(R.id.welcome_fragment);
            View text_head= page.findViewById(R.id.heading);
            View text_content = page.findViewById(R.id.content);
            View object1 = page.findViewById(R.id.a000);
            View object2 = page.findViewById(R.id.a001);

            View object3 = page.findViewById(R.id.a002);
            View object4 = page.findViewById(R.id.a003);
            View object5 = page.findViewById(R.id.a004);
            View object6 = page.findViewById(R.id.a005);
            View object7 = page.findViewById(R.id.a006);
            View object8 = page.findViewById(R.id.a008);
            View object9 = page.findViewById(R.id.a010);
            View object10 = page.findViewById(R.id.a011);
            View object11 = page.findViewById(R.id.a007);
            View object12 = page.findViewById(R.id.a012);
            View object13 = page.findViewById(R.id.a013);

            if(0 <= position && position < 1){
                //setTranslationX改变了view的位置，但没有改变view的LayoutParams里的margin属性；
                //它改变的是android:translationX 属性，也即这个参数级别是和margin平行的
                ViewHelper.setTranslationX(page,pageWidth * -position);
            }
            if(-1 < position && position < 0){
                ViewHelper.setTranslationX(page,pageWidth * -position);
            }

            if(position <= -1.0f || position >= 1.0f) {
            } else if( position == 0.0f ) {
            } else {
                if(backgroundView != null) {
                    ViewHelper.setAlpha(backgroundView,1.0f - Math.abs(position));

                }

                if (text_head != null) {
                    ViewHelper.setTranslationX(text_head,pageWidth * position);
                    ViewHelper.setAlpha(text_head,1.0f - Math.abs(position));
                }

                if (text_content != null) {
                    ViewHelper.setTranslationX(text_content,pageWidth * position);
                    ViewHelper.setAlpha(text_content,1.0f - Math.abs(position));
                }

                if (object1 != null) {
                    ViewHelper.setTranslationX(object1,pageWidth * position);
                }

                // parallax effect
                if(object2 != null){
                    ViewHelper.setTranslationX(object2,pageWidth * position);
                }

                if(object4 != null){
                    ViewHelper.setTranslationX(object4,pageWidth/2 * position);
                }
                if(object5 != null){
                    ViewHelper.setTranslationX(object5,pageWidth/2 * position);
                }
                if(object6 != null){
                    ViewHelper.setTranslationX(object6,pageWidth/2 * position);
                }
                if(object7 != null){
                    ViewHelper.setTranslationX(object7,pageWidth/2 * position);
                }

                if(object8 != null){
                    ViewHelper.setTranslationX(object8,(float)(pageWidth/1.5 * position));
                }

                if(object9 != null){
                    ViewHelper.setTranslationX(object9,(float)(pageWidth/2 * position));
                }

                if(object10 != null){
                    ViewHelper.setTranslationX(object10,pageWidth/2 * position);
                }

                if(object11 != null){
                    ViewHelper.setTranslationX(object11,(float)(pageWidth/1.2 * position));
                }

                if(object12 != null){
                    ViewHelper.setTranslationX(object12,(float)(pageWidth/1.3 * position));
                }

                if(object13 != null){
                    ViewHelper.setTranslationX(object13,(float)(pageWidth/1.8 * position));
                }

                if(object3 != null){
                    ViewHelper.setTranslationX(object3,(float)(pageWidth/1.2 * position));
                }
            }
        }
    }
}
