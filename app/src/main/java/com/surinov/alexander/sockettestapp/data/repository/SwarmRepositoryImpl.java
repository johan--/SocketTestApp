package com.surinov.alexander.sockettestapp.data.repository;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.surinov.alexander.sockettestapp.data.provider.GsonProvider;
import com.surinov.alexander.sockettestapp.data.source.request.SwarmSportsRequest;
import com.surinov.alexander.sockettestapp.data.source.response.sport.SwarmSportListResponse;
import com.surinov.alexander.sockettestapp.data.source.DataSource;
import com.surinov.alexander.sockettestapp.data.source.request.JsonSerializable;
import com.surinov.alexander.sockettestapp.data.source.request.SwarmRequest;
import com.surinov.alexander.sockettestapp.data.source.request.SwarmUnsubscribeRequest;
import com.surinov.alexander.sockettestapp.utils.Logger;
import com.surinov.alexander.sockettestapp.utils.rx.transformer.SwarmResponseFilterTransformer;
import com.surinov.alexander.sockettestapp.utils.rx.transformer.WebSocketResponseTransformer;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

public class SwarmRepositoryImpl implements SwarmRepository {

    @NonNull
    private final DataSource mDataSource;

    public SwarmRepositoryImpl(@NonNull DataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public Observable<SwarmSportListResponse> fetchSportEvents(@NonNull SwarmSportsRequest request) {
        return fetchSwarmData(request)
                .map(new Func1<JsonObject, SwarmSportListResponse>() {
                    @Override
                    public SwarmSportListResponse call(JsonObject jsonObject) {
                        return GsonProvider.INSTANCE.fromJson(jsonObject, SwarmSportListResponse.class);
                    }
                });
    }

    private Observable<JsonObject> fetchSwarmData(final SwarmRequest swarmRequest) {
        final SwarmResponseFilterTransformer swarmResponseFilterTransformer =
                new SwarmResponseFilterTransformer(swarmRequest.gerRequestId());

        return mDataSource.getWebSocketResponseObservable()
                .compose(WebSocketResponseTransformer.INSTANCE)
                .compose(swarmResponseFilterTransformer)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.d("SportLiveEventsRepositoryImpl.fetchSwarmData.doOnSubscribe");
                        sendRequest(swarmRequest);
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.d("SportLiveEventsRepositoryImpl.fetchSwarmData.doOnUnsubscribe");
                        if (mDataSource.isConnectionOpened()) {
                            String subId = swarmResponseFilterTransformer.getSubId();
                            if (!subId.equals(SwarmResponseFilterTransformer.UNSPECIFIED_SUB_ID)) {
                                sendRequest(new SwarmUnsubscribeRequest(subId));
                                Logger.d("SportLiveEventsRepositoryImpl.fetchSwarmData.doOnUnsubscribe: send unsubscribe = " + subId);
                            }
                        }
                    }
                });
    }

    private void sendRequest(JsonSerializable request) {
        String jsonStringRequest = request.toJsonString();
        mDataSource.sendRequest(jsonStringRequest);
    }
}