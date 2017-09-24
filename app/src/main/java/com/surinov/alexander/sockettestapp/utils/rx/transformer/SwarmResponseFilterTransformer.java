package com.surinov.alexander.sockettestapp.utils.rx.transformer;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.surinov.alexander.sockettestapp.data.source.exception.SwarmException;
import com.surinov.alexander.sockettestapp.data.source.response.SwarmResponse;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class SwarmResponseFilterTransformer implements Observable.Transformer<SwarmResponse, JsonObject> {

    public static final String UNSPECIFIED_SUB_ID = "unspecified";

    private final long mRequestId;

    private String mSubId = UNSPECIFIED_SUB_ID;

    public String getSubId() {
        return mSubId;
    }

    public SwarmResponseFilterTransformer(long requestId) {
        mRequestId = requestId;
    }

    @Override
    public Observable<JsonObject> call(Observable<SwarmResponse> source) {
        return source.filter(new Func1<SwarmResponse, Boolean>() {
            @Override
            public Boolean call(SwarmResponse swarmResponse) {
                JsonElement jsonData = swarmResponse.getJsonData();

                if (swarmResponse.getRequestId() == mRequestId) {
                    if (jsonData.isJsonObject()) {
                        JsonPrimitive subId = jsonData.getAsJsonObject().getAsJsonPrimitive("subid");
                        if (subId != null && subId.isString()) {
                            mSubId = subId.getAsString();
                        }
                    }

                    return true;
                }

                return jsonData.isJsonObject() && jsonData.getAsJsonObject().has(mSubId);
            }
        }).doOnNext(new Action1<SwarmResponse>() {
            @Override
            public void call(SwarmResponse swarmResponse) {
                if (swarmResponse.getCode() != SwarmResponse.SWARM_SUCCESS_CODE) {
                    throw new SwarmException(swarmResponse.getMessage(), swarmResponse.getCode());
                }
            }
        }).filter(new Func1<SwarmResponse, Boolean>() {
            @Override
            public Boolean call(SwarmResponse swarmResponse) {
                return swarmResponse.getJsonData().isJsonObject();
            }
        }).map(new Func1<SwarmResponse, JsonObject>() {
            @Override
            public JsonObject call(SwarmResponse swarmResponse) {
                JsonObject jsonData = swarmResponse.getJsonData().getAsJsonObject();
                if (jsonData.has(mSubId)) {
                    return jsonData.getAsJsonObject(mSubId);
                } else {
                    return jsonData.getAsJsonObject("data");
                }
            }
        });
    }
}