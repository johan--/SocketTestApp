package com.surinov.alexander.sockettestapp.data.source.entity;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class WebSocketJsonData {

    public static final int UNSPECIFIED_REQUEST_ID = 0;

    @SerializedName("code")
    private int mCode;

    @SerializedName("rid")
    private long mRequestId;

    @SerializedName("data")
    private JsonObject mJsonData;

    public int getCode() {
        return mCode;
    }

    public long getRequestId() {
        return mRequestId;
    }

    public JsonObject getJsonData() {
        return mJsonData;
    }

    @Override
    public String toString() {
        return "WebSocketJsonData{" +
                "mCode=" + mCode +
                ", mRequestId=" + mRequestId +
                ", mJsonData=" + mJsonData +
                '}';
    }
}