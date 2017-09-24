package com.surinov.alexander.sockettestapp.data.source.request;

import com.surinov.alexander.sockettestapp.data.source.request.generator.SwarmRequestsGenerator;
import com.surinov.alexander.sockettestapp.data.provider.SwarmRequestsGeneratorProvider;

public class SportsRequest implements SwarmRequest {

    private final long mRequestId;
    private final int mType;
    private final boolean mSubscribe;

    private SwarmRequestsGenerator mSwarmRequestsGenerator = SwarmRequestsGeneratorProvider.INSTANCE;

    public SportsRequest(int type, boolean subscribe) {
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