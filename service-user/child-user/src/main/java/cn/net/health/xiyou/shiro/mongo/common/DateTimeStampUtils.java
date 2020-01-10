package cn.net.health.xiyou.shiro.mongo.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author xiyou
 * @version 1.2
 * @date 2019/12/25 10:31
 */
public class DateTimeStampUtils {
    // 获取当天0点时间戳 Created by Wenhui Huang 2019/02/28
    public static Date todayZero() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    // 获取下一天0点时间戳 Created by Wenhui Huang 2019/02/28
    public static Date nextDayZero() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    // 获取下一天0点时间戳 Created by Wenhui Huang 2019/02/28
    public static Date nextTenDaysZero() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    // 获取当天0点时间戳 Created by Wenhui Huang 2019/02/28
    public static Long dayTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        return time;
    }

    // 获取下一天0点时间戳 Created by Wenhui Huang 2019/02/28
    public static Long nextDayTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        return time;
    }

    // 获取当月0点时间戳 Created by Wenhui Huang 2019/02/28
    public static Long monthTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        return time;
    }

    // 获取当年0点时间戳 Created by Wenhui Huang 2019/02/28
    public static Long yearTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.DATE, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        return time;
    }

    public static void main(String[] args) {


        Long time1 = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//要转换的时间格式
        Long time2 = dayTimeInMillis();
        Long timeNext = nextDayTimeInMillis();
        Long time3 = monthTimeInMillis();
        Long time4 = yearTimeInMillis();

        System.out.println("time1: " + sdf.format(time1) + " " + time1);
        System.out.println("timeNext: " + sdf.format(timeNext) + " " + timeNext);
        System.out.println("time2: " + sdf.format(time2) + " " + time2);
        System.out.println("time3: " + sdf.format(time3) + " " + time3);
        System.out.println("time4: " + sdf.format(time4) + " " + time4);
        // System.out.println("time5: "+sdf.format(1551431981748L));


    }
}
