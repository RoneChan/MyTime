package com.example.mytime;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyTime implements Serializable {
    private String title, year, month, day, remark;
    private String tag = "5", reset = "4";
    private String timeImgPath = "";

    public MyTime(String title, String year, String month, String day, String remark, String tag, String reset) {
        this.title = title;
        this.year = year;
        this.month = month;
        this.day = day;
        this.remark = remark;
        this.tag = tag;
        this.reset = reset;
    }

    public MyTime(String title, String year, String month, String day, String remark, String tag, String reset, String timeImgPath) {
        this.title = title;
        this.year = year;
        this.month = month;
        this.day = day;
        this.remark = remark;
        this.tag = tag;
        this.reset = reset;
        this.timeImgPath = timeImgPath;
    }

    public String getTimeImgPath() {
        return timeImgPath;
    }

    public void setTimeImgPath(String timeImgPath) {
        this.timeImgPath = timeImgPath;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getReset() {
        return reset;
    }

    public void setReset(String reset) {
        this.reset = reset;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {

        this.day = day;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public static long CalculateLastDay(MyTime time) {
        /*
         *计算相隔天数
         */
        long lastday = 0;
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String now = year + "-" + month + "-" + day;
        String setTime = time.getYear() + "-" + time.getMonth() + "-" + time.getDay();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date nowDate = df.parse(now);
            Date setTimeDate = df.parse(setTime);
            lastday = (long) ((setTimeDate.getTime() - nowDate.getTime()) / (60 * 60 * 1000 * 24));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lastday;
    }
}
