package com.surinov.alexander.sockettestapp.utils.rx.transformer;

import com.google.gson.Gson;
import com.surinov.alexander.sockettestapp.data.provider.GsonProvider;
import com.surinov.alexander.sockettestapp.data.source.entity.WebSocketJsonData;

import rx.Observable;
import rx.functions.Func1;

public class WebSocketJsonStringTransformer implements Observable.Transformer<String, WebSocketJsonData> {

    public static final WebSocketJsonStringTransformer INSTANCE = new WebSocketJsonStringTransformer();

    private WebSocketJsonStringTransformer() {

    }

    @Override
    public Observable<WebSocketJsonData> call(Observable<String> source) {

        return source.map(new Func1<String, WebSocketJsonData>() {
            @Override
            public WebSocketJsonData call(String jsonString) {
                Gson gson = GsonProvider.provideGson();
                return gson.fromJson(jsonString, WebSocketJsonData.class);
            }
        });
    }
}
