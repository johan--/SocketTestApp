package com.surinov.alexander.sockettestapp.data.source.response;

import android.support.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class SwarmResponse {

    public static final int UNSPECIFIED_REQUEST_ID = 0;

    public static final int SWARM_SUCCESS_CODE = 0;

    @SerializedName("code")
    private int mCode;

    @SerializedName("rid")
    private long mRequestId = UNSPECIFIED_REQUEST_ID;

    @SerializedName("data")
    private JsonElement mJsonData;

    @Nullable
    @SerializedName("msg")
    private String mMessage;

    public int getCode() {
        return mCode;
    }

    public long getRequestId() {
        return mRequestId;
    }

    public JsonElement getJsonData() {
        return mJsonData;
    }

    public String getMessage() {
        return mMessage == null ? "" : mMessage;
    }

    @Override
    public String toString() {
        return "SwarmResponse{" +
                "mCode=" + mCode +
                ", mRequestId=" + mRequestId +
                ", mJsonData=" + mJsonData +
                ", mMessage='" + mMessage + '\'' +
                '}';
    }
}