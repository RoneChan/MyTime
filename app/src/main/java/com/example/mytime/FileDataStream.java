package com.example.mytime;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
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
            outputStream=new ObjectOutputStream(context.openFileOutput("MyTime.txt",Context.MODE_APPEND));
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
}
