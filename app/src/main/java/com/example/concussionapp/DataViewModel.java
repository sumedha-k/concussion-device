package com.example.concussionapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataViewModel extends ViewModel {
    private final MutableLiveData<String> btData = new MutableLiveData<String>();
    public void sendBtData(String str) {
        btData.setValue(str);
    }
    public LiveData<String> getBtData() {
        return btData;
    }
}
