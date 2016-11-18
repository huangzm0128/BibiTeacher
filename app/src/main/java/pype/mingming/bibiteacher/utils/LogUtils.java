package pype.mingming.bibiteacher.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 自定义Log日志输出工具
 * Created by mingming on 2016/6/5.
 */
public class LogUtils {
    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;
    private static final int NOTHING = 6;

    private static int LEVEL = VERBOSE;

    public static void v(String tag, String msg){
        if(LEVEL <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg){
        if(LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg){
        if(LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg){
        if(LEVEL <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if(LEVEL <= ERROR) {
            Log.e(tag, msg);
        }
    }


}
