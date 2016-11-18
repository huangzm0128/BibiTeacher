package pype.mingming.bibiteacher.ui.bottomTab;

import android.graphics.Bitmap;

/**
 * 底部导航条目操作接口
 * Created by mingming on 2016/7/27.
 */
public interface ITabView {

    /**
     * 设置选中时颜色
     * @param selectedColor
     */
    void setSelectedColor(int selectedColor);

    /**
     * 设置未选中时颜色
     * @param unselectedColor
     */
    void setUnselectedColor(int unselectedColor);


    /**
     * 设置指示点大小
     * @param indicatorSize
     */
    void setIndicatorSize(int indicatorSize);

    /**
     * 设置指示点图片
     * @param bitmap
     */
    void setIndicatorBitmap(Bitmap bitmap);

    /**
     * 设置指示点图片
     * @param resId
     */
    void setIndicatorBitmap(int resId);

    /**
     * 设置是否选中
     * @param isSelected
     */
    void setSelected(boolean isSelected);
}
