package com.surinov.alexander.sockettestapp.data.source;

import com.surinov.alexander.sockettestapp.data.source.entity.WebSocketData;

import rx.Observable;

public interface DataSource {
    void openConnection();

    void closeConnection();

    boolean isConnectionOpened();

    void sendData(String data);

    Observable<WebSocketData> getDataObservable();
}
