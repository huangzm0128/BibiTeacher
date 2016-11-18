package pype.mingming.bibiteacher.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import pype.mingming.bibiteacher.R;

/**
 * 自定义圆形图片
 *
 * 圆形头像嘛说到底就是张图片，所以自定义圆形图片控件自然要继承Android原生的ImgaeView,
 * 实现其中的setImageBitmap，setImageDrawable，setImageURI，setImageResource的方法。当然主要的onDraw方法也不能缺少，
 * 在其中要实现圆形头像的绘制。这里还要借助两个主要的类Matrix与BitmapShader，通过这两个类实现图片的缩放效果
 *
 * Created by mingming on 2016/7/29.
 */
public class CircleImageView extends ImageView{

    /**
     *  属性数组
     */
    private TypedArray ta;

    private int mBitmapWidth;
    private int mBitmapHeight;

    /**
     * 边界颜色
     */
    private int mBorderColor;

    /**
     * 填充颜色
     */
    private int mFillColor;

    /**
     * 描边宽度
     */
    private int mStrokeWidth;

    /**
     * 默认边界颜色
     */
    private static final int DEFAULT_BORDER_COLOR = Color.WHITE;

    /**
     * 填充颜色
     */
    private static final int DEFAULT_FILL_COLOR = R.color.fill_color;

    /**
     * 默认描边宽度
     */
    private static final int DEFAULT_STROKE_WIDTH = 1;

    /**
     * Scaletype决定了图片在View上显示时的样子
     * ScaleType.CENTER_CROP 按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
     */
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    /**
     * 位图的格式为ARGB_8888（比位图原本默认的RGB.565要清晰）
     */
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    private Bitmap mBitmap;
    /**
     * 绘图的Paint
     */
    private Paint mPaint;
    private Paint mBorderPaint;
    private Paint mFillPaint;

    /**
     * 矩阵，用于缩放或放大
     */
    private Matrix mMatrix;

    /**
     * 绘制的范围
     */
    private RectF mDrawableRec;

    /**
     * 渲染图像，使用图像为绘制的图形着色
     */
    private BitmapShader mBitmapShader;

    /**
     * 位图半径
     */
    private float mRadius;

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleImageView(Context context) {
       this(context, null);
    }

    private void init(){
        mPaint = new Paint();
        mBorderPaint = new Paint();
        mFillPaint = new Paint();
        mMatrix = new Matrix();
        mDrawableRec = new RectF();
        this.setScaleType(SCALE_TYPE);
        mBorderColor = DEFAULT_BORDER_COLOR;
        mFillColor = DEFAULT_FILL_COLOR;
        mStrokeWidth = DEFAULT_STROKE_WIDTH;
        setUp();
    }

    @Override
    public void setImageBitmap(Bitmap bm){
        super.setImageBitmap(bm);
        mBitmap = bm;
        setUp();
    }

    @Override
    public void setImageDrawable(Drawable drawable){
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setUp();
    }

    @Override
    public void setImageURI(Uri uri){
        super.setImageURI(uri);
        mBitmap = uri != null? getBitmapFromDrawable(getDrawable()) : null;
        setUp();
    }


    @Override
    public void setImageResource(int resId){
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setUp();
    }


    /**
     * 将drawable装换为Bitmap
     * @param drawable
     * @return
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if(drawable == null){
            return null;
        }
        //判断drawable是否为BitmapDrawable的子类
        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap;
        if(drawable instanceof ColorDrawable){
            //如果是颜色drawable，则要创建2,2位图
            bitmap = Bitmap.createBitmap(2,2,BITMAP_CONFIG);
        }else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG);
        }
        Canvas canvas = new Canvas(bitmap);
        //设置显示区域
        drawable.setBounds(0, 0, canvas.getWidth(), getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 配置画笔Paint 和 BitmapShader属性
     */
    private void setUp() {
        if(getWidth() == 0 && getHeight() == 0){
            return;
        }
        if(mBitmap == null){
            invalidate();
            return;
        }

        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);

        if(mStrokeWidth != 0){
            //初始化描边画笔
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(mBorderColor);
            mBorderPaint.setStrokeWidth(mStrokeWidth);
        }

        mRadius = Math.min(getWidth() * 1.0f / 2, getHeight() * 1.0f / 2);

        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();

        float scal; //缩放值
        mMatrix.set(null);
        mDrawableRec.set(0, 0, getWidth(), getHeight());
        //图片过大与过小都不好，不可能绝对的适中，所以缩放值的获取是最重要的。
        // 我们要比较图片宽高与控件宽高，通过错位相乘来比较图片的宽高哪个与控件的宽高相差更大。
        // 取相差大的值进行比较得到缩放值
        if (mBitmapWidth * mDrawableRec.height() > mDrawableRec.width() * mBitmapHeight){
            scal = mDrawableRec.height() * 1.0f / mBitmapHeight;
        }else {
            scal = mDrawableRec.width() * 1.0f / mBitmapWidth;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //设置shader
        mPaint.setShader(mBitmapShader);
        mPaint.setAntiAlias(true);
        mMatrix.setScale(scal, scal);
        //设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmap == null){
            return;
        }
        //填充
        canvas.drawCircle(getWidth() * 1.0f / 2, getHeight() * 1.0f / 2, mRadius, mFillPaint);
        canvas.drawCircle(getWidth() * 1.0f / 2, getHeight() * 1.0f / 2, mRadius, mPaint);
        //描边
        if(mStrokeWidth != 0){
            canvas.drawCircle(getWidth() * 1.0f / 2, getHeight() * 1.0f / 2, mRadius, mBorderPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setUp();
    }

    //设置填充颜色
    public void setmFillColor(int mFillColor) {
        this.mFillColor = mFillColor;
    }

    //设置描边宽度
    public void setmStrokeWidth(int mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
    }

    //设置边界颜色
    public void setmBorderColor(int mBorderColor) {
        this.mBorderColor = mBorderColor;
    }

}
