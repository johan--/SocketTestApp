package com.surinov.alexander.sockettestapp.utils.rx.transformer;


import com.google.gson.Gson;
import com.surinov.alexander.sockettestapp.data.provider.GsonProvider;
import com.surinov.alexander.sockettestapp.data.source.entity.WebSocketJsonData;
import com.surinov.alexander.sockettestapp.data.source.entity.WebSocketResponse;

import rx.Observable;
import rx.functions.Func1;

public class WebSocketResponseTransformer implements Observable.Transformer<WebSocketResponse, WebSocketJsonData> {

    public static final WebSocketResponseTransformer INSTANCE = new WebSocketResponseTransformer();

    private WebSocketResponseTransformer() {

    }

    @Override
    public Observable<WebSocketJsonData> call(Observable<WebSocketResponse> source) {
        return source.flatMap(new Func1<WebSocketResponse, Observable<String>>() {
            @Override
            public Observable<String> call(WebSocketResponse webSocketData) {
                switch (webSocketData.getState()) {
                    case COMPLETED:
                        return Observable.empty();
                    case ERROR:
                        return Observable.error(webSocketData.getThrowable());
                    case NEXT:
                        return Observable.just(webSocketData.getResponse());
                    default:
                        throw new RuntimeException("Unknown data state: " + webSocketData.getState());
                }
            }
        }).map(new Func1<String, WebSocketJsonData>() {
            @Override
            public WebSocketJsonData call(String jsonString) {
                Gson gson = GsonProvider.provideGson();
                return gson.fromJson(jsonString, WebSocketJsonData.class);
            }
        });
    }
}