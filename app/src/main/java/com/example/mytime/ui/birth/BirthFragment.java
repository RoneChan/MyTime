package com.example.mytime.ui.birth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mytime.MyFragmentAdapter;
import com.example.mytime.MyTime;
import com.example.mytime.R;
import com.example.mytime.ui.home.TimeDetail;

import java.util.ArrayList;

import static com.example.mytime.ui.home.HomeFragment.times;

public class BirthFragment extends Fragment {

    private MyFragmentAdapter myWorkFragmentAdapter;
    ListView ls_birth;
    private ArrayList<MyTime> workTimes = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_birth, container, false);
        for (MyTime time : times) {
            String tag = time.getTag();
            if (tag.equals("1")) {
                workTimes.add(time);
            }
        }

        myWorkFragmentAdapter = new MyFragmentAdapter(container.getContext(), R.layout.fragment_home_item, workTimes);
        ls_birth = view.findViewById(R.id.ls_birth);
        ls_birth.setAdapter(myWorkFragmentAdapter);

        ls_birth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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