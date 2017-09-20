package com.surinov.alexander.sockettestapp.data.repository;

import com.surinov.alexander.sockettestapp.data.source.response.sport.SwarmSportListResponse;

import rx.Observable;

public interface SportEventsRepository {

    Observable<SwarmSportListResponse> requestSportLiveEventsObservable(long requestId);
}
