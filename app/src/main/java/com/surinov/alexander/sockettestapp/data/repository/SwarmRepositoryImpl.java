package com.surinov.alexander.sockettestapp.data.repository;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.surinov.alexander.sockettestapp.data.source.DataSource;
import com.surinov.alexander.sockettestapp.utils.Logger;
import com.surinov.alexander.sockettestapp.utils.rx.transformer.SwarmResponseFilterTransformer;
import com.surinov.alexander.sockettestapp.utils.rx.transformer.WebSocketResponseTransformer;

import rx.Observable;
import rx.Single;
import rx.functions.Action0;

public class SwarmRepositoryImpl implements SwarmRepository {

    private static final String REQUEST_SPORT_LIVE_EVENTS =
            "{\"command\":\"get\",\"rid\": 1,\"params\":{\"source\":\"betting\",\"where\":{\"game\":{\"type\":1}},\"what\":{\"game\":\"@count\",\"sport\":[]},\"subscribe\":true}}";

    @NonNull
    private final DataSource mDataSource;

    public SwarmRepositoryImpl(@NonNull DataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public Observable<JsonObject> requestSwarmDataWithUpdates(long requestId) {
        final SwarmResponseFilterTransformer swarmResponseFilterTransformer =
                new SwarmResponseFilterTransformer(requestId);

        return mDataSource.getWebSocketResponseObservable()
                .compose(WebSocketResponseTransformer.INSTANCE)
                .compose(swarmResponseFilterTransformer)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.d("SportLiveEventsRepositoryImpl.requestSwarmDataWithUpdates.doOnSubscribe");
                        // perform initial request for data with updates for web socket
                        mDataSource.sendRequest(REQUEST_SPORT_LIVE_EVENTS);
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.d("SportLiveEventsRepositoryImpl.requestSwarmDataWithUpdates.doOnUnsubscribe");
                        if (mDataSource.isConnectionOpened()) {
                            String subId = swarmResponseFilterTransformer.getSubId();
                            Logger.d("SportLiveEventsRepositoryImpl.requestSwarmDataWithUpdates.doOnUnsubscribe: send unsubscribe = " + subId);

                        }
                    }
                });
    }

    @Override
    public Single<JsonObject> requestSwarmData(long requestId) {
        return mDataSource.getWebSocketResponseObservable()
                .compose(WebSocketResponseTransformer.INSTANCE)
                .compose(new SwarmResponseFilterTransformer(requestId))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.d("SportLiveEventsRepositoryImpl.requestSwarmData.doOnSubscribe");
                        mDataSource.sendRequest(REQUEST_SPORT_LIVE_EVENTS);
                    }
                })
                .take(1)
                .toSingle();
    }
}