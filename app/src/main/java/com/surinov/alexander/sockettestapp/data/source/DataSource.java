package com.surinov.alexander.sockettestapp.data.source;

import com.surinov.alexander.sockettestapp.data.source.entity.WebSocketResponse;

import rx.Observable;

public interface DataSource {

    void sendCommand(String command);

    Observable<WebSocketResponse> getDataObservable();
}
