package pype.mingming.bibiteacher.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wushaohongly on 2016/8/5.
 */
public class TimeUtils {
    public static String dataLongToSNS(long time){

        String format = "MM-dd";

        long now = System.currentTimeMillis();
        long diff = now - time;
        diff = diff/1000;// 秒

        if(diff<0){
            return dateLongToString(time,format);
        }

        if(diff<30){ // 30秒
            return "刚刚";
        }
        if(diff<60){
            return diff + "秒前";
        }
        if(diff<3600){
            return (diff/60) + "分钟前";
        }
        //获取今天凌晨时间
        long todayStart = getMoring(new Date()).getTime();
        if(time>=todayStart){// 今天
            return (diff/3600) + "小时前";
        }
        if(time<todayStart && time >= todayStart-86400000){
            return "昨天 " + dateLongToString(time, "HH:mm");
        }

        return dateLongToString(time,format);
    }
    //获取今天凌晨的时间
    private static Date getMoring(Date date){
        Calendar calendar  = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    public static String dateLongToString(long time){
        return dateLongToString(time,null);
    }
    public static String dateLongToString(long time,String format){
        if(time<=0){
            return "Empty";
        }
        DateFormat format2 = new SimpleDateFormat(format);
        String dateString = format2.format(new Date(time));
        return dateString;
    }
}
