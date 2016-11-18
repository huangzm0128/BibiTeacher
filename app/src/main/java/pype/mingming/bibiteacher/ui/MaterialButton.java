package pype.mingming.bibiteacher.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.utils.LogUtils;

/**
 * material design 自定义动画自定义控件(或称组件)也就是编写自己的控件类型,而非Android中提供的标准的控件
 * 参考网页 http://blog.csdn.net/bbld_/article/details/40633327
 *          http://www.cnblogs.com/zwl12549/archive/2011/04/13/2015366.html
 *          http://blog.csdn.net/dreamfly2007/article/details/8542051
 *
 * 1.我们的自定义控件和其他的控件一样,应该写成一个类,而这个类的属性是是有自己来决定的.

 * 2.我们要在res/values目录下建立一个attrs.xml的文件,并在此文件中增加对控件的属性的定义.

 * 3.使用AttributeSet来完成控件类的构造函数,并在构造函数中将自定义控件类中变量与attrs.xml中的属性连接起来.

 * 4 .在自定义控件类中使用这些已经连接的属性变量.

 * 5.将自定义的控件类定义到布局用的xml文件中去.

 * 6.在界面中生成此自定义控件类对象,并加以使用.
 *
 * Created by mingming on 2016/6/2.
 */
public class MaterialButton extends RelativeLayout {
    private static final String TAG = MaterialButton.class.getSimpleName();

    private static final int DEFAULT_RADIUS = 10;
    private static final int DEFAULT_FRAME_RATE = 10;
    private static final int DEFAULT_DURATION = 200;
    private static final int DEFAULT_ALPHA = 255;
    private static final float DEFAULT_SCALE = 0.8f;
    private static final int DEFAULT_ALPHA_STEP = 5;

    /**
     * 动画帧率
     */
    private int mFrameRate = DEFAULT_FRAME_RATE;

    /**
     * 持续时间
     */
    private int mDuration = DEFAULT_DURATION;

    /**
     * 画笔
     */
    Paint mPaint = new Paint();

    /**
     * 被点击视图的中心点
     */
    private Point mCenterPoint = null;

    /**
     * 视图Rect
     */
    private RectF mTargetRectf;

    /**
     * 起始圆形背景半径
     */
    private int mRadius = DEFAULT_RADIUS;

    /**
     * 最大圆形半径
     */
    private int mMaxRadius = DEFAULT_RADIUS;

    /**
     * 渐变的背景色
     */
    private int mCircleColor = Color.LTGRAY;

    /**
     * 每次重绘时半径的增幅
     */
    private int mRadiusStep = 1;

    /**
     * 保存用户设置的Alpha值
     */
    private int mBackupAlpha;

    /**
     * 圆形半径对于被点击的视图的缩放比例，默认为0.8f
     */
    private float mCircleScale = DEFAULT_SCALE;

    /**
     * 颜色的Alpha值(0,255)
     */
    private int mColorAlpha = DEFAULT_ALPHA;

    /**
     * 每次动画Alpha的渐变递减值
     */
    private int mAlphaStep = DEFAULT_ALPHA_STEP;

    /**
     * 目标视图
     */
    private View mTargetView;

