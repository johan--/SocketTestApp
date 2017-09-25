package com.surinov.alexander.sockettestapp.data.repository;

import android.support.annotation.NonNull;

import com.surinov.alexander.sockettestapp.data.source.request.SwarmRequest;

import rx.Observable;
import rx.Single;

public interface SwarmRepository {

    <T> Observable<T> fetchSwarmDataObservable(@NonNull Class<T> classOfT,
                                               @NonNull SwarmRequest request);

    <T> Single<T> fetchSwarmDataSingle(@NonNull Class<T> classOfT,
                                       @NonNull SwarmRequest request);
}