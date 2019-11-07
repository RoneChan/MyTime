package com.example.mytime.ui.AddNewTime;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;

import java.util.Calendar;

public class NewTime extends AppCompatActivity {

    ConstraintLayout data, reset, image, add_table;
    TextView data2, reset2,add_table2;

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
        add_table2= findViewById(R.id.tv_add_table2);

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示日历选择
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(NewTime.this,R.style.ThemeDialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                data2.setText(year+"年"+month+"月"+dayOfMonth +"日");
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
                //弹出对话框
                final String items[] = {"每周", "每月", "每年", "自定义", "无"};
                AlertDialog dialog = new AlertDialog.Builder(NewTime.this)
                        .setIcon(R.drawable.ic_reset)//设置标题的图片
                        .setTitle("周期")//设置对话框的标题
                        .setSingleChoiceItems(items, 4, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which != 3) {
                                    reset2.setText(items[which]);
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
                                                    dialog.dismiss();
                                                }
                                            }).create();
                                    customDialog.show();
                                }
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
                                if (which != 4) {
                                    add_table2.setText(tableItems[which]);
                                } else {
                                    dialog.dismiss();
                                    View view = getLayoutInflater().inflate(R.layout.new_time_coustom_time, null);
                                    final EditText editText = (EditText) view.findViewById(R.id.et_custom_time_set);
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
                                                    dialog.dismiss();
                                                }
                                            }).create();
                                    customDialog.show();
                                }
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
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });

    }

}
