package com.surinov.alexander.sockettestapp.data.source.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Data {

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

    private Data(@NonNull State state, @NonNull String data, @Nullable Throwable throwable) {
        mState = state;
        mData = data;
        mThrowable = throwable;
    }

    private Data(State state, String data) {
        this(state, data, null);
    }

    private Data(State state, Throwable throwable) {
        this(state, "", throwable);
    }

    public static Data error(Throwable throwable) {
        return new Data(State.ERROR, throwable);
    }

    public static Data next(String data) {
        return new Data(State.NEXT, data);
    }

    public static Data competed() {
        return new Data(State.COMPLETED, "");
    }
}