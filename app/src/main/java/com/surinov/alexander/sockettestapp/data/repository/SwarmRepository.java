package com.surinov.alexander.sockettestapp.data.repository;

import android.support.annotation.NonNull;

import com.surinov.alexander.sockettestapp.data.source.request.SportsRequest;
import com.surinov.alexander.sockettestapp.data.source.response.SportsResponse;

import rx.Observable;

public interface SwarmRepository {

    Observable<SportsResponse> fetchSports(@NonNull SportsRequest request);
}
