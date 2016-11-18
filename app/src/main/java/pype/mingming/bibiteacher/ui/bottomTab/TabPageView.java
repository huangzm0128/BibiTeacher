package pype.mingming.bibiteacher.ui.bottomTab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * TabPageIndicator的每一个条目
 * 继承 底部导航条目操作基类
 * Created by mingming on 2016/7/27.
 */
public class TabPageView extends TabViewBase {

	private Paint mPaint = new Paint();
	/**
	 *  透明度 0.0-1.0
	 */
	private float mAlpha = 0f;

	/**
	 *  图标
	 */
	//选中的图标
	private Bitmap mSelectedIconBitmap;
	//未选中的图标
	private Bitmap mUnselectedIconBitmap;

	public TabPageView(Context context) {
		super(context);
	}

	public TabPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//绘制渐变度
		int alpha = (int) Math.ceil((255 * mAlpha));
		drawSourceBitmap(canvas, alpha);
		drawTargetBitmap(canvas, alpha);
		drawIndicator(canvas);
	}

	/**
	 * 绘制未选中图标
	 * @param canvas
	 * @param alpha
	 */
	private void drawSourceBitmap(Canvas canvas, int alpha) {
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setAlpha(255 - alpha);
		canvas.drawBitmap(mUnselectedIconBitmap, null, mIconRect, mPaint);
	}

	/**
	 * 绘制选中图标
	 * @param canvas
	 * @param alpha
	 */
	private void drawTargetBitmap(Canvas canvas, int alpha) {
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setAlpha(alpha);
		canvas.drawBitmap(mSelectedIconBitmap, null, mIconRect, mPaint);
	}



	@Override
	public void setSelected(boolean selected) {
		if(selected) {
			setIconAlpha(1.0f);
		} else {
			setIconAlpha(0f);
		}
	}

	public void setSelectedIcon(int resId) {
		this.mSelectedIconBitmap = BitmapFactory.decodeResource(getResources(), resId);
		if (mIconRect != null)
			invalidateView();
	}

	public void setUnselectedIcon(int resId) {
		this.mUnselectedIconBitmap = BitmapFactory.decodeResource(getResources(), resId);
		if (mIconRect != null)
			invalidateView();
	}


	public void setIconAlpha(float alpha) {
		this.mAlpha = alpha;
		invalidateView();
	}

}
