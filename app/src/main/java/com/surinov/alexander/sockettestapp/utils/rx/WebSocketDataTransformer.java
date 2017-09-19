package com.surinov.alexander.sockettestapp.utils.rx;


import com.surinov.alexander.sockettestapp.data.source.entity.WebSocketResponse;

import rx.Observable;
import rx.functions.Func1;

public class WebSocketDataTransformer implements Observable.Transformer<WebSocketResponse, String> {

    public static final WebSocketDataTransformer INSTANCE = new WebSocketDataTransformer();

    @Override
    public Observable<String> call(Observable<WebSocketResponse> source) {
        return source.flatMap(new Func1<WebSocketResponse, Observable<String>>() {
            @Override
            public Observable<String> call(WebSocketResponse webSocketData) {
                switch (webSocketData.getState()) {
                    case COMPLETED:
                        return Observable.empty();
                    case ERROR:
                        return Observable.error(webSocketData.getThrowable());
                    case NEXT:
                        return Observable.just(webSocketData.getData());
                    default:
                        throw new RuntimeException("Unknown data state: " + webSocketData.getState());
                }
            }
        });
    }
}