package pype.mingming.bibiteacher.ui.bottomTab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import pype.mingming.bibiteacher.R;


/**
 * 底部导航条目操作基类
 * Created by mingming on 2016/7/27.
 */
public abstract class TabViewBase extends View implements ITabView {

    /**
     *  选中颜色
     */
    protected int mSelectedColor;

    /**
     *  未选中颜色
     */
    protected int mUnselectedColor;

    /**
     * 限制绘制icon的范围
     */
    protected Rect mIconRect;

    /**
     *  限制绘制指示点的范围
     */
    private Rect mIndicatorRect;

    /**
     * 是否显示指示点
     */
    private boolean isIndicateDisplay;

    /**
     * 指示点大小
     */
    private int mIndicatorSize;

    /**
     * 指示点图片
     */
    private Bitmap mIndicatorBitmap;

    /**
     * 是否选中
     */
    protected boolean isSelected;

    protected Rect mBottomBound = new Rect();

    //该构造函数初始化下面的构造函数
    public TabViewBase(Context context) {
        this(context, null);
    }

    public TabViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        setIndicatorBitmap(R.drawable.update_hint_default);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 得到绘制icon的宽
        int bitmapWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight(), getMeasuredHeight() - getPaddingTop()
                - getPaddingBottom() - mBottomBound.height());

        int left = (getMeasuredWidth() >> 1) - (bitmapWidth >> 1);
        int top = ((getMeasuredHeight() - mBottomBound.height()) >> 1) - (bitmapWidth >> 1);
        // 设置icon的绘制范围
        mIconRect = new Rect(left, top, left + bitmapWidth, top + bitmapWidth);
        // 设置指示点的范围
        int indicatorRadius = mIndicatorSize >> 1;
        int tabRealHeight = bitmapWidth + mBottomBound.height();
        mIndicatorRect = new Rect(left + tabRealHeight* 4/5 - indicatorRadius, top, left+tabRealHeight* 4/5 + indicatorRadius, top + mIndicatorSize);
    }


    /**
     * 绘制指示点
     * @param canvas
     */
    protected void drawIndicator(Canvas canvas) {
        if(isIndicateDisplay) {
            canvas.drawBitmap(mIndicatorBitmap, null, mIndicatorRect, null);
        }
    }


    @Override
    public void setSelectedColor(int selectedColor) {
        this.mSelectedColor = selectedColor;
        invalidateView();
    }

    @Override
    public void setUnselectedColor(int unselectedColor) {
        this.mUnselectedColor = unselectedColor;
        invalidateView();
    }


    @Override
    public void setIndicatorSize(int indicatorSize) {
        this.mIndicatorSize = indicatorSize;
    }

    @Override
    public void setIndicatorBitmap(Bitmap bitmap) {
        this.mIndicatorBitmap = bitmap;
    }

    @Override
    public void setIndicatorBitmap(int resId) {
        this.mIndicatorBitmap = BitmapFactory.decodeResource(getResources(), resId);
    }

    @Override
    public abstract void setSelected(boolean selected);

    /**
     * 设置切换颜色渐变
     * @param alpha
     */
    protected void setIconAlpha(float alpha){}

    /**
     * 设置指示点的显示
     *
     * @param visible
     *            是否显示，如果false，则都不显示
     */
    public void setIndicateDisplay(boolean visible) {
        this.isIndicateDisplay = visible;
    }



    protected void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }
}
