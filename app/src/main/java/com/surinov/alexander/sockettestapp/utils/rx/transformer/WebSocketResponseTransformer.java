package com.surinov.alexander.sockettestapp.utils.rx.transformer;


import com.google.gson.Gson;
import com.surinov.alexander.sockettestapp.data.provider.GsonProvider;
import com.surinov.alexander.sockettestapp.data.source.response.SwarmResponse;
import com.surinov.alexander.sockettestapp.data.source.response.WebSocketResponse;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class WebSocketResponseTransformer implements Observable.Transformer<WebSocketResponse, SwarmResponse> {

    public static final WebSocketResponseTransformer INSTANCE = new WebSocketResponseTransformer();

    private WebSocketResponseTransformer() {
        // WebSocketResponseTransformer is a single instance class
    }

    @Override
    public Observable<SwarmResponse> call(final Observable<WebSocketResponse> source) {
        return source.doOnNext(new Action1<WebSocketResponse>() {
            @Override
            public void call(WebSocketResponse webSocketResponse) {
                if (webSocketResponse.getState() == WebSocketResponse.State.ERROR) {
                    throw new RuntimeException(webSocketResponse.getThrowable());
                }
            }
        }).takeWhile(new Func1<WebSocketResponse, Boolean>() {
            @Override
            public Boolean call(WebSocketResponse webSocketResponse) {
                return webSocketResponse.getState() == WebSocketResponse.State.NEXT;
            }
        }).map(new Func1<WebSocketResponse, SwarmResponse>() {
            @Override
            public SwarmResponse call(WebSocketResponse swarmResponse) {
                return GsonProvider.INSTANCE.fromJson(swarmResponse.getResponse(), SwarmResponse.class);
            }
        });
    }
}