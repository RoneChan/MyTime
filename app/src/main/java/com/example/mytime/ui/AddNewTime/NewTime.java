package com.example.mytime.ui.AddNewTime;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.MainActivity;
import com.example.mytime.MyTime;
import com.example.mytime.R;
import com.example.mytime.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.mytime.ui.home.HomeFragment.times;

public class NewTime extends AppCompatActivity {
    public static final int CONTEXT_ITEM_NEW = 1;
    public static final int CONTEXT_ITEM_EDIT = 2;

    ConstraintLayout data, reset, image, add_table;
    TextView data2, reset2, add_table2;
    EditText et_title, et_remark;
    Button save;
    static int flag = -1;
    int position = -1;
    MyTime myTime = null;

    String myYear="", myMonth="", myDayOfMonth="";
    String chooseTag = "5", chooseReset = "4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏头部
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_time);

        init();


    }

    private void init() {
        data = findViewById(R.id.layout_data);
        reset = findViewById(R.id.layout_reset);
        image = findViewById(R.id.layout_image);
        add_table = findViewById(R.id.layout_add_table);
        data2 = findViewById(R.id.tv_data2);
        reset2 = findViewById(R.id.tv_reset2);
        add_table2 = findViewById(R.id.tv_add_table2);
        save = findViewById(R.id.btn_save);
        et_title = findViewById(R.id.et_title);
        et_remark = findViewById(R.id.et_remark);


        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        String title = intent.getStringExtra("title");

        if (position == -1) {
            //新建item
            flag = CONTEXT_ITEM_NEW;
        } else {
            flag = CONTEXT_ITEM_EDIT;
            myTime = times.get(position);
            et_title.setText(myTime.getTitle());
            et_remark.setText(myTime.getRemark());
            myYear = myTime.getYear();
            myMonth = myTime.getMonth();
            myDayOfMonth = myTime.getDay();
            data2.setText(myYear + "年" + myMonth + "月" + myDayOfMonth + "日");
        }

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示日历选择
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(NewTime.this, R.style.ThemeDialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                myYear = year + "";
                                myMonth = month + "";
                                myDayOfMonth = dayOfMonth + "";
                                data2.setText(myYear + "年" + myMonth + "月" + myDayOfMonth + "日");
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                dialog.show();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出tag对话框
                final String items[] = {"每周", "每月", "每年", "自定义", "无"};

                AlertDialog dialog = new AlertDialog.Builder(NewTime.this)
                        .setIcon(R.drawable.ic_reset)//设置标题的图片
                        .setTitle("周期")//设置对话框的标题
                        .setSingleChoiceItems(items, 4, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chooseTag = which + "";
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        //自定义对话框
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Integer.parseInt(chooseReset) != 3) {
                                    reset2.setText(items[Integer.parseInt(chooseReset)]);
                                } else {
                                    dialog.dismiss();
                                    View view = getLayoutInflater().inflate(R.layout.new_time_coustom_time, null);
                                    final EditText editText = (EditText) view.findViewById(R.id.et_custom_time_set);
                                    AlertDialog customDialog = new AlertDialog.Builder(NewTime.this)
                                            .setIcon(R.drawable.ic_reset)//设置标题的图片
                                            .setTitle("周期")//设置对话框的标题
                                            .setView(view)
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String content = editText.getText().toString();
                                                    reset2.setText(content);
                                                    chooseReset = content;
                                                    dialog.dismiss();
                                                }
                                            }).create();
                                    customDialog.show();
                                }
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        add_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框
                final String tableItems[] = {"学习", "生日", "工作", "节假日", "自定义", "无"};
                AlertDialog dialog = new AlertDialog.Builder(NewTime.this)
                        .setIcon(R.drawable.ic_table)//设置标题的图片
                        .setTitle("标签")//设置对话框的标题
                        .setSingleChoiceItems(tableItems, 5, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chooseTag = which + "";
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Integer.parseInt(chooseTag) != 4) {
                                    add_table2.setText(tableItems[Integer.parseInt(chooseTag)]);
                                } else {
                                    dialog.dismiss();
                                    View view = getLayoutInflater().inflate(R.layout.new_time_coustom_time2, null);

                                    final EditText editText = (EditText) view.findViewById(R.id.et_custom_time_tag);

                                    AlertDialog customDialog = new AlertDialog.Builder(NewTime.this)
                                            .setIcon(R.drawable.ic_table)//设置标题的图片
                                            .setTitle("标签")//设置对话框的标题
                                            .setView(view)
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String content = editText.getText().toString();
                                                    add_table2.setText(content);
                                                    chooseTag = content;
                                                    dialog.dismiss();
                                                }
                                            }).create();
                                    customDialog.show();
                                }
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString();
                String remark = et_remark.getText().toString();
                if(title.equals("")){
                   Toast.makeText(NewTime.this,"请输入标题", Toast.LENGTH_SHORT).show();
                }else if ( myYear.equals("") || myMonth.equals("") || myDayOfMonth.equals("")) {
                    Toast.makeText(NewTime.this,"请选择日期", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(NewTime.this, HomeFragment.class);
                    intent.putExtra("title", title);
                    intent.putExtra("remark", remark);
                    intent.putExtra("year", myYear);
                    intent.putExtra("month", myMonth);
                    intent.putExtra("day", myDayOfMonth);
                    intent.putExtra("reset", chooseReset);
                    intent.putExtra("reset", chooseTag);
                    intent.putExtra("position", position);
                    setResult(1, intent);
                    finish();
                }
            }
        });

    }

}
