package pype.mingming.bibiteacher.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 使用ToastUtils管理Toast
 * Created by mingming on 2016/6/8.
 */
public class ToastUtils {
    private static Toast mToast;

    public static void showToast(Context context, String msg, int duration){
        if(mToast == null){
            mToast = Toast.makeText(context, msg, duration);
        }else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void clearToast(){
        if(mToast != null){
            mToast.cancel();
        }
    }
}
