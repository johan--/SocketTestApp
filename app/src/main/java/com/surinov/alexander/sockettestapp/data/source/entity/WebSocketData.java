package com.surinov.alexander.sockettestapp.data.source.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class WebSocketData {

    @NonNull
    private final String mData;

    @Nullable
    private final Throwable mThrowable;

    private WebSocketData(@NonNull String data, @Nullable Throwable throwable) {
        mData = data;
        mThrowable = throwable;
    }

    private WebSocketData(@NonNull String data) {
        this(data, null);
    }

    @Nullable
    public Throwable getThrowable() {
        return mThrowable;
    }

    @NonNull
    public String getData() {
        return mData;
    }

    public boolean isError() {
        return mThrowable != null;
    }

    public static WebSocketData error(@NonNull Throwable throwable) {
        return new WebSocketData("", throwable);
    }

    public static WebSocketData success(@NonNull String data) {
        return new WebSocketData(data);
    }
}