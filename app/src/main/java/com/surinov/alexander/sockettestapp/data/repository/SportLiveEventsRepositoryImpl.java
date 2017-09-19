package com.surinov.alexander.sockettestapp.data.repository;

import android.support.annotation.NonNull;

import com.surinov.alexander.sockettestapp.data.source.DataSource;
import com.surinov.alexander.sockettestapp.utils.Logger;
import com.surinov.alexander.sockettestapp.utils.rx.WebSocketDataTransformer;

import rx.Observable;
import rx.Single;
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
    public Observable<String> requestSportLiveEventsObservable() {
        return mDataSource.getDataObservable()
                .compose(WebSocketDataTransformer.INSTANCE)
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

    @Override
    public Single<String> requestSportEventsSingle() {
        return mDataSource.getDataObservable()
                .compose(new WebSocketDataTransformer())
                .toSingle()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // perform initial request for data WITHOUT updates for web socket
                        mDataSource.sendCommand(REQUEST_SPORT_LIVE_EVENTS);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.d("SportLiveEventsRepositoryImpl.requestSportEventsSingle.doOnUnsubscribe");
                    }
                });
    }
}
