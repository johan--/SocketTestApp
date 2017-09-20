package com.surinov.alexander.sockettestapp.utils.rx;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.surinov.alexander.sockettestapp.data.source.entity.WebSocketJsonData;
import com.surinov.alexander.sockettestapp.utils.Logger;

import rx.functions.Func1;

public class FilterWebSocketJsonData implements Func1<WebSocketJsonData, Boolean> {

    public static final String UNSPECIFIED_SUB_ID = "0";

    private final long mRequestId;

    private String mSubId = UNSPECIFIED_SUB_ID;

    public String getSubId() {
        return mSubId;
    }

    public FilterWebSocketJsonData(long requestId) {
        mRequestId = requestId;
    }

    @Override
    public Boolean call(WebSocketJsonData webSocketJsonData) {
        JsonObject jsonData = webSocketJsonData.getJsonObject();

        if (webSocketJsonData.getRequestId() == mRequestId) {
            Logger.d("FilterWebSocketJsonData.call: wow, requestid's is equals!");

            JsonPrimitive subId = jsonData.getAsJsonPrimitive("subid");

            if (subId != null && subId.isString()) {
                mSubId = subId.getAsString();
                Logger.d("FilterWebSocketJsonData.call: retrieve and save subid: " + mSubId);
            } else {
                throw new IllegalStateException("Source Json object has no 'subid' primitive field. Actual subid field: " + subId);
            }

            return true;
        } else if (jsonData.has(mSubId)) {
            Logger.d("FilterWebSocketJsonData.call: Hah, receive update with my subid: " + mSubId);
            return true;
        } else {
            Logger.d("FilterWebSocketJsonData.call: Hmmm, receive updates for someone else...");
            return false;
        }
    }
}