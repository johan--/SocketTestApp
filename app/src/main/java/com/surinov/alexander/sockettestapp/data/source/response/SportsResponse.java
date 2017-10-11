package com.surinov.alexander.sockettestapp.data.source.response;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class SportsResponse {

    @SerializedName("sport")
    private Map<String, SportItem> mSportMap;

    public Map<String, SportItem> getSportMap() {
        return mSportMap;
    }

    @Override
    public String toString() {
        return "SportsResponse{" +
                "mSportMap=" + mSportMap +
                '}';
    }

    @SuppressWarnings("WeakerAccess")
    public static class SportItem implements Updatable<SportItem> {

        private SportItem(SportItem oldItem, SportItem newItem) {
            mId = newItem.getId() != null ? newItem.getId() : oldItem.getId();
            mAlias = newItem.getAlias() != null ? newItem.getAlias() : oldItem.getAlias();
            mType = newItem.getType() != null ? newItem.getType() : oldItem.getType();
            mName = newItem.getName() != null ? newItem.getName() : oldItem.getName();
            mOrder = newItem.getOrder() != null ? newItem.getOrder() : oldItem.getOrder();
            mGameCount = newItem.getGameCount() != null ? newItem.getGameCount() : oldItem.getGameCount();
        }

        @SerializedName("id")
        private Integer mId;

        @SerializedName("alias")
        private String mAlias;

        @SerializedName("type")
        private Integer mType;

        @SerializedName("name")
        private String mName;

        @SerializedName("order")
        private Integer mOrder;

        @SerializedName("game")
        private Integer mGameCount;

        @Override
        public String toString() {
            return "SportItem{" +
                    "mId=" + mId +
                    ", mAlias='" + mAlias + '\'' +
                    ", mType=" + mType +
                    ", mName='" + mName + '\'' +
                    ", mOrder=" + mOrder +
                    ", mGameCount=" + mGameCount +
                    '}';
        }

        @Override
        public SportItem update(SportItem newItem) {
            return new SportItem(this, newItem);
        }

        public Integer getId() {
            return mId;
        }

        public String getAlias() {
            return mAlias;
        }

        public Integer getType() {
            return mType;
        }

        public String getName() {
            return mName;
        }

        public Integer getOrder() {
            return mOrder;
        }

        public Integer getGameCount() {
            return mGameCount;
        }
    }
}