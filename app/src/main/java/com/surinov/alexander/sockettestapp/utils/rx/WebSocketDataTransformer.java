package com.surinov.alexander.sockettestapp.utils.rx;


import com.surinov.alexander.sockettestapp.data.source.entity.Data;

import rx.Observable;
import rx.functions.Func1;

public class WebSocketDataTransformer implements Observable.Transformer<Data, String> {

    public static final WebSocketDataTransformer INSTANCE = new WebSocketDataTransformer();

    @Override
    public Observable<String> call(Observable<Data> source) {
        return source.flatMap(new Func1<Data, Observable<String>>() {
            @Override
            public Observable<String> call(Data data) {
                switch (data.getState()) {
                    case COMPLETED:
                        return Observable.empty();
                    case ERROR:
                        return Observable.error(data.getThrowable());
                    case NEXT:
                        return Observable.just(data.getData());
                    default:
                        throw new RuntimeException("Unknown data state: " + data.getState());
                }
            }
        });
    }
}