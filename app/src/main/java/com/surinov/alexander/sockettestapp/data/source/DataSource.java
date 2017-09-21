package com.surinov.alexander.sockettestapp.data.source;

import com.surinov.alexander.sockettestapp.data.source.response.WebSocketResponse;

import rx.Observable;

public interface DataSource {

    void openConnection();

    void closeConnection();

    boolean isConnectionOpened();

    void sendRequest(String request);

    Observable<WebSocketResponse> getWebSocketResponseObservable();
}
