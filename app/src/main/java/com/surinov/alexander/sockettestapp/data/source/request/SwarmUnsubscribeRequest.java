package com.surinov.alexander.sockettestapp.data.source.request;

import java.util.Locale;

public class SwarmUnsubscribeRequest implements SwarmRequest {

    private static final String SWARM_UNSUBSCRIBE_REQUEST =
            "{\"command\": \"unsubscribe\",\"params\": {\"subid\": \"%s\"}}";

    private final String mSubId;

    public SwarmUnsubscribeRequest(String subId) {
        mSubId = subId;
    }

    @Override
    public String toJsonString() {
        return String.format(Locale.getDefault(), SWARM_UNSUBSCRIBE_REQUEST, mSubId);
    }

    @Override
    public long gerRequestId() {
        return 0;
    }
}