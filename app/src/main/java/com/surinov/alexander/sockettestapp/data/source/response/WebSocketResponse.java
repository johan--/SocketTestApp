package com.surinov.alexander.sockettestapp.data.source.response;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class WebSocketResponse {

    @SuppressWarnings("WeakerAccess")
    public enum State {
        COMPLETED, NEXT, ERROR
    }

    @NonNull
    private final State mState;

    @NonNull
    private final String mResponse;

    @Nullable
    private final Throwable mThrowable;

    @NonNull
    public State getState() {
        return mState;
    }

    @NonNull
    public String getResponse() {
        return mResponse;
    }

    @Nullable
    public Throwable getThrowable() {
        return mThrowable;
    }

    private WebSocketResponse(@NonNull State state, @NonNull String response, @Nullable Throwable throwable) {
        mState = state;
        mResponse = response;
        mThrowable = throwable;
    }

    private WebSocketResponse(State state, String response) {
        this(state, response, null);
    }

    private WebSocketResponse(State state, Throwable throwable) {
        this(state, "", throwable);
    }

    public static WebSocketResponse error(Throwable throwable) {
        return new WebSocketResponse(State.ERROR, throwable);
    }

    public static WebSocketResponse next(String data) {
        return new WebSocketResponse(State.NEXT, data);
    }

    public static WebSocketResponse competed() {
        return new WebSocketResponse(State.COMPLETED, "");
    }
}