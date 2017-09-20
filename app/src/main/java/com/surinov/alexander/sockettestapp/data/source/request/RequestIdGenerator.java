package com.surinov.alexander.sockettestapp.data.source.request;

import java.util.concurrent.atomic.AtomicLong;

public class RequestIdGenerator {
    private static final AtomicLong GENERATOR = new AtomicLong();

    public static long nextRequestId() {
        return GENERATOR.incrementAndGet();
    }
}