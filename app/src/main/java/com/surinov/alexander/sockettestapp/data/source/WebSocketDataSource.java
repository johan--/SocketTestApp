package com.surinov.alexander.sockettestapp.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.surinov.alexander.sockettestapp.data.source.request.SwarmSessionRequest;
import com.surinov.alexander.sockettestapp.data.source.response.WebSocketResponse;
import com.surinov.alexander.sockettestapp.utils.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import rx.Observable;
import rx.subjects.PublishSubject;

public class WebSocketDataSource implements DataSource {

    private enum ConnectionState {
        OPENED, CLOSED
    }

    private static final String WEB_SOCKET_ENDPOINT = "wss://swarm.888.ru/";

    @NonNull
    private final OkHttpClient mOkHttpClient;

    @NonNull
    private final PublishSubject<WebSocketResponse> mWebSocketResponseSubject = PublishSubject.create();

    @Nullable
    private WebSocket mWebSocket;

    private volatile ConnectionState mConnectionState = ConnectionState.CLOSED;

    private final WebSocketListener mWebSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Logger.d("WebSocketDataSource.WebSocketListener.onOpen");
            mConnectionState = ConnectionState.OPENED;
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Logger.d("WebSocketDataSource.WebSocketListener.onMessage: " + text);
            pushDataIfHasObservers(WebSocketResponse.next(text), mWebSocketResponseSubject);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            Logger.d("WebSocketDataSource.WebSocketListener.onClosing");
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            Logger.d("WebSocketDataSource.WebSocketListener.onClosed");
            mConnectionState = ConnectionState.CLOSED;
            pushDataIfHasObservers(WebSocketResponse.competed(), mWebSocketResponseSubject);

        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Logger.d("WebSocketDataSource.WebSocketListener.onFailure: " + t);
            mConnectionState = ConnectionState.CLOSED;
            pushDataIfHasObservers(WebSocketResponse.error(t), mWebSocketResponseSubject);
        }
    };

    public WebSocketDataSource(@NonNull OkHttpClient okHttpClient) {
        Logger.d("WebSocketDataSource.WebSocketDataSource(@NonNull OkHttpClient okHttpClient)");
        mOkHttpClient = okHttpClient;
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
        }
    }

    @Override
    public boolean isConnectionOpened() {
        return mConnectionState == ConnectionState.OPENED;
    }

    @Override
    public synchronized void sendRequest(String jsonStringRequest) {
        openConnectionIfNeeded();
        if (mWebSocket != null) {
            mWebSocket.send(jsonStringRequest);
        }
    }

    @Override
    public Observable<WebSocketResponse> getWebSocketResponseObservable() {
        return mWebSocketResponseSubject;
    }

    private void pushDataIfHasObservers(@NonNull WebSocketResponse webSocketResponse,
                                        @NonNull PublishSubject<WebSocketResponse> subject) {
        if (subject.hasObservers()) {
            subject.onNext(webSocketResponse);
        }
    }

    private synchronized void openConnectionIfNeeded() {
        Logger.d("WebSocketDataSource.openConnectionIfNeeded");
        if (mConnectionState == ConnectionState.OPENED) {
            return;
        }

        Logger.d("WebSocketDataSource.openConnectionIfNeeded: " + "Create connection and request session");

        Request openConnectionRequest = new Request.Builder()
                .url(WEB_SOCKET_ENDPOINT)
                .build();

        WebSocket webSocket = mWebSocket = mOkHttpClient.newWebSocket(openConnectionRequest, mWebSocketListener); // open connection
        webSocket.send(SwarmSessionRequest.INSTANCE.toJsonString()); // request session
    }
}