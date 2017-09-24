package com.surinov.alexander.sockettestapp.data.source.request.sport;

import com.surinov.alexander.sockettestapp.data.source.request.RequestIdGenerator;
import com.surinov.alexander.sockettestapp.data.source.request.SwarmRequest;
import com.surinov.alexander.sockettestapp.data.source.request.generator.SwarmRequestsGenerator;
import com.surinov.alexander.sockettestapp.data.source.request.generator.SwarmRequestsGeneratorProvider;

public class SwarmSportsRequest implements SwarmRequest {

    private final long mRequestId;
    private final int mType;
    private final boolean mSubscribe;

    private SwarmRequestsGenerator mSwarmRequestsGenerator = SwarmRequestsGeneratorProvider.requestsGenerator();

    public SwarmSportsRequest(int type, boolean subscribe) {
        mRequestId = RequestIdGenerator.nextRequestId();
        mType = type;
        mSubscribe = subscribe;
    }

    @Override
    public String toJsonString() {
        return mSwarmRequestsGenerator.generateSportsRequest(mRequestId, mType, mSubscribe);
    }

    @Override
    public long gerRequestId() {
        return mRequestId;
    }
}