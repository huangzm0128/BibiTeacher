package pype.mingming.bibiteacher.utils;

import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理创建的activity
 * Created by mingming on 2016/6/3.
 */
public class ActivityUtils {
    public static List<FragmentActivity> activities = new ArrayList<>();

    /**
     * 将activity添加到activities列表中
     * @param activity 要添加的activity
     */
    public static void addActivity(FragmentActivity activity){
        activities.add(activity);
    }

    /**
     * 从activities列表中移除activity
     * @param activity 要移除的activity
     */
    public static void removeActivity(FragmentActivity activity){
        activities.remove(activity);
    }

    /**
     * 移除activities列表中的所以activity
     */
    public static void finishAll(){
        for(FragmentActivity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
