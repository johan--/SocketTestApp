package com.surinov.alexander.sockettestapp.utils;

import android.util.Log;

import com.surinov.alexander.sockettestapp.BuildConfig;

public class Logger {
    private static final String TAG = "DebugTag";

    private Logger() {
        throw new AssertionError("Util class not instantiated");
    }

    public static void d(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message + " | Thread: " + Thread.currentThread().getId());
        }
    }
}