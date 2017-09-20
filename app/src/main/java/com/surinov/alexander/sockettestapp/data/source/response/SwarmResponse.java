package com.surinov.alexander.sockettestapp.data.source.response;

import android.support.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class SwarmResponse {

    public static final int UNSPECIFIED_REQUEST_ID = 0;

    public static final int SWARM_SUCCESS_CODE = 0;

    @SerializedName("code")
    private int mCode;

    @SerializedName("rid")
    private long mRequestId = UNSPECIFIED_REQUEST_ID;

    @Nullable
    @SerializedName("data")
    private JsonObject mData; // TODO: 20.09.2017 handle null 'data' field

    @Nullable
    @SerializedName("msg")
    private String mMessage;

    public int getCode() {
        return mCode;
    }

    public long getRequestId() {
        return mRequestId;
    }

    @Nullable
    public JsonObject getData() {
        return mData;
    }

    public String getMessage() {
        return mMessage == null ? "" : mMessage;
    }

    @Override
    public String toString() {
        return "SwarmResponse{" +
                "mCode=" + mCode +
                ", mRequestId=" + mRequestId +
                ", mData=" + mData +
                ", mMessage='" + mMessage + '\'' +
                '}';
    }
}