package com.surinov.alexander.sockettestapp.data.repository;

import android.support.annotation.NonNull;

import com.surinov.alexander.sockettestapp.data.source.request.SwarmSportsRequest;
import com.surinov.alexander.sockettestapp.data.source.response.sport.SwarmSportListResponse;

import rx.Observable;

public interface SwarmRepository {

    Observable<SwarmSportListResponse> fetchSportEvents(@NonNull SwarmSportsRequest request);
}
