package pype.mingming.bibiteacher.entity;

/**
 * Point类非常简单，只有x和y两个变量用于记录坐标的位置，并提供了构造方法来设置坐标，
 * 以及get方法来获取坐标。接下来定义PointEvaluator
 * Created by mingming on 2016/6/25.
 */
public class Point {

    private float x;
    private float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}