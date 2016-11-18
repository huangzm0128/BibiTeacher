package pype.mingming.bibiteacher.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 左右滑动时点大小的动态的变化
 * Created by mingming on 2016/7/24.
 */
public class LPagePointView extends View {
    private final int POINT_OFFSET = 2;
    private final int VIEW_OFFSET = 2;
    private final int MIN_POINT_SIZE = 1;
    private final int INDEX_POINT_LENGTH = 6;

    /**
     * 数量
     */
    private int pointSize = 0;
    /**
     * 当前变换的点
     */
    private int pointIndex = 0;
    /**
     * 点的半径
     */
    private int pointRadius = 5;
    /**
     * 小点的颜色
     */
    private int pointColor = Color.WHITE;
    /**
     * 小点的位置X
     */
    private int pointLocationX = 0;
    /**
     * 小点的位置Y
     */
    private int pointLocationY = 0;
    /**
     * 小点的坐标X
     */
    private int pointX = 0;
    /**
     * 小点的坐标Y
     */
    private int pointY = 0;
    /**
     * 小点的画笔
     */
    private Paint pointPaint;
    /**
     * 移动进度
     */
    private float pointPercent;
    /**
     * 宽度
     */
    private int width = 0;
    /**
     * 高度
     */
    private int height = 0;


    public LPagePointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public LPagePointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LPagePointView(Context context) {
        this(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();

        if(pointSize < MIN_POINT_SIZE){
            return;
        }
        pointPaint.setColor(Color.WHITE);
        pointRadius = Math.min(width / (pointSize * POINT_OFFSET + VIEW_OFFSET), height >> 1); // >> 1相当于除以2
        pointLocationX = (width >> 1) - pointRadius * (pointSize * POINT_OFFSET + VIEW_OFFSET);
        pointLocationY = (height >> 1) - pointRadius;
        pointX = pointLocationX;
        pointY = pointLocationY;
        int pointWidth = 0;
        RectF rectF = null;
        /*
        * 画点算法;
        * 当下点为6个点宽度的宽，滑动时随着pointPercent增加，当下点的宽度逐渐减小
        * 下一个点宽度逐渐增加，其它点为正常大小
        * */
        for(int i=0; i<pointSize; i++){
            if(i == pointIndex){
                pointWidth = (int) (pointRadius * INDEX_POINT_LENGTH * (1-pointPercent)) + (pointRadius << 1); // << 1相当于*2
            }else if(i == pointIndex + 1){
                pointWidth = (int) ((pointRadius * INDEX_POINT_LENGTH * pointPercent)) + (pointRadius << 1);
            }else {
                pointWidth = pointRadius << 1;
            }

            rectF = new RectF(pointX, pointY, pointX + pointWidth, pointY + (pointRadius << 1));
            canvas.drawRoundRect(rectF, pointRadius, pointRadius, pointPaint);
            pointX += (pointRadius << 1) + pointWidth;
        }
    }

    /**
     * 初始化
     */
    public void init(){
        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
    }

    public int getPointSize(){return pointSize;}
    public void setPointSize(int pointSize){
        this.pointSize = pointSize;
        invalidate();
    }

    public int getPointRadius(){return pointRadius;}

    public void setPointColor(int pointColor){
        this.pointColor = pointColor;
        invalidate();
    }

    /**
     * 状态修改
     * @param index 下标
     * @param percent 百分比
     */
    public void onChange(int index, float percent){
        this.pointIndex = index;
        this.pointPercent = percent;
        invalidate();
    }

}
