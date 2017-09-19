package com.surinov.alexander.sockettestapp.data.source.entity;

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
    private final String mData;

    @Nullable
    private final Throwable mThrowable;

    @NonNull
    public State getState() {
        return mState;
    }

    @NonNull
    public String getData() {
        return mData;
    }

    @Nullable
    public Throwable getThrowable() {
        return mThrowable;
    }

    private WebSocketResponse(@NonNull State state, @NonNull String data, @Nullable Throwable throwable) {
        mState = state;
        mData = data;
        mThrowable = throwable;
    }

    private WebSocketResponse(State state, String data) {
        this(state, data, null);
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