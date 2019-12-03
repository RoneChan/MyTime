package com.example.mytime.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mytime.MyTime;
import com.example.mytime.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.mytime.MyTime.CalculateLastDay;

public class MyFragmentAdapter extends ArrayAdapter<MyTime> {

    int resource;
    ArrayList<MyTime> myTimes = new ArrayList<MyTime>();

    public MyFragmentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MyTime> objects) {
        super(context, resource);
        this.resource = resource;
        myTimes = objects;
    }

    public MyFragmentAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Nullable
    @Override
    public MyTime getItem(int position) {
        return myTimes.get(position);
    }

    @Override
    public int getCount() {
        return myTimes.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            //若是第一次创建
            convertView = LayoutInflater.from(this.getContext()).inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_home_title);
            viewHolder.data = (TextView) convertView.findViewById(R.id.tv_home_data);
            viewHolder.remark = (TextView) convertView.findViewById(R.id.tv_home_remark);
            viewHolder.last_day = (TextView) convertView.findViewById(R.id.tv_last_day);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //设置控件的值
        MyTime time = this.getItem(position);
        if (time != null) {
            long lastDay = CalculateLastDay(time);
            if (lastDay < 0) {
                //final String resetItems[] = {"每周", "每月", "每年", "自定义", "无"};
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(time.getYear()));
                calendar.set(Calendar.MONTH, Integer.parseInt(time.getMonth()) - 1);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(time.getDay()));

                String myYear = "", myMonth = "", myDayOfMonth = "";
                switch (time.getReset()) {
                    case "7"://每周重复
                    {
                        int delayDay = (int) Math.abs(lastDay) / 7 + 1;//计算往后推迟多少个星期
                        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + (delayDay * 7));
                        //获取更新后的日期
                        myYear = calendar.get(Calendar.YEAR) + "";
                        myMonth = calendar.get(Calendar.MONTH) + 1 + "";
                        myDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) + "";
                        myTimes.get(position).setYear(myYear);
                        myTimes.get(position).setMonth(myMonth);
                        myTimes.get(position).setDay(myDayOfMonth);
                    }
                    break;
                    case "30"://每月重复
                        int delayMoth = (int) Math.abs(lastDay) / 28 + 1;//计算往后推迟多少月
                        String passMonth = time.getMonth();
                        String newMonth = (Integer.parseInt(passMonth) + delayMoth) + "";
                        myTimes.get(position).setMonth(newMonth);
                        break;
                    case "365"://每年重复
                        int delayYear = (int) Math.abs(lastDay) / 365 + 1;//计算往后推迟多少年
                        String passYear = time.getYear();
                        String newYear = (Integer.parseInt(passYear) + delayYear) + "";
                        myTimes.get(position).setYear(newYear);
                        break;
                    case "-1"://无重复
                        break;
                    default://自定义
                    {
                        int delayDay = (int) Math.abs(lastDay) / Integer.parseInt(time.getReset()) + 1;//计算往后推迟多少个周期
                        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + delayDay*Integer.parseInt(time.getReset()));
                        //获取更新后的日期
                        myYear = calendar.get(Calendar.YEAR) + "";
                        myMonth = calendar.get(Calendar.MONTH) + 1+"";
                        myDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) + "";
                        myTimes.get(position).setYear(myYear);
                        myTimes.get(position).setMonth(myMonth);
                        myTimes.get(position).setDay(myDayOfMonth);
                    }
                    break;
                }
            }
            lastDay = CalculateLastDay(myTimes.get(position));
            if (lastDay >= 0) {
                viewHolder.last_day.setText("只剩\n" + lastDay + "天");
            } else {
                viewHolder.last_day.setText("已经\n" + Math.abs(lastDay) + "天");
            }
            viewHolder.title.setText(time.getTitle());
            viewHolder.data.setText(myTimes.get(position).getYear() + "年" + myTimes.get(position).getMonth() + "月" + myTimes.get(position).getDay() + "日");
            viewHolder.remark.setText(time.getRemark());
        }
        return convertView;
    }

    //存放复用的组件
    class ViewHolder {
        TextView title, data, remark, last_day;
    }
}

