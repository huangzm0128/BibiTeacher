package pype.mingming.bibiteacher.db;

import android.content.Context;
import android.content.SharedPreferences;

import pype.mingming.bibiteacher.Constant;


/**
 * SharePreference管理
 * Created by mingming on 2016/6/10.
 */
public class SharePreferencesHelper {

    /**
     * 设置Constant.LOGTAG 文件中的theme的值
     * @param context 上下文
     * @param is is的值为false即为夜间模式， true为正常模式
     */
    public static void setTheme(Context context, boolean is){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.LOGTAG, Context.MODE_PRIVATE);
        //apply()与commit()都是提交数据的方法，apply()没有返回值，并且通过内存存储到硬盘中，效率高
        sharedPreferences.edit().putBoolean("theme", is).apply();
    }

    /**
     * 获取Constant.LOGTAG 文件中的theme的值
     * @param context 上下文
     * @return 返回Constant.LOGTAG 文件中的theme的值
     */
    public static boolean getTheme(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.LOGTAG, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("theme", false);
    }
}
