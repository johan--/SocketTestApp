package com.surinov.alexander.sockettestapp.data.source.response.sport;

import com.google.gson.annotations.SerializedName;

public class SwarmSportResponse {

    @SerializedName("id")
    private int mId;

    @SerializedName("alias")
    private String mAlias;

    @SerializedName("type")
    private int mType;

    @SerializedName("name")
    private String mName;

    @SerializedName("order")
    private int mOrder;

    @SerializedName("game")
    private int mGame;

    @Override
    public String toString() {
        return "SwarmSportResponse{" +
                "mId=" + mId +
                ", mAlias='" + mAlias + '\'' +
                ", mType=" + mType +
                ", mName='" + mName + '\'' +
                ", mOrder=" + mOrder +
                ", mGame=" + mGame +
                '}';
    }

    public int getId() {
        return mId;
    }

    public String getAlias() {
        return mAlias;
    }

    public int getType() {
        return mType;
    }

    public String getName() {
        return mName;
    }

    public int getOrder() {
        return mOrder;
    }

    public int getGame() {
        return mGame;
    }
}