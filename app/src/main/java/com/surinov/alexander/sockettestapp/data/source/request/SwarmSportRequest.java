package com.surinov.alexander.sockettestapp.data.source.request;

import java.util.Locale;

public class SwarmSportRequest implements SwarmRequest {

    private static final String SWARM_SPORT_REQUEST =
            "{\"command\": \"get\",\"rid\": %d," +
                    "\"params\": {\"source\": \"betting\"," +
                    "\"where\": {" + "\"game\": {\"type\": %d}}," +
                    "\"what\": {\"game\": \"@count\",\"sport\": []}," +
                    "\"subscribe\": %b}}";

    private final long mRequestId;
    private final int mType;
    private final boolean mSubscribeForUpdates;

    public SwarmSportRequest(int type, boolean subscribeForUpdates) {
        mRequestId = RequestIdGenerator.nextRequestId();
        mType = type;
        mSubscribeForUpdates = subscribeForUpdates;
    }

    @Override
    public String toJsonString() {
        return String.format(Locale.getDefault(), SWARM_SPORT_REQUEST, mRequestId, mType, mSubscribeForUpdates);
    }

    @Override
    public long gerRequestId() {
        return mRequestId;
    }
}