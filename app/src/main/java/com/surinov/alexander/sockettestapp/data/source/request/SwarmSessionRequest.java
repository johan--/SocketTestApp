package com.surinov.alexander.sockettestapp.data.source.request;

import com.google.gson.annotations.SerializedName;
import com.surinov.alexander.sockettestapp.data.provider.GsonProvider;

public class SwarmSessionRequest implements SwarmRequest {

    private static final String SWARM_SESSION_REQUEST_COMMAND = "request_session";

    private static final String SWARM_SESSION_LANGUAGE = "rus";

    private static final int SWARM_SESSION_PLATFORM_CODE = 16;

    private static final int SWARM_SESSION_SITE_ID = 325;

    public static final SwarmSessionRequest INSTANCE = new SwarmSessionRequest();

    private SwarmSessionRequest() {
        // SwarmSessionRequest is a single instance class
    }

    @SerializedName("command")
    private final String mCommand = SWARM_SESSION_REQUEST_COMMAND;

    @SerializedName("params")
    private final Params mParams = new Params();

    @Override
    public String toJsonString() {
        return GsonProvider.provideGson().toJson(this);
    }

    private static class Params {

        @SerializedName("language")
        private final String mLanguage = SWARM_SESSION_LANGUAGE;

        @SerializedName("source")
        private final int mSource = SWARM_SESSION_PLATFORM_CODE;

        @SerializedName("site_id")
        private final int mSiteId = SWARM_SESSION_SITE_ID;
    }
}