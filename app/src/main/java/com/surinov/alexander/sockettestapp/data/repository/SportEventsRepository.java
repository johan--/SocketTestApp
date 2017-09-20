package com.surinov.alexander.sockettestapp.data.repository;

import com.google.gson.JsonObject;

import rx.Observable;

public interface SportEventsRepository {

    Observable<JsonObject> requestSportLiveEventsObservable(long requestId);
}
