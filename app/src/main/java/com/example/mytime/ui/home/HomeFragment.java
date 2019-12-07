package com.example.mytime.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.mytime.control.FileDataStream;
import com.example.mytime.MyTime;
import com.example.mytime.R;
import com.example.mytime.control.MyFragmentAdapter;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private MyFragmentAdapter myHomeFragmentAdapter;
    //private MyHomeFragmentAdapter myHomeFragmentAdapter;
    public static ArrayList<MyTime> times=new ArrayList<MyTime>() ;
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
        if(times.size()==0)
          times = fileDataSource.load();

        myHomeFragmentAdapter = new MyFragmentAdapter(container.getContext(), R.layout.fragment_home_item, times);
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
}