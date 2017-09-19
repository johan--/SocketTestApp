package com.surinov.alexander.sockettestapp.data.repository;

import com.surinov.alexander.sockettestapp.data.source.entity.WebSocketJsonData;

import rx.Observable;
import rx.Single;

public interface SportEventsRepository {

    Observable<WebSocketJsonData> requestSportLiveEventsObservable(long requestId);
}
