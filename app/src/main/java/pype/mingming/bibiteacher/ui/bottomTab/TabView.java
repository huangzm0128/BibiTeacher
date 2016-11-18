package pype.mingming.bibiteacher.ui.bottomTab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * TabPageIndicator的每一个条目
 * 继承 底部导航条目操作基类
 * Created by mingming on 2016/7/27.
 */
public class TabView extends TabViewBase {

	/** 图标 */
	private Bitmap mSelectedIconBitmap;
	private Bitmap mUnselectedIconBitmap;

	public TabView(Context context) {
		super(context);
	}

	public TabView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setupTargetBitmap(canvas);
		drawIndicator(canvas);

	}

	/**
	 * 绘制图标图片
	 * @param canvas
	 */
	private void setupTargetBitmap(Canvas canvas) {
		canvas.drawBitmap(isSelected ? mSelectedIconBitmap : mUnselectedIconBitmap, null, mIconRect, null);
	}

	@Override
	public void setSelected(boolean selected) {
		this.isSelected = selected;
		invalidateView();
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

}
