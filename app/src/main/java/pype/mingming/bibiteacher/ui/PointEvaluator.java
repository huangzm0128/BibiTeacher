package pype.mingming.bibiteacher.ui;


import android.animation.TypeEvaluator;

import pype.mingming.bibiteacher.entity.Point;


/**
 *
 * PointEvaluator同样实现了TypeEvaluator接口并重写了evaluate()方法。
 * 其实evaluate()方法中的逻辑还是非常简单的，先是将startValue和endValue强转成Point对象，
 * 然后同样根据fraction来计算当前动画的x和y的值，最后组装到一个新的Point对象当中并返回。
 * 这样我们就将PointEvaluator编写完成了，接下来我们就可以非常轻松地对Point对象进行动画操作了
 * Created by mingming on 2016/6/25.
 */
public class PointEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;

        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
        Point point = new Point(x, y);
        return point;
    }

    /**
     * 重写evaluate()方法。evaluate()方法当中传入了三个参数，
     * 第一个参数fraction非常重要，这个参数用于表示动画的完成度的，我们应该根据它来计算当前动画的值应该是多少，
     * 第二第三个参数分别表示动画的初始值和结束值。
     * 那么上述代码的逻辑就比较清晰了，用结束值减去初始值，算出它们之间的差值，
     * 然后乘以fraction这个系数，再加上初始值，那么就得到当前动画的值了
     */

}
