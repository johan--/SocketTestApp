package com.surinov.alexander.sockettestapp.data.source.exception;


public class SwarmException extends RuntimeException {
    private final int mCode;

    public SwarmException(String message, int code) {
        super(message);
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }
}