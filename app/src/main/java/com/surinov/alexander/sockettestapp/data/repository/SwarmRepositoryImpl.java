package com.surinov.alexander.sockettestapp.data.repository;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.surinov.alexander.sockettestapp.data.source.DataSource;
import com.surinov.alexander.sockettestapp.data.source.request.SwarmRequest;
import com.surinov.alexander.sockettestapp.data.source.request.SwarmUnsubscribeRequest;
import com.surinov.alexander.sockettestapp.utils.Logger;
import com.surinov.alexander.sockettestapp.utils.rx.transformer.SwarmResponseFilterTransformer;
import com.surinov.alexander.sockettestapp.utils.rx.transformer.WebSocketResponseTransformer;

import rx.Observable;
import rx.Single;
import rx.functions.Action0;

public class SwarmRepositoryImpl implements SwarmRepository {

    @NonNull
    private final DataSource mDataSource;

    public SwarmRepositoryImpl(@NonNull DataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public Observable<JsonObject> requestSwarmDataWithUpdates(final SwarmRequest swarmRequest) {
        final SwarmResponseFilterTransformer swarmResponseFilterTransformer =
                new SwarmResponseFilterTransformer(swarmRequest.gerRequestId());

        return mDataSource.getWebSocketResponseObservable()
                .compose(WebSocketResponseTransformer.INSTANCE)
                .compose(swarmResponseFilterTransformer)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.d("SportLiveEventsRepositoryImpl.requestSwarmDataWithUpdates.doOnSubscribe");
                        sendRequest(swarmRequest);
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.d("SportLiveEventsRepositoryImpl.requestSwarmDataWithUpdates.doOnUnsubscribe");
                        if (mDataSource.isConnectionOpened()) {
                            String subId = swarmResponseFilterTransformer.getSubId();
                            if (!subId.equals(SwarmResponseFilterTransformer.UNSPECIFIED_SUB_ID)) {
                                sendRequest(new SwarmUnsubscribeRequest(subId));
                                Logger.d("SportLiveEventsRepositoryImpl.requestSwarmDataWithUpdates.doOnUnsubscribe: send unsubscribe = " + subId);
                            }
                        }
                    }
                });
    }

    @Override
    public Single<JsonObject> requestSwarmData(final SwarmRequest swarmRequest) {
        return mDataSource.getWebSocketResponseObservable()
                .compose(WebSocketResponseTransformer.INSTANCE)
                .compose(new SwarmResponseFilterTransformer(swarmRequest.gerRequestId()))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.d("SportLiveEventsRepositoryImpl.requestSwarmData.doOnSubscribe");
                        sendRequest(swarmRequest);
                    }
                })
                .take(1)
                .toSingle();
    }

    private void sendRequest(SwarmRequest swarmRequest) {
        String jsonSwarmRequest = swarmRequest.toJsonString();
        mDataSource.sendRequest(jsonSwarmRequest);
    }
}