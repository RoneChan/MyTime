package com.example.mytime.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.example.mytime.MyTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileDataStream {
    private Context context;
    ArrayList<MyTime> myTimes=new ArrayList<>();

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public FileDataStream(Context context) {
        this.context = context;
    }

    public ArrayList<MyTime> getMyTimes() {
        return myTimes;
    }

    public void setMyTimes(ArrayList<MyTime> myTimes) {
        this.myTimes = myTimes;
    }

    public void save(){
        ObjectOutputStream outputStream=null;
        try{
            //写入文件
            outputStream=new ObjectOutputStream(context.openFileOutput("MyTime.txt",Context.MODE_PRIVATE));
            outputStream.writeObject(myTimes);
            outputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList<MyTime> load(){
        try{
            ObjectInputStream inputStream=new ObjectInputStream(context.openFileInput("MyTime.txt"));
            myTimes=(ArrayList<MyTime>) inputStream.readObject();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return myTimes;
    }

    /* 保存bitmap到本地
     * @param context
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(Context context, Bitmap mBitmap, String title) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savePath = "/sdcard/Mytime/pic/";
        } else {
            savePath = context.getApplicationContext().getFilesDir().getAbsolutePath() + "/Mytime/pic/";
        }
        try {
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
        return filePic.getAbsolutePath();
    }
}
