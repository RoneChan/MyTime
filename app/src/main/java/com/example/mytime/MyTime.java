package com.example.mytime;

import android.graphics.Bitmap;

import java.io.Serializable;

public class MyTime implements Serializable {
    private String title,year,month,day,remark;
    private String tag,reset;
    private String timeImgPath="";

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
        this.timeImgPath=timeImgPath;
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

    public void setDay(String dayday) {
        this.day = day;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
