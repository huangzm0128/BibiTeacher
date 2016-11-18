package pype.mingming.bibiteacher.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

import pype.mingming.bibiteacher.entity.MyDate;
import pype.mingming.bibiteacher.ui.MCalendarDialog;
import pype.mingming.bibiteacher.ui.MCalendarView;

/**
 * Created by mingming on 2016/10/6.
 */

public class CalendarUtil {

    private double dialogWidthProportion = 0.8;
    private double dialogHeightProportion = 0.5;
    /**
     * 获取一个有限制的日期选择对话框
     * @param context
     * @param month
     * @param year
     * @param day
     * @param startMonth 起始的月份
     * @param startYear 起始年份
     * @param startDay 起始日期
     * @param selectedDays 已选择的天数
     * @param lis
     * @return
     */
    public Dialog getCalendarDialog(Activity context, int month, int year,
                                    int day, int startMonth, int startYear, int startDay,
                                    MCalendarView.SlideType slideType, List<MyDate> selectedDays, MCalendarDialog.CalendarDialogListener lis) {
        MCalendarDialog dialog = new MCalendarDialog(context, month, year, day,
                startMonth, startYear, startDay,slideType, selectedDays,lis);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        dialog.setCancelable(true);
        dialog.show();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * dialogHeightProportion);
        p.width = (int) (d.getWidth() * dialogWidthProportion);
        dialog.onWindowAttributesChanged(p);
        window.setAttributes(p);
        return dialog;
    }
}
