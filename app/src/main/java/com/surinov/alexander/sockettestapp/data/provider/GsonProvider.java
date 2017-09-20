package com.surinov.alexander.sockettestapp.data.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {
    private static volatile Gson sGson;

    public static Gson provideGson() {
        Gson gson = sGson;
        if (gson == null) {
            synchronized (GsonProvider.class) {
                gson = sGson;
                if (gson == null) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.serializeNulls();

                    // configure GsonBuilder
                    // ...

                    gson = sGson = gsonBuilder.create();
                }
            }
        }

        return gson;
    }
}
