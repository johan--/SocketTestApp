package com.surinov.alexander.sockettestapp.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.surinov.alexander.sockettestapp.utils.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class WebSocketDataSource implements DataSource {

    private static final String WEB_SOCKET_ENDPOINT = "wss://swarm.888.ru/";

    private static final String REQUEST_SESSION_COMMAND = "{\"command\":\"request_session\",\"params\":{\"site_id\":325,\"language\":\"rus\",\"source\":16}}";

    @NonNull
    private final OkHttpClient mOkHttpClient;

    @NonNull
    private final Subject<String, String> mDataSubject;

    @Nullable
    private WebSocket mWebSocket;

    private WebSocketListener mWebSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Logger.d("WebSocketDataSource.WebSocketListener.onOpen");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Logger.d("WebSocketDataSource.WebSocketListener.onMessage: " + text);
            if (mDataSubject.hasObservers()) {
                mDataSubject.onNext(text);
            }
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            Logger.d("WebSocketDataSource.WebSocketListener.onClosing");
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            Logger.d("WebSocketDataSource.WebSocketListener.onClosed");
            mDataSubject.onCompleted();

        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Logger.d("WebSocketDataSource.WebSocketListener.onFailure: " + t);
            mDataSubject.onError(t);
        }
    };

    public WebSocketDataSource(@NonNull OkHttpClient okHttpClient) {
        Logger.d("WebSocketDataSource.WebSocketDataSource(@NonNull OkHttpClient okHttpClient)");
        mOkHttpClient = okHttpClient;
        mDataSubject = PublishSubject.create();
    }

    @Override
    public void openConnection() {
        Logger.d("WebSocketDataSource.openConnection");
        openConnectionIfNeeded();
    }

    @Override
    public synchronized void closeConnection() {
        Logger.d("WebSocketDataSource.closeConnection");
        if (mWebSocket != null) {
            mWebSocket.close(1000, "Normal closure; the connection successfully completed whatever purpose for which it was created");
            mWebSocket = null;
        }
    }

    @Override
    public boolean isConnectionOpened() {
        return mWebSocket != null;
    }

    @Override
    public synchronized void sendData(String data) {
        openConnectionIfNeeded();
        if (mWebSocket != null) {
            mWebSocket.send(data);
        }
    }

    @Override
    public Observable<String> getDataObservable() {
        return mDataSubject;
    }

    private synchronized void openConnectionIfNeeded() {
        Logger.d("WebSocketDataSource.openConnectionIfNeeded");
        if (mWebSocket != null) {
            return;
        }

        Logger.d("WebSocketDataSource.openConnectionIfNeeded: " + "Create connection and request session");

        Request openConnectionRequest = new Request.Builder()
                .url(WEB_SOCKET_ENDPOINT)
                .build();

        mWebSocket = mOkHttpClient.newWebSocket(openConnectionRequest, mWebSocketListener); // open connection
        mWebSocket.send(REQUEST_SESSION_COMMAND); // request session
    }
}