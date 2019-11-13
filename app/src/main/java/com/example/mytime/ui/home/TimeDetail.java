package com.example.mytime.ui.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytime.MyTime;
import com.example.mytime.R;
import com.example.mytime.ui.AddNewTime.NewTime;

import java.util.ArrayList;


import static com.example.mytime.ui.home.HomeFragment.times;
public class TimeDetail extends AppCompatActivity implements View.OnClickListener {

    String title,year,month,day,remark;
    String tag,reset;
    TextView tv_title,tv_date,tv_remark;
    ImageView btn_edit;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_detail);

        Intent intent = getIntent();
        position=intent.getIntExtra("position",-1);
        /*
        title=intent.getStringExtra("title");
        year=intent.getStringExtra("year");
        month=intent.getStringExtra("month");
        day=intent.getStringExtra("day");
        remark=intent.getStringExtra("remark");
        tag=intent.getStringExtra("tag");
        reset=intent.getStringExtra("reset");

         */

        MyTime myTime=times.get(position);
        title=myTime.getTitle();
        year=myTime.getYear();
        month=myTime.getMonth();
        day=myTime.getDay();
        remark=myTime.getRemark();

        tv_title=findViewById(R.id.tv_detail_title);
        tv_date=findViewById(R.id.tv_detail_date);
        tv_remark=findViewById(R.id.tv_detail_remark);
        btn_edit=findViewById(R.id.btn_home_edit);

        tv_title.setText(title);
        tv_date.setText(year+"年"+month+"月"+day+"日");
        tv_remark.setText(remark);

        btn_edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this, NewTime.class);
        /*
        intent.putExtra("title",title);
        intent.putExtra("year",year);
        intent.putExtra("day",day);
        intent.putExtra("month",month);
        intent.putExtra("remark",remark);
        intent.putExtra("tag",tag);
        intent.putExtra("reset",reset);

         */

        intent.putExtra("position",position);
        startActivityForResult(intent, 1);
       //

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==1){
            int position=data.getIntExtra("position",-1);

            if(position==-1){
                MyTime time =new MyTime(data.getStringExtra("title"),data.getStringExtra("year")
                        ,data.getStringExtra("month"),data.getStringExtra("day"),data.getStringExtra("remark")
                ,data.getStringExtra("tag"),data.getStringExtra("reset"));
                times.add(time);

            }else{
                times.get(position).setTitle(data.getStringExtra("title"));
                times.get(position).setRemark(data.getStringExtra("remark"));
                times.get(position).setYear(data.getStringExtra("year"));
                times.get(position).setMonth(data.getStringExtra("month"));
                times.get(position).setDay(data.getStringExtra("day"));
                times.get(position).setTag(data.getStringExtra("tag"));
                times.get(position).setReset(data.getStringExtra("reset"));
                Intent intent=new Intent(TimeDetail.this,HomeFragment.class);
                //startActivity(intent);
                finish();
            }

        }
        finish();
    }


}
