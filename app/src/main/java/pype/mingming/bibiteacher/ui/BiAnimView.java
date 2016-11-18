package pype.mingming.bibiteacher.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import pype.mingming.bibiteacher.entity.Point;

/**
 * 三个球体运动生成动画
 * Created by mingming on 2016/8/3.
 */
public class BiAnimView extends View {
    Context context;
    private int radius;
    private static  float RADIUS ;
    private Point line1CurrentPoint;
    private Point actCurrentPoint;
    private Point act2CurrentPoint;

    private Paint textPaint;


    private boolean revealStart = false;

    private Paint mLinePaint;
    private Paint mAcr1Paint;
    private Paint mArc2Paint;
    public BiAnimView(Context context) {
       this(context,null);
    }

    public BiAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.BLUE);
        mAcr1Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAcr1Paint.setColor(Color.RED);
        mArc2Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArc2Paint.setColor(Color.GRAY);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);

        // 通过Resources获取(获取屏幕的参数)
        DisplayMetrics dm2 = getResources().getDisplayMetrics();
        RADIUS = dm2.widthPixels / 16;
        textPaint.setTextSize(RADIUS * 4/7);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(line1CurrentPoint == null){
            line1CurrentPoint = new Point(getWidth()/2, getHeight() /4);
            if(actCurrentPoint == null){

                actCurrentPoint = new Point(getWidth() / 2, getHeight() / 4 );

            }
            if(act2CurrentPoint == null){
                act2CurrentPoint = new Point(getWidth() / 2, getHeight() / 4 );
            }
            drawBi(canvas);
            startAnimal();
        }else {
            drawBi(canvas);
        }
        if(revealStart){
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mLinePaint); //扩大至整个屏幕（因为 radius 属性）
        }
    }

    private void drawBi(Canvas canvas){

        float x = line1CurrentPoint.getX();
        float y = line1CurrentPoint.getY();
        float ax = actCurrentPoint.getX();
        float ay = actCurrentPoint.getY();
        float ax2 = act2CurrentPoint.getX();
        float ay2 = act2CurrentPoint.getY();

        canvas.drawCircle(ax, ay, RADIUS , mAcr1Paint);
        canvas.drawText("家", ax - RADIUS / 3, ay + RADIUS / 5, textPaint );

        canvas.drawCircle(ax2, ay2, RADIUS , mArc2Paint);
        canvas.drawText("Bi", ax2 - RADIUS / 3, ay2 + RADIUS / 5, textPaint );

        canvas.drawCircle(x, y, RADIUS, mLinePaint);
        canvas.drawText("教", x - RADIUS / 3, y + RADIUS / 5, textPaint );

    }

    /**
     * 每当Point值有改变的时候都会回调onAnimationUpdate()方法。在这个方法当中，
     * 我们对currentPoint对象进行了重新赋值，并调用了invalidate()方法，这样的话onDraw()方法就会重新调用，
     * 并且由于currentPoint对象的坐标已经改变了，那么绘制的位置也会改变，于是一个平移的动画效果也就实现了
     */
    private void startAnimal() {
        int startX = getWidth() / 2;
        int startY = getHeight() / 4;

        Point startPoint = new Point(startX, startY);
        Point endPoint = new Point(startX, getHeight() - RADIUS * 7);

        Point certenPoint = new Point(getWidth() / 2, getHeight() / 2);

        AnimatorSet set = new AnimatorSet();

        //画上圆弧
        ArcEvaluator ae = new ArcEvaluator();
        ValueAnimator arc1Animl = ValueAnimator.ofObject(ae,
                new Point(getWidth() / 2, getHeight() / 4), certenPoint);
        arc1Animl.setDuration(2000);
        arc1Animl.setInterpolator(new AccelerateDecelerateInterpolator());
        arc1Animl.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                actCurrentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });

        //画下圆弧
        ValueAnimator arc2Animl = ValueAnimator.ofObject(new ArcEvaluator(),
                new Point(getWidth() / 2, getHeight() / 4), endPoint);
        arc2Animl.setDuration(2000);
        arc2Animl.setInterpolator(new AccelerateDecelerateInterpolator());
        arc2Animl.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                act2CurrentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });

        //画往下线性运动的员
        final ValueAnimator line1Animl = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        line1Animl.setDuration(1500);
        line1Animl.setInterpolator(new AccelerateDecelerateInterpolator());
        line1Animl.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                line1CurrentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });

        //画往上线性运动的圆
        ValueAnimator line2Animl = ValueAnimator.ofObject(new PointEvaluator(),endPoint, certenPoint);
        line2Animl.setDuration(1000);
        line2Animl.setStartDelay(2000);
        line2Animl.setInterpolator(new AccelerateDecelerateInterpolator());
        line2Animl.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                line1CurrentPoint = (Point) animation.getAnimatedValue();
                act2CurrentPoint = line1CurrentPoint;
                //一定要记得刷新呀
                invalidate();
            }
        });

        set.play(line1Animl).with(arc1Animl).before(arc2Animl).before(line2Animl);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                revealStart = true;
                startReveal();
            }
        });

    }

    /**
     * radius 在300ms内从70逐渐增大到maxRadius,
     * 并在到达maxRadius后调用callback.revealEnd()去启MainActivity
     */
    private void startReveal(){
        setVisibility(VISIBLE);
        //计算对角线sqrt开平方，pow计算x的n次方
        int maxRadius = (int) Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2));
        ObjectAnimator revealAnimator = ObjectAnimator.ofInt(this, "radius", 40,maxRadius).setDuration(300);
        revealAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (null != callback) {
                    callback.revealEnd();
                }

            }
        });
        revealAnimator.setStartDelay(500);
        revealAnimator.start();
    }

    public void setRadius(int radius){
        this.radius = radius;
        //Notion 调用invalidate() 后才调用 onDraw()
        invalidate();
    }

    public int getRadius(){
        return radius;
    }

    public void setCallback(MyCallback callback){
        this.callback = callback;
    }

    //接口回调对象
    private MyCallback callback;
    //自定义接口回调
    public interface MyCallback{
        void revealEnd();
    }
}
