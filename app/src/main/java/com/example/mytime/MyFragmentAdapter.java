package com.example.mytime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.example.mytime.ui.home.HomeFragment.times;

public class MyFragmentAdapter extends ArrayAdapter<MyTime> {

    int resource;
    ArrayList<MyTime> myTimes=new ArrayList<MyTime>();

    public MyFragmentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MyTime> objects) {
        super(context, resource);
        this.resource = resource;
        myTimes=objects;
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
            viewHolder.last_day=(TextView) convertView.findViewById(R.id.tv_last_day);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //设置控件的值
        MyTime time = this.getItem(position);
        if (time != null) {
            viewHolder.title.setText(time.getTitle());
            viewHolder.data.setText(time.getYear() + "年" + time.getMonth() + "月" + time.getDay() + "日");
            // img.setImageResource(time.getPictureId());
            viewHolder.remark.setText(time.getRemark());

            /*
             *计算相隔天数
             */
            Calendar calendar = Calendar.getInstance();
            //获取系统的日期
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            String now=year+"-"+month+"-"+day;
            String setTime=time.getYear()+"-"+time.getMonth()+"-"+time.getDay();
            DateFormat df=new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date nowDate=df.parse(now);
                Date setTimeDate=df.parse(setTime);
                long lastday= (long) ((setTimeDate.getTime()-nowDate.getTime())/(60*60*1000*24));
                if (lastday >= 0) {
                    viewHolder.last_day.setText("只剩\n"+lastday+"天");
                }else{
                    viewHolder.last_day.setText("已经\n"+Math.abs(lastday)+"天");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }
    //存放复用的组件
    class ViewHolder {
        TextView title, data, remark,last_day;
    }
}

