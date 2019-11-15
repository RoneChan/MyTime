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

import java.util.ArrayList;
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
        fileDataSource.setMyTimes(times);
        fileDataSource.save();
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

    class MyHomeFragmentAdapter extends ArrayAdapter<MyTime> {

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
            }
            return convertView;
        }

        //存放复用的组件
        class ViewHolder {
            TextView title, data, remark;
            ImageView img;

        }
    }

}