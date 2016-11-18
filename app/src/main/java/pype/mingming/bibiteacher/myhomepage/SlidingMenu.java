package pype.mingming.bibiteacher.myhomepage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by mk on 2016/7/26.
 */
public class SlidingMenu extends ScrollView{
    private LinearLayout mWapper;
    //菜单View
    private ViewGroup mMenu;
    //内容View
    private ViewGroup mContent;
    //屏幕高度和宽度
    private int mScreenHeight,mScreenWidth;
    //菜单屏幕上部距离，单位为dp
    private int mMenuTopPadding =250;
    private boolean once;
    //菜单的高度
    private int mMenuHeight;

    public SlidingMenu(Context context) {
        super(context);
    }
    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取屏幕高度和宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        mScreenWidth = outMetrics.widthPixels;
        //将dp值转化为px
        mMenuTopPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,250,
                context.getResources().getDisplayMetrics());

    }
    /**
     * 设置自己的宽和高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!once){
            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(1);
            mContent = (ViewGroup) mWapper.getChildAt(0);
            mMenu.getLayoutParams().height = mScreenHeight;
            mMenuHeight = mScreenHeight - mMenuTopPadding;
            mMenu.getLayoutParams().width = mScreenWidth;
            mContent.getLayoutParams().height = mScreenHeight-120;//内容View的初始显示高度
            mContent.getLayoutParams().width = mScreenWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 隐藏菜单
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed){
            this.scrollTo(0,-mMenuHeight);
        }
    }

    /**
     * 菜单项的滑动显示和内容的滑动显示
     * @param ev
     * @return
     */
    /*@Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_UP:
                //隐藏菜单和显示菜单,随着用户的滑动距离来决定
                int scroll = getScrollY();
                if(scroll<mScreenHeight/4){
                    this.scrollTo(0,600);
                }
                else if ((scroll>mScreenHeight/3)&&(scroll<mScreenHeight/2)){
                    this.smoothScrollTo(0,mMenuHeight);
                }
                else if (scroll>mScreenHeight/2){
                    this.smoothScrollTo(0,mScreenHeight-100);//菜单View全屏显示
                    myTopBar.setTitleText("我的资料");
                    myTopBar.setMyTopBackGroud(0xFFF59563);

                }
                else {
                    this.smoothScrollTo(0,-mMenuHeight);

                }

                return true;
        }
        return super.onTouchEvent(ev);

    }*/
}
