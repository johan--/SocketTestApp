package com.surinov.alexander.sockettestapp.data.repository;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.surinov.alexander.sockettestapp.data.provider.GsonProvider;
import com.surinov.alexander.sockettestapp.data.source.DataSource;
import com.surinov.alexander.sockettestapp.data.source.response.sport.SwarmSportListResponse;
import com.surinov.alexander.sockettestapp.utils.Logger;
import com.surinov.alexander.sockettestapp.utils.rx.transformer.SwarmResponseFilterTransformer;
import com.surinov.alexander.sockettestapp.utils.rx.transformer.WebSocketResponseTransformer;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

public class SportLiveEventsRepositoryImpl implements SportEventsRepository {

    private static final String REQUEST_SPORT_LIVE_EVENTS =
            "{\"command\":\"get\",\"rid\": 1,\"params\":{\"source\":\"betting\",\"where\":{\"game\":{\"type\":1}},\"what\":{\"game\":\"@count\",\"sport\":[]},\"subscribe\":true}}";

    @NonNull
    private final DataSource mDataSource;

    public SportLiveEventsRepositoryImpl(@NonNull DataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public Observable<SwarmSportListResponse> requestSportLiveEventsObservable(long requestId) {
        return mDataSource.getWebSocketResponseObservable()
                .compose(WebSocketResponseTransformer.INSTANCE)
                .compose(new SwarmResponseFilterTransformer(requestId))
                .map(new Func1<JsonObject, SwarmSportListResponse>() {
                    @Override
                    public SwarmSportListResponse call(JsonObject jsonObject) {
                        Gson gson = GsonProvider.provideGson();
                        return gson.fromJson(jsonObject, SwarmSportListResponse.class);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.d("SportLiveEventsRepositoryImpl.requestSportLiveEventsObservable.doOnSubscribe");
                        // perform initial request for data with updates for web socket
                        mDataSource.sendCommand(REQUEST_SPORT_LIVE_EVENTS);
                    }
                });
    }
}
