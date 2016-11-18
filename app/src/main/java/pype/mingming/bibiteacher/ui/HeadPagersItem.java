package pype.mingming.bibiteacher.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.HeadPagersItemBean;
import pype.mingming.bibiteacher.utils.LogUtils;
import pype.mingming.bibiteacher.utils.ToastUtils;

/**
 * 首页中标题栏下的位置的图片滚动焦点图
 * Created by mingming on 2016/7/24.
 */
public class HeadPagersItem extends FrameLayout implements ViewPager.OnPageChangeListener{
    private final String TAG = HeadPagersItem.class.getSimpleName();

    private final int MIN_WINDOW_HEIGHT = 1;
    private final float RATE = 0.20f;

    /**
     * 上下文
     */
    private Context context;
    /**
     * 分页容器
     */
    private ViewPager pagerView;

    /**
     * 下标
     */
    private LPagePointView pointView;
    /**
     * 数据bean
     */
    private HeadPagersItemBean bean;
    /**
     * 图片数组
     */
    private ImageView[] viewList;
    /**
     * 图片点击事件
     */
    private OnPageClickListener clickListener;
    /**
     * 根结点
     */
    private FrameLayout root;

    private int windowHeight = 0;

    private void initView(){
        //把head_pagers_item.xml填充到该context中
        LayoutInflater.from(context).inflate(R.layout.head_pagers_item, this,true);
        root = (FrameLayout) findViewById(R.id.head_pagers_item_root);
        pagerView = (ViewPager) findViewById(R.id.head_pagers_item_pager);
        pointView = (LPagePointView) findViewById(R.id.head_pager_item_point);
        if(windowHeight < MIN_WINDOW_HEIGHT){
            WindowManager m = ((Activity)context).getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高
            Point point = new Point();
            d.getSize(point); //将屏幕高宽赋值给Point
            windowHeight = point.y;
        }
        ViewGroup.LayoutParams p = root.getLayoutParams(); //// getWindow().getAttributes();
        p.height = (int) (windowHeight * RATE);
        root.setLayoutParams(p);
//        imageLoader = ImageLoader.getInstance(); 改用Glide
        dateSet();

        //开始图片循环滚动
        startImageTimerTask();
    }

    private void dateSet() {
        if(bean == null || bean.getImgUrlSize() == 0)
            return;
        if(pointView == null || pagerView == null)
            return;
        //设置下标点的数量
        pointView.setPointSize(bean.getImgUrlSize());
        viewList = new ImageView[bean.getImgUrlSize()];
        //线性布局参数属性
        LinearLayout.LayoutParams imgParams;
        imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        for(int i=0; i<viewList.length; i++){
            ImageView imageView = new ImageView(context);
            //设置线性布局参数属性
            imageView.setLayoutParams(imgParams);
            //设置图片的大小为中间切割
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //设置点击图片的监听器
            imageView.setOnClickListener(new OnImageListener(i));
            //赋值给viewList图片数组
            viewList[i] = imageView;
            //图片加载开源工具Glide,具体用法请百度
            Glide.with(context)
                    .load(bean.getImgUrl(i))
                    .asBitmap()
                    .placeholder(R.drawable.test_head_pagers_item)
                    .into(viewList[i]);
        }
        //设置分页容器ViewPager的对象的监听器
        pagerView.addOnPageChangeListener(this);
        //设置分页容器ViewPager的对象的适配器
        pagerView.setAdapter(pagerAdapter);
    }

    //新建分页容器ViewPager的对象的适配器
    private PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return viewList.length;
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList[position]);
        }
        /**
         * 载入图片进去
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = viewList[position];
            container.addView(view, 0);
            return view;
        }

    };


    /**
     * 图片滚动任务
     */
    private void startImageTimerTask() {
        stopImageTimerTask();
        // 图片滚动
        mHandler.postDelayed(mImageTimerTask, 3000);
    }

    /**
     * 停止图片滚动任务
     */
    private void stopImageTimerTask() {
        mHandler.removeCallbacks(mImageTimerTask);
    }

    private Handler mHandler = new Handler();

    /**
     * 图片自动轮播Task
     */
    private Runnable mImageTimerTask = new Runnable() {
        @Override
        public void run() {
            if (viewList != null) {
                pagerView.setCurrentItem((pagerView.getCurrentItem()+1) % viewList.length);
            }
        }
    };


    public HeadPagersItem(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public HeadPagersItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public HeadPagersItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }
    public HeadPagersItem(Context context,HeadPagersItemBean bean,OnPageClickListener listener) {
        super(context);
        this.bean = bean;
        this.clickListener = listener;
        this.context = context;
        initView();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        pointView.onChange(position,positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE)
            //当viewPager发生滚动时，开启自动滚动
            startImageTimerTask();
    }

    /**
     * 点击事件
     */
    public interface OnPageClickListener{
        void onPageClick(View view, int index);
    }

    private class OnImageListener implements OnClickListener {
        private int index;
        public OnImageListener(int index){
            super();
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null){
                //通过OnImageListener来确定onPageClick的具体页面
                clickListener.onPageClick(HeadPagersItem.this, index);
            }

        }
    }

    public void setBean(HeadPagersItemBean bean){
        this.bean = bean;
        dateSet();
    }
    public HeadPagersItemBean getBean(){return bean;}

    public OnPageClickListener getClickListener() {
        return clickListener;
    }
    public void setClickListener(OnPageClickListener clickListener) {
        this.clickListener = clickListener;
    }

}
