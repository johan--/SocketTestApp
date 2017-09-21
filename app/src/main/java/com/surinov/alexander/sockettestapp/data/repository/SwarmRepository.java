package com.surinov.alexander.sockettestapp.data.repository;

import com.google.gson.JsonObject;

import rx.Observable;
import rx.Single;

public interface SwarmRepository {

    Observable<JsonObject> requestSwarmDataWithUpdates(long requestId);

    Single<JsonObject> requestSwarmData(long requestId);
}
