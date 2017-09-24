package com.surinov.alexander.sockettestapp.data.source.request.generator;

import android.support.annotation.NonNull;

import com.surinov.alexander.sockettestapp.App;
import com.surinov.alexander.sockettestapp.R;

public class AppResourcesSwarmRequestsGenerator implements SwarmRequestsGenerator {

    @Override
    public String generateSessionRequest(int siteId, @NonNull String language, int source) {
        return App.getAppContext().getString(R.string.swarm_session_request, siteId, language, source);
    }

    @Override
    public String generateUnsubscribeRequest(@NonNull String subId) {
        return App.getAppContext().getString(R.string.swarm_unsubscribe_request, subId);
    }

    @Override
    public String generateSportsRequest(long requestId, int type, boolean subscribe) {
        return App.getAppContext().getString(R.string.swarm_sports_request, requestId, type, subscribe);
    }
}