package com.surinov.alexander.sockettestapp.data.repository;

import android.support.annotation.NonNull;

import com.surinov.alexander.sockettestapp.data.source.DataSource;
import com.surinov.alexander.sockettestapp.data.source.entity.WebSocketJsonData;
import com.surinov.alexander.sockettestapp.utils.Logger;
import com.surinov.alexander.sockettestapp.utils.rx.FilterFunc;
import com.surinov.alexander.sockettestapp.utils.rx.transformer.WebSocketJsonStringTransformer;
import com.surinov.alexander.sockettestapp.utils.rx.transformer.WebSocketResponseTransformer;

import rx.Observable;
import rx.functions.Action0;

public class SportLiveEventsRepositoryImpl implements SportEventsRepository {



    private static final String REQUEST_SPORT_LIVE_EVENTS =
            "{\"command\":\"get\",\"rid\": 1,\"params\":{\"source\":\"betting\",\"where\":{\"game\":{\"type\":1}},\"what\":{\"game\":\"@count\",\"sport\":[]},\"subscribe\":true}}";

    @NonNull
    private final DataSource mDataSource;

    public SportLiveEventsRepositoryImpl(@NonNull DataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public Observable<WebSocketJsonData> requestSportLiveEventsObservable(long requestId) {
        return mDataSource.getDataObservable()
                .compose(WebSocketResponseTransformer.INSTANCE)
                .compose(WebSocketJsonStringTransformer.INSTANCE)
                .filter(new FilterFunc(requestId))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.d("SportLiveEventsRepositoryImpl.requestSportLiveEventsObservable.doOnSubscribe");
                        // perform initial request for data with updates for web socket
                        mDataSource.sendCommand(REQUEST_SPORT_LIVE_EVENTS);
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        // perform unsubscribe from updates request for web socket
                        Logger.d("SportLiveEventsRepositoryImpl.requestSportLiveEventsObservable.doOnUnsubscribe");
                    }
                });
    }
}
