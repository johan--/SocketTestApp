package com.surinov.alexander.sockettestapp.data.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {
    public static final Gson INSTANCE;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        INSTANCE = gsonBuilder.create();
    }
}
