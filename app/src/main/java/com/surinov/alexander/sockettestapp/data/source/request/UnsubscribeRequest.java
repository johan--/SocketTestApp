package com.surinov.alexander.sockettestapp.data.source.request;

import com.surinov.alexander.sockettestapp.data.source.request.generator.SwarmRequestsGenerator;
import com.surinov.alexander.sockettestapp.data.provider.SwarmRequestsGeneratorProvider;

public class UnsubscribeRequest implements JsonSerializable {
    private final String mSubId;

    private SwarmRequestsGenerator mSwarmRequestsGenerator = SwarmRequestsGeneratorProvider.INSTANCE;

    public UnsubscribeRequest(String subId) {
        mSubId = subId;
    }

    @Override
    public String toJsonString() {
        return mSwarmRequestsGenerator.generateUnsubscribeRequest(mSubId);
    }
}