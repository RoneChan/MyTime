package com.example.mytime.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mytime.FileDataStream;
import com.example.mytime.MyTime;
import com.example.mytime.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private MyHomeFragmentAdapter myHomeFragmentAdapter;
    public static ArrayList<MyTime> times;
    ListView listView;

    private FileDataStream fileDataSource;

    //在关闭正常程序时，保存数据
    @Override
    public void onDestroy() {
        fileDataSource.setMyTimes(times);
        fileDataSource.save();
        super.onDestroy();
    }

    //在程序被kill时，保存好数据
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
     //   fileDataSource.setMyTimes(times);
       // fileDataSource.save();
    }

    @Override
    public void onResume() {
        super.onResume();
        //通知是适配器更新页面
        myHomeFragmentAdapter.notifyDataSetChanged();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listView = view.findViewById(R.id.ls_home_fragment);
        fileDataSource = new FileDataStream(container.getContext());
        //从文件总读取数据
        times = fileDataSource.load();

        myHomeFragmentAdapter = new MyHomeFragmentAdapter(container.getContext(), R.layout.fragment_home_item, times);
        listView.setAdapter(myHomeFragmentAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(container.getContext(), TimeDetail.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        return view;
    }

    public class MyHomeFragmentAdapter extends ArrayAdapter<MyTime> {

        int resource;

        public MyHomeFragmentAdapter(@NonNull Context context, int resource, @NonNull List<MyTime> objects) {
            super(context, resource);
            this.resource = resource;
        }

        @Nullable
        @Override
        public MyTime getItem(int position) {
            return times.get(position);
        }

        @Override
        public int getCount() {
            return times.size();
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
                    //System.out.println((nowDate.getTime()-setTimeDate.getTime())/(60*60*1000*24));
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

}