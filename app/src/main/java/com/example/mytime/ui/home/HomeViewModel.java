package com.example.mytime.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


//无用！！！！可删除
public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<ArrayList<String>> mtext2;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragmen3");
        mtext2= new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}