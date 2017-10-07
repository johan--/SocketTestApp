package com.surinov.alexander.sockettestapp.utils;

import android.support.annotation.Nullable;

import java.util.Collection;

public class CollectionUtils {
    private CollectionUtils() {
        throw new AssertionError("Utility class not instantiated");
    }

    public static boolean isNullOrEmpty(@Nullable Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }
}