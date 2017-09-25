package com.surinov.alexander.sockettestapp.data.source.response;

public interface Updatable<T> {
    T update(T item);
}