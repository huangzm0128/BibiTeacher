package pype.mingming.bibiteacher.ui.bottomTab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

/**
 * TabPageIndicatorEx的每一个条目
 * 继承 底部导航条目操作基类
 * Created by mingming on 2016/7/27.
 */
public class TabPageViewEx extends TabViewBase {
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Paint mPaint;
	/**
	 *  透明度 0.0-1.0
	 */
	private float mAlpha = 0f;
	/**
	 * 图标
	 */
	private Bitmap mIconBitmap;

	public TabPageViewEx(Context context) {
		super(context);
	}

	public TabPageViewEx(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int alpha = (int) Math.ceil((255 * mAlpha));
		canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
		setupTargetBitmap(alpha);

		canvas.drawBitmap(mBitmap, 0, 0, null);
		drawIndicator(canvas);
	}

	//绘制目标图标
	private void setupTargetBitmap(int alpha) {
		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mPaint = new Paint();
		mPaint.setColor(mSelectedColor);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setAlpha(alpha);
		mCanvas.drawRect(mIconRect, mPaint);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mPaint.setAlpha(255);
		mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
	}



	@Override
	public void setSelected(boolean selected) {
		if(selected) {
			setIconAlpha(1.0f);
		} else {
			setIconAlpha(0f);
		}
	}

	public void setIcon(int resId) {
		this.mIconBitmap = BitmapFactory.decodeResource(getResources(), resId);
		if (mIconRect != null)
			invalidateView();
	}

	public void setIcon(Bitmap iconBitmap) {
		this.mIconBitmap = iconBitmap;
		if (mIconRect != null)
			invalidateView();
	}

	public void setIconAlpha(float alpha) {
		this.mAlpha = alpha;
		invalidateView();
	}

}
