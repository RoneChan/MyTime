package com.example.mytime.ui.vocation;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mytime.control.MyFragmentAdapter;
import com.example.mytime.MyTime;
import com.example.mytime.R;
import com.example.mytime.ui.home.TimeDetail;

import java.util.ArrayList;

import static com.example.mytime.ui.home.HomeFragment.times;


public class VocationFragment extends Fragment {

    private MyFragmentAdapter myWorkFragmentAdapter;
    ListView ls_vocation;
    private ArrayList<MyTime> workTimes = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vocation, container, false);
        for (MyTime time : times) {
            String tag = time.getTag();
            if (tag.equals("3")) {
                workTimes.add(time);
            }
        }

        myWorkFragmentAdapter = new MyFragmentAdapter(container.getContext(), R.layout.fragment_home_item, workTimes);
        ls_vocation = view.findViewById(R.id.ls_vocation);
        ls_vocation.setAdapter(myWorkFragmentAdapter);
        ls_vocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), TimeDetail.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        return view;
    }

}
