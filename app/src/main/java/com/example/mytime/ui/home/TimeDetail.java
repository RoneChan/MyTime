package com.example.mytime.ui.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.MyTime;
import com.example.mytime.R;
import com.example.mytime.ui.AddNewTime.NewTime;
import com.example.mytime.ui.gallery.RealPathFromUriUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static com.example.mytime.MainActivity.TIME_NEW_REQUEST_CODE;
import static com.example.mytime.ui.AddNewTime.NewTime.TIME_OK;
import static com.example.mytime.ui.gallery.GalleryFragment.CAMERA_REQUEST_CODE;
import static com.example.mytime.ui.home.HomeFragment.times;

public class TimeDetail extends AppCompatActivity {

    String title, year, month, day, remark;
    String tag, reset;
    TextView tv_title, tv_date, tv_remark;
    ImageView btn_edit, btn_delete, btn_change_background;
    Bitmap timeVitmap;

    static int NEW_ITME = -1;
    public final static int GALLERY_REQUEST_CODE = 202;
    public final static int TIME_EDIT_REQUEST_CODE = 200;
    public final static int PIC_CUT_REQUEST_CODE = 199;

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
        /*
        title=intent.getStringExtra("title");
        year=intent.getStringExtra("year");
        month=intent.getStringExtra("month");
        day=intent.getStringExtra("day");
        remark=intent.getStringExtra("remark");
        tag=intent.getStringExtra("tag");
        reset=intent.getStringExtra("reset");

         */

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
                times.remove(position);
                finish();
            }
        });

        btn_change_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(TimeDetail.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
                    // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
                    // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
                    ActivityCompat.requestPermissions(TimeDetail.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
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
        timeVitmap = BitmapFactory.decodeFile(myTime.getTimeImgPath());

        tv_title = findViewById(R.id.tv_detail_title);
        tv_date = findViewById(R.id.tv_detail_date);
        tv_remark = findViewById(R.id.tv_detail_remark);
        btn_edit = findViewById(R.id.btn_home_edit);
        btn_delete = findViewById(R.id.btn_home_delete);
        btn_change_background = findViewById(R.id.btn_home_change_background);
        imageView = findViewById(R.id.img_detail_background);

        tv_title.setText(title);
        tv_date.setText(year + "年" + month + "月" + day + "日");
        tv_remark.setText(remark);
        imageView.setImageBitmap(timeVitmap);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TIME_NEW_REQUEST_CODE && resultCode == TIME_OK) {
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
            times.get(position).setTitle(data.getStringExtra("title"));
            times.get(position).setRemark(data.getStringExtra("remark"));
            times.get(position).setYear(data.getStringExtra("year"));
            times.get(position).setMonth(data.getStringExtra("month"));
            times.get(position).setDay(data.getStringExtra("day"));
            times.get(position).setTag(data.getStringExtra("tag"));
            times.get(position).setReset(data.getStringExtra("reset"));
            Intent intent = new Intent(TimeDetail.this, HomeFragment.class);
            finish();
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (data != null) {
                String realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(this, data.getData());
                startPhotoZoom(data.getData());

               /* Bitmap bitmap = BitmapFactory.decodeFile(realPathFromUri);
                imageView.setImageBitmap(bitmap);
                times.get(position).setTimeImgPath(saveBitmap(this, bitmap));

                */

            } else {
                Toast.makeText(this, "图片损坏，请重新选择", Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode==PIC_CUT_REQUEST_CODE) {
            Bitmap bitmap = BitmapFactory.decodeFile(RealPathFromUriUtils.getRealPathFromUri(this,uritempFile));
            imageView.setImageBitmap(bitmap);
            times.get(position).setTimeImgPath(saveBitmap(this, bitmap));
        }
    }

    private Uri uritempFile;
    public void startPhotoZoom(Uri uri) {
        Log.e("uri=====", "" + uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 60);
        intent.putExtra("outputY", 60);
        //uritempFile为Uri类变量，实例化uritempFile
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //开启临时权限
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //重点:针对7.0以上的操作
            intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, uri));
            uritempFile = uri;
        } else {
            uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PIC_CUT_REQUEST_CODE);
    }

    public void startPhotoZoom(Uri uri, String imageUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);

        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 300);

        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        startActivityForResult(intent, PIC_CUT_REQUEST_CODE);
    }


    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }

    /* 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = "/sdcard/Mytime/pic/";
        } else {
            savePath = context.getApplicationContext().getFilesDir().getAbsolutePath() + "/Mytime/pic/";
        }
        try {
            filePic = new File(savePath + times.get(position).getTitle() + ".jpg");
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
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return filePic.getAbsolutePath();
    }
}
