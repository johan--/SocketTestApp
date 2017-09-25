package com.surinov.alexander.sockettestapp.data.source.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SportsResponse {

    @SerializedName("sport")
    private Map<String, SportItem> mSportMap;

    public Map<String, SportItem> getSportMap() {
        return mSportMap;
    }

    public List<SportItem> getSportItems() {
        return mSportMap.isEmpty() ? Collections.<SportItem>emptyList() : new ArrayList<>(mSportMap.values());
    }

    @Override
    public String toString() {
        return "SwarmSportListResponse{" +
                "mSportMap=" + mSportMap +
                '}';
    }

    public static class SportItem implements Updatable<SportItem> {

        private SportItem(SportItem oldItem, SportItem newItem) {
            mId = newItem.getId() != null ? newItem.getId() : oldItem.getId();
            mAlias = newItem.getAlias() != null ? newItem.getAlias() : oldItem.getAlias();
            mType = newItem.getType() != null ? newItem.getType() : oldItem.getType();
            mName = newItem.getName() != null ? newItem.getName() : oldItem.getName();
            mOrder = newItem.getOrder() != null ? newItem.getOrder() : oldItem.getOrder();
            mGame = newItem.getGame() != null ? newItem.getGame() : oldItem.getGame();
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
        private Integer mGame;

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

        public Integer getGame() {
            return mGame;
        }
    }
}