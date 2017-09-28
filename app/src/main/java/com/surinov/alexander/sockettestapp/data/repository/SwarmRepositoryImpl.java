package com.surinov.alexander.sockettestapp.data.repository;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.surinov.alexander.sockettestapp.data.provider.GsonProvider;
import com.surinov.alexander.sockettestapp.data.rx.transformer.SwarmResponseTransformer;
import com.surinov.alexander.sockettestapp.data.rx.transformer.WebSocketResponseTransformer;
import com.surinov.alexander.sockettestapp.data.source.DataSource;
import com.surinov.alexander.sockettestapp.data.source.request.JsonSerializable;
import com.surinov.alexander.sockettestapp.data.source.request.SwarmRequest;
import com.surinov.alexander.sockettestapp.data.source.request.UnsubscribeRequest;
import com.surinov.alexander.sockettestapp.utils.Logger;

import rx.Observable;
import rx.Single;
import rx.functions.Action0;
import rx.functions.Func1;

public class SwarmRepositoryImpl implements SwarmRepository {

    @NonNull
    private final DataSource mDataSource;

    public SwarmRepositoryImpl(@NonNull DataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public <T> Observable<T> fetchObservableSwarmData(@NonNull final Class<T> classOfT,
                                                      @NonNull SwarmRequest request) {
        return fetchSwarmData(request)
                .map(new Func1<JsonObject, T>() {
                    @Override
                    public T call(JsonObject jsonObject) {
                        return GsonProvider.INSTANCE.fromJson(jsonObject, classOfT);
                    }
                });
    }

    @Override
    public <T> Single<T> fetchSingleSwarmData(@NonNull final Class<T> classOfT,
                                              @NonNull SwarmRequest request) {
        return fetchSwarmData(request)
                .map(new Func1<JsonObject, T>() {
                    @Override
                    public T call(JsonObject jsonObject) {
                        return GsonProvider.INSTANCE.fromJson(jsonObject, classOfT);
                    }
                })
                .take(1)
                .toSingle();
    }

    private Observable<JsonObject> fetchSwarmData(final SwarmRequest swarmRequest) {
        final SwarmResponseTransformer swarmResponseFilterTransformer =
                new SwarmResponseTransformer(swarmRequest.gerRequestId());

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
                            if (!subId.equals(SwarmResponseTransformer.UNSPECIFIED_SUB_ID)) {
                                sendRequest(new UnsubscribeRequest(subId));
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