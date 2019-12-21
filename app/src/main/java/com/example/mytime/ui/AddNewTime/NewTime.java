package com.example.mytime.ui.AddNewTime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.MyTime;
import com.example.mytime.R;
import com.example.mytime.ui.home.HomeFragment;

import java.io.FileDescriptor;
import java.util.Calendar;

import static com.example.mytime.control.FileDataStream.saveBitmap;
import static com.example.mytime.MyTime.CalculateLastDay;
import static com.example.mytime.ui.home.HomeFragment.times;
import static com.example.mytime.ui.home.TimeDetail.CAMERA_REQUEST_CODE;
import static com.example.mytime.ui.home.TimeDetail.GALLERY_REQUEST_CODE;

public class NewTime extends AppCompatActivity {
    public static final int CONTEXT_ITEM_NEW = 1;
    public static final int CONTEXT_ITEM_EDIT = 2;
    public static final int TIME_OK = 100;
    final String resetItems[] = {"每周", "每月", "每年", "自定义", "无"};
    final String tableItems[] = {"学习", "生日", "工作", "节假日", "自定义", "无"};

    ConstraintLayout data, reset, image, add_table;
    TextView data2, reset2, add_table2, image2;
    EditText et_title, et_remark;
    Button save;
    String oldPicPath = "";

    int flag = -1, havePicFlag = 0;
    int position = -1;

    MyTime myTime = null;
    Bitmap bitmap;
    String myYear = "", myMonth = "", myDayOfMonth = "";
    String chooseTag = "5", chooseReset = "-1";
    int chooseResetItem=4,chooseTagItem=5;
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
        image2 = findViewById(R.id.tv_image2);
        save = findViewById(R.id.btn_save);
        et_title = findViewById(R.id.et_title);
        et_remark = findViewById(R.id.et_remark);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);

        if (position == -1) {
            //新建item
            flag = CONTEXT_ITEM_NEW;
        } else {
            //修改item
            flag = CONTEXT_ITEM_EDIT;
            myTime = times.get(position);
            et_title.setText(myTime.getTitle());
            et_remark.setText(myTime.getRemark());

            //获取旧的信息
            myYear = myTime.getYear();
            myMonth = myTime.getMonth();
            myDayOfMonth = myTime.getDay();
            chooseTag = myTime.getTag();
            chooseReset = myTime.getReset();
            oldPicPath = myTime.getTimeImgPath();

            switch (chooseReset) {
                case "7":
                    chooseResetItem = 0;
                    break;
                case "30":
                    chooseResetItem = 1;
                    break;
                case "365":
                    chooseResetItem = 2;
                    break;
                case "-1":
                    chooseResetItem=4;
                    break;
                default:
                    chooseResetItem = 3;
                    break;
            }

            String test=resetItems[chooseResetItem];
            data2.setText(myYear + "年" + myMonth + "月" + myDayOfMonth + "日");

            if(chooseResetItem==3)
                reset2.setText(chooseReset + "天");
            else
                reset2.setText(resetItems[chooseResetItem]);

            switch (chooseTag) {
                case "0": case "1": case "2": case "3": case "5":
                    chooseTagItem = Integer.parseInt(chooseTag);
                    break;
                default:
                    chooseTagItem = 4;
                    break;
            }
            if(chooseTagItem!=4)
                add_table2.setText(tableItems[chooseTagItem]);
            else
                add_table2.setText(chooseTag);
            if (!oldPicPath.equals(""))
                image2.setText("已选择图片");
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
                                myMonth = (month + 1) + "";
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
            int chooseItem;
            @Override
            public void onClick(View v) {
                //弹出tag对话框
                AlertDialog dialog = new AlertDialog.Builder(NewTime.this)
                        .setIcon(R.drawable.ic_reset)//设置标题的图片
                        .setTitle("周期")//设置对话框的标题
                        .setSingleChoiceItems(resetItems, chooseResetItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               chooseReset=which+"";
                               chooseItem=which;
                                switch (which) {
                                    case 0:
                                        chooseReset = "7";
                                        break;
                                    case 1:
                                        chooseReset = "30";
                                        break;
                                    case 2:
                                        chooseReset = "365";
                                        break;
                                    case 4:  //无
                                        chooseReset="-1";
                                        break;
                                    default: //自定义
                                        chooseReset = which + "";
                                        break;
                                }
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
                                if (chooseItem != 3) {
                                    reset2.setText(resetItems[chooseItem]);
                                    //{"每周", "每月", "每年", "自定义", "无"};
                                } else {
                                    dialog.dismiss();
                                    View view = getLayoutInflater().inflate(R.layout.new_time_coustom_time, null);
                                    final EditText et_reset = (EditText) view.findViewById(R.id.et_custom_time_reset);

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
                                                    String content = et_reset.getText().toString();
                                                    reset2.setText(content + "天");
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
                if (ContextCompat.checkSelfPermission(NewTime.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(NewTime.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
                    // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
                    // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
                    ActivityCompat.requestPermissions(NewTime.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                } else {
                    //权限已经被授予，在这里直接写要执行的相应方法即可
                    Intent imgIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    imgIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    imgIntent.setType("image/*");
                    startActivityForResult(imgIntent, GALLERY_REQUEST_CODE);
                }
            }
        });

        add_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框
                AlertDialog dialog = new AlertDialog.Builder(NewTime.this)
                        .setIcon(R.drawable.ic_table)//设置标题的图片
                        .setTitle("标签")//设置对话框的标题
                        .setSingleChoiceItems(tableItems, chooseTagItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chooseTag = which + "";
                                chooseTagItem=which;
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
                                if (chooseTagItem != 4) {
                                    add_table2.setText(tableItems[chooseTagItem]);
                                } else {
                                    dialog.dismiss();
                                    View view = getLayoutInflater().inflate(R.layout.new_time_coustom_time2, null);

                                    final EditText et_tag = (EditText) view.findViewById(R.id.et_custom_time_tag);

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
                                                    String content = et_tag.getText().toString();
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
                if (title.equals("")) {
                    Toast.makeText(NewTime.this, "请输入标题", Toast.LENGTH_SHORT).show();
                } else if (myYear.equals("") || myMonth.equals("") || myDayOfMonth.equals("")) {
                    Toast.makeText(NewTime.this, "请选择日期", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(NewTime.this, HomeFragment.class);

                    if (havePicFlag == 1) {
                        String imgPath = saveBitmap(NewTime.this, bitmap, title);
                        //修改图片
                        intent.putExtra("image", imgPath);
                    } else {
                        if (oldPicPath.equals(""))   //表示新建time
                            intent.putExtra("image", "");
                        else
                            intent.putExtra("image", oldPicPath); //表示未修改图片
                    }

                    intent.putExtra("title", title);
                    intent.putExtra("remark", remark);
                    intent.putExtra("year", myYear);
                    intent.putExtra("month", myMonth);
                    intent.putExtra("day", myDayOfMonth);
                    intent.putExtra("reset", chooseReset);
                    intent.putExtra("tag", chooseTag);
                    intent.putExtra("position", position);

                    setResult(TIME_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (data != null) {
                ParcelFileDescriptor parcelFileDescriptor = null;
                try {
                    parcelFileDescriptor = this.getContentResolver().openFileDescriptor(data.getData(), "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                havePicFlag = 1;  //havePicFlag=1标志新建time时选择了图片，或者在修改的时候选择了新的图片
                image2.setText("已选择图片");
            } else {
                Toast.makeText(this, "图片损坏，请重新选择", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
