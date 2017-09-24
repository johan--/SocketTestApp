package com.surinov.alexander.sockettestapp.data.source.request;

import com.surinov.alexander.sockettestapp.data.source.request.generator.SwarmRequestsGenerator;
import com.surinov.alexander.sockettestapp.data.provider.SwarmRequestsGeneratorProvider;

public class SwarmSessionRequest implements JsonSerializable {

    private static final String SWARM_SESSION_LANGUAGE = "rus";

    private static final int SWARM_SESSION_PLATFORM_CODE = 16;

    private static final int SWARM_SESSION_SITE_ID = 325;

    private SwarmRequestsGenerator mSwarmRequestsGenerator = SwarmRequestsGeneratorProvider.INSTANCE;

    @Override
    public String toJsonString() {
        return mSwarmRequestsGenerator.generateSessionRequest(SWARM_SESSION_SITE_ID, SWARM_SESSION_LANGUAGE, SWARM_SESSION_PLATFORM_CODE);
    }
}