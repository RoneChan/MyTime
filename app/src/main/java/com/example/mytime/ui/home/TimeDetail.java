package com.example.mytime.ui.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.IslamicCalendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.MyTime;
import com.example.mytime.R;
import com.example.mytime.ui.AddNewTime.NewTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import static com.example.mytime.MainActivity.TIME_NEW_REQUEST_CODE;
import static com.example.mytime.ui.AddNewTime.NewTime.TIME_OK;
import static com.example.mytime.ui.about.AboutFragment.CAMERA_REQUEST_CODE;
import static com.example.mytime.ui.home.HomeFragment.times;

public class TimeDetail extends AppCompatActivity {
    static int NEW_ITME = -1;
    public final static int GALLERY_REQUEST_CODE = 202;
    public final static int TIME_EDIT_REQUEST_CODE = 200;

    String title, year, month, day, remark;
    String tag, reset;
    TextView tv_title, tv_date, tv_remark, tv_last_day;
    ImageView btn_edit, btn_delete, btn_change_background;
    Bitmap timeVitmap;
    ImageView imageView;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏头部
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_time_detail);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);

        Init(position);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeDetail.this, NewTime.class);
                intent.putExtra("position", position);
                startActivityForResult(intent, TIME_EDIT_REQUEST_CODE);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除用户选择的图片
                File file = new File(times.get(position).getTimeImgPath());
                file.delete();
                times.remove(position);
                finish();
            }
        });

        btn_change_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(TimeDetail.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(TimeDetail.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
                    // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
                    // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
                    ActivityCompat.requestPermissions(TimeDetail.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                } else { //权限已经被授予，在这里直接写要执行的相应方法即可
                    Intent imgIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    imgIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    imgIntent.setType("image/*");
                    startActivityForResult(imgIntent, GALLERY_REQUEST_CODE);
                }
            }
        });
    }

    void Init(int position) {
        MyTime myTime = times.get(position);
        //从List数组中获取相应的数据
        title = myTime.getTitle();
        year = myTime.getYear();
        month = myTime.getMonth();
        day = myTime.getDay();
        remark = myTime.getRemark();
        tag = myTime.getTag();
        reset = myTime.getReset();

        tv_title = findViewById(R.id.tv_detail_title);
        tv_date = findViewById(R.id.tv_detail_date);
        tv_remark = findViewById(R.id.tv_detail_remark);
        tv_last_day = findViewById(R.id.tv_last_day);
        btn_edit = findViewById(R.id.btn_home_edit);
        btn_delete = findViewById(R.id.btn_home_delete);
        btn_change_background = findViewById(R.id.btn_home_change_background);
        imageView = findViewById(R.id.img_detail_background);


        tv_title.setText(title);
        tv_date.setText("距离" + year + "年" + month + "月" + day + "日");
        tv_remark.setText(remark);
        long lastDay = MyTime.CalculateLastDay(myTime);
        if (lastDay >= 0) {
            tv_last_day.setText("只剩" + lastDay + "天");
        } else {

            tv_last_day.setText("已经" + Math.abs(lastDay) + "天");
        }
        //tv_last_day.setText();

        try{


        //s设置用户保存的图片
        if (!(myTime.getTimeImgPath()).equals("")) {
            timeVitmap = BitmapFactory.decodeFile(myTime.getTimeImgPath());
            int bwidth = timeVitmap.getWidth();
            int bHeight = timeVitmap.getHeight();
            DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            int height = width * bHeight / bwidth;
            ViewGroup.LayoutParams para = imageView.getLayoutParams();
            para.height = height;
            imageView.setLayoutParams(para);
            imageView.setImageBitmap(timeVitmap);
        }
        }catch (Exception e){
            Toast.makeText(this, "图片已损坏", Toast.LENGTH_SHORT).show();
        }
    }

    public void getPic(Uri url) {
        try {
            if (!url.equals("")) {
                //获取被选图片的url
                String realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(this, url);
                //将图片转化为Bitmap
                Bitmap bitmap = BitmapFactory.decodeFile(realPathFromUri);

                //获取图片长宽
                int bwidth = bitmap.getWidth();
                int bHeight = bitmap.getHeight();
                //获取屏幕高、宽，让图片按比例放大，使其宽度等于屏幕大小
                DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels;
                int height = width * bHeight / bwidth;
                ViewGroup.LayoutParams para = imageView.getLayoutParams();
                para.height = height;
                //设置imageview高度
                imageView.setLayoutParams(para);

                imageView.setImageBitmap(bitmap);
                //保存bitMap
                times.get(position).setTimeImgPath(saveBitmap(this, bitmap, times.get(position).getTitle()));
            }
        }catch (Exception e){
            Toast.makeText(this, "图片已损坏", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TIME_NEW_REQUEST_CODE && resultCode == TIME_OK) {
            //成功新建倒计时
            int position = data.getIntExtra("position", -1);

            //position==NEW_ITME,代表新建倒计时项
            if (position == NEW_ITME) {
                MyTime time = new MyTime(data.getStringExtra("title"), data.getStringExtra("year")
                        , data.getStringExtra("month"), data.getStringExtra("day"), data.getStringExtra("remark")
                        , data.getStringExtra("tag"), data.getStringExtra("reset"));
                times.add(time);
            }
        } else if (requestCode == TIME_EDIT_REQUEST_CODE && resultCode == TIME_OK) {
            //此时为修改倒计时项
            //String str=data.getStringExtra("day");
            times.get(position).setTitle(data.getStringExtra("title"));
            times.get(position).setRemark(data.getStringExtra("remark"));
            times.get(position).setYear(data.getStringExtra("year"));
            times.get(position).setMonth(data.getStringExtra("month"));
            times.get(position).setDay(data.getStringExtra("day"));
            times.get(position).setTag(data.getStringExtra("tag"));
            times.get(position).setReset(data.getStringExtra("reset"));
            times.get(position).setTimeImgPath(data.getStringExtra("image"));

            Intent intent = new Intent(TimeDetail.this, HomeFragment.class);
            finish();
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (data != null) {
                getPic(data.getData());
            }

        }
    }

    //保存bitmap到本地
    public static String saveBitmap(Context context, Bitmap mBitmap, String title) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //存储路径
            savePath = "/sdcard/Mytime/pic/";
        } else {
            savePath = context.getApplicationContext().getFilesDir().getAbsolutePath() + "/Mytime/pic/";
        }
        try {
            //设置图片的保存路径
            filePic = new File(savePath + title + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            //压缩文件
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //返回存储图片的绝对路径
        return filePic.getAbsolutePath();
    }
}
