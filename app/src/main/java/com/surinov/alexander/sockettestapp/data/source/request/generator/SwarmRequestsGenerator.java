package com.surinov.alexander.sockettestapp.data.source.request.generator;

import android.support.annotation.NonNull;

public interface SwarmRequestsGenerator {
    String generateSessionRequest(int siteId, @NonNull String language, int source);

    String generateUnsubscribeRequest(@NonNull String subId);

    String generateSportsRequest(long requestId, int type, boolean subscribe);
}
