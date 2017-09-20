package com.surinov.alexander.sockettestapp.data.source.entity;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class WebSocketJsonData {

    public static final int UNSPECIFIED_REQUEST_ID = 0;

    private WebSocketJsonData(int code, long requestId, JsonObject jsonObject) {
        mCode = code;
        mRequestId = requestId;
        mJsonObject = jsonObject;
    }

    @SerializedName("code")
    private int mCode;

    @SerializedName("rid")
    private long mRequestId;

    @SerializedName("data")
    private JsonObject mJsonObject;

    public int getCode() {
        return mCode;
    }

    public long getRequestId() {
        return mRequestId;
    }

    public JsonObject getJsonObject() {
        return mJsonObject;
    }

    public WebSocketJsonData withNewJsonData(JsonObject jsonData) {
        return new WebSocketJsonData(mCode, mRequestId, jsonData);
    }

    @Override
    public String toString() {
        return "WebSocketJsonData{" +
                "mCode=" + mCode +
                ", mRequestId=" + mRequestId +
                ", mJsonObject=" + mJsonObject +
                '}';
    }
}