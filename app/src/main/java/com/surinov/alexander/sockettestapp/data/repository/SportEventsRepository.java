package com.surinov.alexander.sockettestapp.data.repository;

import rx.Observable;
import rx.Single;

public interface SportEventsRepository {

    Observable<String> requestSportLiveEventsObservable();

    Single<String> requestSportEventsSingle();
}
