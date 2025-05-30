package com.teach.javafx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DateTimeTool Date数据转换操作类
 */
public class DateTimeTool {
    /**
     * formatDateTime 字串串转换日期
     */
    public static Date formatDateTime(String timeSrc, String f) {
        SimpleDateFormat sdFormat = new SimpleDateFormat(f);
        sdFormat.setLenient(true);
        try {
            if (timeSrc == null || timeSrc.trim().isEmpty())
                return null;

            return sdFormat.parse(timeSrc);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
            return null;
        }
    }

    /**
     *   parseDateTime 日期转换字符串
     */
    public static String parseDateTime(Date timeSrc) {
        return parseDateTime(timeSrc,"yyyy-MM-dd HH:mm:ss");
    }

    public static String parseDateTime(Date timeSrc, String f) {
        if (timeSrc == null)
            return null;
        SimpleDateFormat sdFormat = new SimpleDateFormat(f);
        return sdFormat.format(timeSrc);
    }

    /**
     * nextDay 后一天日期
     */
    public static Date nextDay(Date date) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.DAY_OF_MONTH, 1);
            return c1.getTime();
        } else
            return null;
    }

    /**
     * nextDay 后n天日期
     */
    public static Date nextDay(Date date, int num) {
        if (date != null) {
            if(num == 0)
                return date;
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.DAY_OF_MONTH, num);
            return c1.getTime();
        } else
            return null;
    }

    /**
     * prevDay 前一天日期
     */
    public static Date prevDay(Date date) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.DAY_OF_MONTH, -1);
            return c1.getTime();
        } else
            return null;

    }

    /**
     * prevDay 前n天日期
     */
    public static Date prevDay(Date date, int n) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.DAY_OF_MONTH, -n);
            return c1.getTime();
        } else
            return null;
    }

    /**
     * nextWeek 下一周日期
     */
    public static Date nextWeek(Date date) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.DAY_OF_MONTH, 7);
            return c1.getTime();
        } else
            return null;
    }

    /**
     *  其一周日期
     */
    public static Date prevWeek(Date date) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.DAY_OF_MONTH, -7);
            return c1.getTime();
        } else
            return null;
    }

    /**
     * nextMonth 下一月日期
     */
    public static Date nextMonth(Date date) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.MONTH, 1);
            return c1.getTime();
        } else
            return null;
    }

    /**
     * nextMonth 获得后n个月日期
     */
    public static Date nextMonth(Date date,int n) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.MONTH, n);
            return c1.getTime();
        } else
            return null;
    }

    /**
     * prevMonth 获得前一月的日期
     */
    public static Date prevMonth(Date date) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.MONTH, -1);
            return c1.getTime();
        } else
            return null;
    }

    /**
     * prevMonth 获得前n个月的日期
     */
    public static Date prevMonth(Date date, int n) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.MONTH, -n);
            return c1.getTime();
        } else
            return null;
    }

    /**
     * nextYear 获得后n年的日期
     */
    public static Date nextYear(Date date,int n) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.YEAR, n);
            return c1.getTime();
        } else
            return null;
    }

    /**
     * prevYear 获得前一年的日期
     */
    public static Date prevYear(Date date) {
        if (date != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.YEAR, -1);
            return c1.getTime();
        } else
            return null;
    }

    /**
     * getCurrentWeekDay 获得当前的星期几
     */
    public static int getCurrentWeekDay(){
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        date= nextDay(date);
        date= nextDay(date);
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static void main(String args[]){
        System.out.println(getCurrentWeekDay());

    }
}
