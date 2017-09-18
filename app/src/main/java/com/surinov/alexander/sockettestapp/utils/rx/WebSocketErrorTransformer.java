package com.surinov.alexander.sockettestapp.utils.rx;


import com.surinov.alexander.sockettestapp.data.source.entity.WebSocketData;

import rx.Observable;
import rx.functions.Func1;

public class WebSocketErrorTransformer implements Observable.Transformer<WebSocketData, String> {

    public static final WebSocketErrorTransformer INSTANCE = new WebSocketErrorTransformer();

    @Override
    public Observable<String> call(Observable<WebSocketData> source) {
        return source.map(new Func1<WebSocketData, String>() {
            @Override
            public String call(WebSocketData webSocketData) {
                if (webSocketData.isError()) {
                    throw new RuntimeException(webSocketData.getThrowable());
                }
                return webSocketData.getData();
            }
        });
    }
}