package com.surinov.alexander.sockettestapp.data.source;

import com.surinov.alexander.sockettestapp.data.source.response.WebSocketResponse;

import rx.Observable;

public interface DataSource {

    void sendRequest(String request);

    Observable<WebSocketResponse> getWebSocketResponseObservable();
}
