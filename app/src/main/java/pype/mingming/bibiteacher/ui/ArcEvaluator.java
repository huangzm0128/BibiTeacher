package pype.mingming.bibiteacher.ui;

import android.animation.TypeEvaluator;

import pype.mingming.bibiteacher.entity.Point;

/**
 * 画圆弧的轨迹
 * Created by mingming on 2016/8/3.
 */
public class ArcEvaluator implements TypeEvaluator {

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {

        //上端点
        Point startPoint = (Point) startValue;
        //下端点
        Point endPoint = (Point) endValue;
        //半径
        float r = Math.abs(startPoint.getY() - endPoint.getY()) / 2;

        //弧度
        float arc = (float) (Math.PI * fraction);

        float x = (float) (startPoint.getX() + Math.sin(arc) * r);
        float y = (float) ((startPoint.getY() + (r - Math.cos(arc) * r)));

        return new Point(x, y);
    }

}
