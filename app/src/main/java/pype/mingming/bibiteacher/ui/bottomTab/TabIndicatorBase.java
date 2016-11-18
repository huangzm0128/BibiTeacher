package pype.mingming.bibiteacher.ui.bottomTab;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import pype.mingming.bibiteacher.R;

/**
 *底部导航基类
 * 继承 底部导航条目操作基类， LinearLayout
 * Created by mingming on 2016/7/27.
 */
public abstract class TabIndicatorBase<T extends TabViewBase> extends LinearLayout {

    /**
     *  底部菜单padding
     */
    protected int mTabPadding;

    /**
     * 指示点大小
     */
    private int mIndicatorSize;

    /**
     *  存放底部菜单
     */
    protected List<T> mCheckedList = new ArrayList<>();

    /**
     * 回调接口，用于获取tab的选中状态
     * */
    protected OnTabSelectedListener mTabListener;

    //构造函数
    public TabIndicatorBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    //初始化该布局
    private void init(Context context, AttributeSet attrs) {
        //设置水平
        setOrientation(LinearLayout.HORIZONTAL);
        //设置居中
        setGravity(Gravity.CENTER);

        //Load defaults from resources
        final Resources res = getResources();
        final float defaultTabPadding = res.getDimension(R.dimen.default_tab_view_padding);
        final float defaultIndicatorSize = res.getDimension(R.dimen.default_tab_view_indicator_size);

        // Styleables from XML
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabIndicator);
        mIndicatorSize = (int) a.getDimension(R.styleable.TabIndicator_TabIndicatorSize, defaultIndicatorSize);
        mTabPadding = (int) a.getDimension(R.styleable.TabIndicator_tabItemPadding, defaultTabPadding);

        handleStyledAttributes(a);
        a.recycle();
        //初始化控件
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //布局属性
        LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        params.gravity = Gravity.CENTER;
        //获取条目个数
        int size = getTabSize();
        for (int i = 0; i < size; i++) {
            final int index = i;

            //生成TabView
            T tabItemView = createTabView();
            tabItemView.setPadding(mTabPadding, mTabPadding, mTabPadding, mTabPadding);
            tabItemView.setIndicatorSize(mIndicatorSize);
            //设置特殊属性
            setProperties(tabItemView, i);
            //把生成的TabView添加到该布局
            this.addView(tabItemView, params);

            // CheckedTextView设置索引作为tag，以便后续图片
            tabItemView.setTag(index);
            // 将CheckedTextView添加到list中，便于操作
            mCheckedList.add(tabItemView);
            tabItemView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 设置底部图片的显示
                    setTabsDisplay(index);
                    if (null != mTabListener) {
                        // tab项被选中的回调事件
                        mTabListener.onTabSelected(index);
                    }
                }
            });

            // 初始化 底部菜单选中状态,默认第一个选中
            if (i == 0) {
                tabItemView.setSelected(true);
            } else {
                tabItemView.setSelected(false);
            }
        }
    }

    /**
     * Allows Derivative classes to handle the XML Attrs without creating a
     * TypedArray themsevles
     *
     * @param a - TypedArray of TabIndicator Attributes
     */
    protected void handleStyledAttributes(TypedArray a) {
    }

    /**
     * 生成TabView
     * @return
     */
    protected abstract T createTabView();

    /**
     * 设置特殊属性
     * @param t
     */
    protected abstract void setProperties(T t, int index);

    /**
     * 获取条目个数
     * @return
     */
    protected abstract int getTabSize();

    /**
     * 设置底部导航中图片显示状态
     */
    public void setTabsDisplay(int index) {
        int size = mCheckedList.size();
        for (int i = 0; i < size; i++) {
            T mIconView = mCheckedList.get(i);
            if ((Integer) (mIconView.getTag()) == index) {
                mIconView.setSelected(true);
            } else {
                mIconView.setSelected(false);
            }
        }
    }

    /**
     * 设置指示点的显示
     *
     * @param position
     *            显示位置
     * @param visible
     *            是否显示，如果false，则都不显示
     */
    public void setIndicateDisplay(int position, boolean visible) {
        int size = mCheckedList.size();
        if (size <= position) {
            return;
        }
        T tabView = mCheckedList.get(position);
        tabView.setIndicateDisplay(visible);
    }

    /**
     * 设置指示点图片
     * @param resId
     */
    public void setIndicateBitmap(int resId) {
        for(T t : mCheckedList) {
            t.setIndicatorBitmap(resId);
        }
    }

    /**
     * 设置定义选中tab的接口回调
     * @param listener
     */
    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        this.mTabListener = listener;
    }

    /**
     * 定义选中tab的接口
     */
    public interface OnTabSelectedListener {
        void onTabSelected(int index);
    }

}