    public MaterialButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 使用AttributeSet来完成控件类的构造函数,并在构造函数中将自定义控件类中变量与attrs.xml中的属性连接起来.
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs){
        if(this.isInEditMode()){
            return;
        }
        if(attrs != null){
            initTypeArray(context, attrs);
        }

        initPaint();
        this.setWillNotDraw(false);
        this.setDrawingCacheEnabled(true);
    }

    /**
     * 将自定义控件类中变量与attrs.xml中的属性连接起来.
     * @param context
     * @param attrs
     */
    private void initTypeArray(Context context, AttributeSet attrs){
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialButton);

        mColorAlpha = typedArray.getInteger(R.styleable.MaterialButton_alpha, DEFAULT_ALPHA);
        mAlphaStep = typedArray.getInteger(R.styleable.MaterialButton_alpha_step, DEFAULT_ALPHA_STEP);
        mFrameRate = typedArray.getInteger(R.styleable.MaterialButton_frame_rate, DEFAULT_FRAME_RATE);
        mCircleColor = typedArray.getColor(R.styleable.MaterialButton_reveal_color, Color.LTGRAY);
        mDuration = typedArray.getInteger(R.styleable.MaterialButton_duration,DEFAULT_DURATION);
        mCircleScale = typedArray.getFloat(R.styleable.MaterialButton_scale, DEFAULT_SCALE);

        typedArray.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint(){
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleColor);
        mPaint.setAlpha(mColorAlpha);

        //备份alpha属性用于动画完成时重置
        mBackupAlpha = mColorAlpha;
    }

    /**
     * 点击的坐标是否在View的内部，并赋值给mTargetView
     * @param touchView 点击的视图
     * @param x 点击的x坐标
     * @param y 点击的y坐标
     * @return 在view的内部返回true,反之返回false
     */
    private boolean isInFrame(View touchView, float x, float y){
        mTargetRectf = getViewRectf(touchView);
        return mTargetRectf.contains(x, y);
    }

    /**
     * 以点击的坐标为起点画一个矩形
     * 获取点中的区域,屏幕绝对坐标值,这个高度值也包含了状态栏和标题栏高度
     * @param touchView
     * @return 点中的区域
     */
    private RectF getViewRectf(View touchView){
        int[] location = new int[2];
        touchView.getLocationOnScreen(location);
        // 视图的区域
        RectF rectF = new RectF(location[0], location[1], location[0]
                + touchView.getWidth(), location[1] + touchView.getHeight());

        return rectF;
    }

    /**
     * 减去状态栏和标题栏的高度，并且计算出点击的RectF的中心点,赋值给mCenterPoint
     */
    private void minusExtraHeight(){
        int[] location = new int[2];
        this.getLocationOnScreen(location);
        //减去该布局的top值，这个top值（location[1]）就是状态栏的高度
        mTargetRectf.top -= location[1];
        mTargetRectf.bottom -= location[1];

        //计算点击中心点
        int centerX = (int) ((mTargetRectf.left + mTargetRectf.right) / 2);
        int centerY = (int) ((mTargetRectf.top + mTargetRectf.bottom) / 2);

        mCenterPoint = new Point(centerX, centerY);
    }

    /**
     * 查找点击的点是否在ViewGroup的childView中
     * @param viewGroup
     * @param x 要查找的点的x
     * @param y 要查找的点的y
     * @return 若要查找的点在ViewGroup中，侧返回该点所在的childView，反之返回null
     */
    private View findTargetView(ViewGroup viewGroup, float x, float y){
        int childCount = viewGroup.getChildCount();
        View childView = null;
        //迭代查找被点击的视图
        for(int i = 0; i < childCount; i++){
            childView = viewGroup.getChildAt(i);
            //instanceof通过返回一个布尔值来指出，这个对象是否是这个特定类或者是它的子类的一个实例
            if(childView instanceof ViewGroup){
                return findTargetView(viewGroup, x, y);
            }else if(isInFrame(childView, x, y)){ //判断该点是否在view的frame中
                return childView;
            }
        }
        return null;
    }

    /**
     *
     * @return 当点前动画的radius大于最大radius时返回true
     */
    private boolean isAnimEnd(){
        return mRadius >= mMaxRadius;
    }

    /**
     * 计算ripple的最大半径，并赋值给mMaxRadius
     * 计算redrawCount的次数，mRadiusStep的增值, mAlphaStep的递减值
     * @param view 目标视图
     */
    private void calculateMaxRadius(View view){
        //取视图最长边
        int maxlength = Math.max(view.getWidth(), view.getHeight());

        //计算ripple圆形半径
        mMaxRadius = (int) (maxlength / 2 * mCircleScale);
        //计算重画次数
        int redrawCount = mDuration / mFrameRate;
        //计算每次动画半径的增值
        mRadiusStep = (mMaxRadius - DEFAULT_RADIUS) / redrawCount;
        //计算每次alpha递减的值(为啥要减100？？？？测试一下就知道了)
        mAlphaStep = (mColorAlpha - 100) / redrawCount;
    }

    /**
     * 处理ACTION_DOWN事件，注意这里获取的是Raw x y;
     * 即屏幕的绝对坐标，但是这个屏幕有状态栏和标题栏是要减去这些高度
     * 因此得到mTargetRectf后其高度top要减去该布局的top，也就是标题栏和状态栏的高度
     *
     * @param event ACTION_DOWN事件
     */
    private void deliveryTouchDownEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            mTargetView = findTargetView(this, event.getRawX(), event.getRawY());
            if(mTargetView != null){
                //减去状态栏和标题栏的高度
                minusExtraHeight();
                //计算相关数据
                calculateMaxRadius(mTargetView);
                //重绘视图
                invalidate();
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        deliveryTouchDownEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        LogUtils.i(TAG, "dispatchDraw");
        super.dispatchDraw(canvas);
        drawRippleIfNecessary(canvas);
    }

    private void drawRippleIfNecessary(Canvas canvas){
        if(isFoundTouchedSubView()){
            //计算新的半径和Alpha值
            mRadius += mRadiusStep;
            mColorAlpha -= mAlphaStep;

            //剪裁一块区域，该区域就是被点击的视图的大小，使用clipRect来获取该区域，使得绘制操作只能在该区域进行
            //即使绘制的内容大于这块区域，那么大于这块区域的绘制内容将不可见，这样保证了背景层只能绘制在该视图的区域
            canvas.clipRect(mTargetRectf);
            mPaint.setAlpha(mColorAlpha);
            //绘制背景
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mPaint);
            if(isAnimEnd()){
                reset();
            }else {
                invalidateDelayed();
            }
        }
    }

    /**
     * 判断是否找到被点击的子视图
     * @return
     */
    private boolean isFoundTouchedSubView(){
        return mCenterPoint != null && mTargetView != null;
    }

    /**
     * 发送重绘消息
     */
    private void invalidateDelayed(){
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, mFrameRate);
    }

    private void reset(){
        mCenterPoint = null;
        mTargetRectf = null;
        mRadius = DEFAULT_RADIUS;
        mColorAlpha = mBackupAlpha;
        mTargetView = null;
        invalidate();

    }
}
