package com.surinov.alexander.sockettestapp.data.source.request;

import com.surinov.alexander.sockettestapp.data.source.request.generator.SwarmRequestsGenerator;
import com.surinov.alexander.sockettestapp.data.source.request.generator.SwarmRequestsGeneratorProvider;

public class SwarmUnsubscribeRequest implements JsonSerializable {
    private final String mSubId;

    private SwarmRequestsGenerator mSwarmRequestsGenerator = SwarmRequestsGeneratorProvider.requestsGenerator();

    public SwarmUnsubscribeRequest(String subId) {
        mSubId = subId;
    }

    @Override
    public String toJsonString() {
        return mSwarmRequestsGenerator.generateUnsubscribeRequest(mSubId);
    }
}