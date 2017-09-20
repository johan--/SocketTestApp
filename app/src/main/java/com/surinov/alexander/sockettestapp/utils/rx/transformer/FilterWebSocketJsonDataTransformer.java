package com.surinov.alexander.sockettestapp.utils.rx.transformer;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.surinov.alexander.sockettestapp.data.source.DataSource;
import com.surinov.alexander.sockettestapp.data.source.entity.WebSocketJsonData;
import com.surinov.alexander.sockettestapp.utils.Logger;
import com.surinov.alexander.sockettestapp.utils.rx.FilterWebSocketJsonData;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

public class FilterWebSocketJsonDataTransformer implements Observable.Transformer<WebSocketJsonData, WebSocketJsonData> {

    @NonNull
    private final FilterWebSocketJsonData mFilter;

    @NonNull
    private final DataSource mDataSource;

    public FilterWebSocketJsonDataTransformer(@NonNull FilterWebSocketJsonData filter,
                                              @NonNull DataSource dataSource) {
        mFilter = filter;
        mDataSource = dataSource;
    }

    @Override
    public Observable<WebSocketJsonData> call(Observable<WebSocketJsonData> source) {
        return source.filter(mFilter)
                .map(new Func1<WebSocketJsonData, WebSocketJsonData>() {
                    @Override
                    public WebSocketJsonData call(WebSocketJsonData webSocketJsonData) {
                        String subId = mFilter.getSubId();
                        JsonObject sourceJsonObject = webSocketJsonData.getJsonObject();

                        JsonObject innerJsonObject;
                        if (sourceJsonObject.has(subId)) {
                            innerJsonObject = sourceJsonObject.getAsJsonObject(subId);
                        } else {
                            innerJsonObject = sourceJsonObject.getAsJsonObject("data");
                        }

                        return webSocketJsonData.withNewJsonData(innerJsonObject);
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        String subId = mFilter.getSubId();
                        if (!subId.equals(FilterWebSocketJsonData.UNSPECIFIED_SUB_ID)) {
                            // TODO: 20.09.2017 send command for unsubscription
                            Logger.d("FilterWebSocketJsonDataTransformer.doOnUnsubscribe: unsubscribe from updates!");
                        }
                    }
                });
    }
}