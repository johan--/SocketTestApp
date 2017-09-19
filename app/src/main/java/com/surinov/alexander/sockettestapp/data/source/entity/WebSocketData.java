package com.surinov.alexander.sockettestapp.data.source.entity;

import com.google.gson.annotations.SerializedName;

public class WebSocketData<T> {

    @SerializedName("code")
    private int mCode;

    @SerializedName("rid")
    private long mRequestId;

    @SerializedName("data")
    private T mData;

    public int getCode() {
        return mCode;
    }

    public long getRequestId() {
        return mRequestId;
    }

    public T getData() {
        return mData;
    }
}