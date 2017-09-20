package com.surinov.alexander.sockettestapp.utils.rx.transformer;


import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.surinov.alexander.sockettestapp.data.source.SwarmException;
import com.surinov.alexander.sockettestapp.data.source.response.SwarmResponse;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class SwarmResponseFilterTransformer implements Observable.Transformer<SwarmResponse, JsonObject> {

    public static final String UNSPECIFIED_SUB_ID = "unspecified";

    private final long mRequestId;

    private String mSubId = UNSPECIFIED_SUB_ID;

    public SwarmResponseFilterTransformer(long requestId) {
        mRequestId = requestId;
    }

    @Override
    public Observable<JsonObject> call(Observable<SwarmResponse> source) {
        return source.filter(new Func1<SwarmResponse, Boolean>() {
            @Override
            public Boolean call(SwarmResponse swarmResponse) {
                JsonObject jsonData = swarmResponse.getData();
                if (jsonData == null) {
                    return false;
                }

                if (swarmResponse.getRequestId() == mRequestId) {
                    JsonPrimitive subId = jsonData.getAsJsonPrimitive("subid");
                    if (subId != null && subId.isString()) {
                        mSubId = subId.getAsString();
                    }

                    return true;
                }

                return jsonData.has(mSubId);
            }
        }).doOnNext(new Action1<SwarmResponse>() {
            @Override
            public void call(SwarmResponse swarmResponse) {
                if (swarmResponse.getCode() != SwarmResponse.SWARM_SUCCESS_CODE) {
                    throw new SwarmException(swarmResponse.getMessage(), swarmResponse.getCode());
                }
            }
        }).map(new Func1<SwarmResponse, JsonObject>() {
            @Override
            public JsonObject call(SwarmResponse swarmResponse) {
                JsonObject jsonData = swarmResponse.getData();

                //noinspection ConstantConditions
                if (jsonData.has(mSubId)) {
                    return jsonData.getAsJsonObject(mSubId);
                } else {
                    return jsonData.getAsJsonObject("data");
                }
            }
        });
    }
}