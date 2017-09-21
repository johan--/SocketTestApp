package com.surinov.alexander.sockettestapp.data.repository;

import com.google.gson.JsonObject;
import com.surinov.alexander.sockettestapp.data.source.request.SwarmRequest;

import rx.Observable;
import rx.Single;

public interface SwarmRepository {

    Observable<JsonObject> requestSwarmDataWithUpdates(SwarmRequest request);

    Single<JsonObject> requestSwarmData(SwarmRequest request);
}
