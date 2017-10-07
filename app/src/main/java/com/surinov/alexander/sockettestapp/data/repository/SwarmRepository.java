package com.surinov.alexander.sockettestapp.data.repository;

import android.support.annotation.NonNull;

import com.surinov.alexander.sockettestapp.data.source.request.SwarmRequest;

import rx.Observable;
import rx.Single;

public interface SwarmRepository {

    /**
     * Use this method to fetch live swarm data with updates.
     * <p>
     * Observable, that returns with this method, will periodically emit new data and not competed
     * until subscriber unsubscribes or any error occur.
     *
     * @param classOfT - {@link Class} of data that subscriber want to receive.
     * @param request  - simple SwarmRequest object.
     * @param <T>      - type of data
     * @return - observable that periodically emit new data.
     */
    <T> Observable<T> fetchObservableSwarmData(@NonNull Class<T> classOfT,
                                               @NonNull SwarmRequest request);

    /**
     * Use this method to fetch swarm data WITHOUT any updates. Just request - response workflow.
     *
     * @param classOfT - {@link Class} of data that subscriber want to receive.
     * @param request  - simple SwarmRequest object.
     * @param <T>      - type of data
     * @return - observable that periodically emit new data.
     */
    <T> Single<T> fetchSingleSwarmData(@NonNull Class<T> classOfT,
                                       @NonNull SwarmRequest request);
}