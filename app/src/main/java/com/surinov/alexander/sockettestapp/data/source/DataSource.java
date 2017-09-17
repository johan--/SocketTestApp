package com.surinov.alexander.sockettestapp.data.source;

import rx.Observable;

public interface DataSource {
    void openConnection();

    void closeConnection();

    boolean isConnectionOpened();

    void sendData(String data);

    Observable<String> getDataObservable();
}
