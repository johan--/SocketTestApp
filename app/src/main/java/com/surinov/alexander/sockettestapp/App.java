package com.surinov.alexander.sockettestapp;

import android.app.Application;

import com.surinov.alexander.sockettestapp.utils.Logger;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("App.onCreate");
    }
}