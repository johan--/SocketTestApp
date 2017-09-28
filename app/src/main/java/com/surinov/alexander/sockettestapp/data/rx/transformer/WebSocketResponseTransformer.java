package com.surinov.alexander.sockettestapp.data.rx.transformer;


import com.surinov.alexander.sockettestapp.data.provider.GsonProvider;
import com.surinov.alexander.sockettestapp.data.source.exception.SocketException;
import com.surinov.alexander.sockettestapp.data.source.response.SwarmResponse;
import com.surinov.alexander.sockettestapp.data.source.response.WebSocketResponse;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * This {@link rx.Observable.Transformer} class responsible for filter {@link WebSocketResponse's}.
 * <p>
 * Each {@link WebSocketResponse} can represent ERROR, NEXT or COMPLETED event.
 * <p>
 * If {@link WebSocketResponse} is ERROR event than we throw {@link SocketException}
 * and all subscribers will finished with onError.
 * <p>
 * If {@link WebSocketResponse} is COMPLETED event than all subscribers will finished with onCompleted.
 * <p>
 * If {@link WebSocketResponse} is NEXT event than it goes on and map to {@link SwarmResponse} object
 */
public class WebSocketResponseTransformer implements Observable.Transformer<WebSocketResponse, SwarmResponse> {

    public static final WebSocketResponseTransformer INSTANCE = new WebSocketResponseTransformer();

    @Override
    public Observable<SwarmResponse> call(final Observable<WebSocketResponse> source) {
        return source.doOnNext(new Action1<WebSocketResponse>() {
            @Override
            public void call(WebSocketResponse webSocketResponse) {
                if (webSocketResponse.getState() == WebSocketResponse.State.ERROR) {
                    throw new SocketException(webSocketResponse.getThrowable());
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