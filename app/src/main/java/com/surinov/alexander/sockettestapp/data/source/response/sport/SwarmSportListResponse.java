package com.surinov.alexander.sockettestapp.data.source.response.sport;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SwarmSportListResponse {

    @SerializedName("sport")
    private Map<String, SwarmSportResponse> mSportMap;

    public List<SwarmSportResponse> getSportList() {
        return mSportMap.isEmpty() ? Collections.<SwarmSportResponse>emptyList() : new ArrayList<>(mSportMap.values());
    }

    @Override
    public String toString() {
        return "SwarmSportListResponse{" +
                "mSportMap=" + mSportMap +
                '}';
    }
}