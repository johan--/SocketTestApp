package com.surinov.alexander.sockettestapp.data.rx.transformer;

import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.util.SortedList;

import com.surinov.alexander.sockettestapp.data.source.response.Updatable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * This {@link rx.Observable.Transformer} class responsible for caching received data
 * and preparing {@link ChangesBundle} object that will go to target subscriber.
 */
public class ReceivedDataTransformer<T extends Updatable<T>> implements Observable.Transformer<Map<String, T>, ReceivedDataTransformer.ChangesBundle<T>> {

    private final ArrayMap<String, T> mCachedData = new ArrayMap<>();

    public ArrayMap<String, T> getCachedData() {
        return mCachedData;
    }

    @Override
    public Observable<ChangesBundle<T>> call(Observable<Map<String, T>> source) {
        return source
                .observeOn(Schedulers.computation())
                .map(new Func1<Map<String, T>, ChangesBundle<T>>() {
                    @Override
                    public ChangesBundle<T> call(Map<String, T> newData) {
                        return prepareChangesBundle(mCachedData, newData);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mCachedData.clear();
                    }
                });
    }

    private ChangesBundle<T> prepareChangesBundle(SimpleArrayMap<String, T> cachedData, Map<String, T> newData) {
        ChangesBundle<T> changes = new ChangesBundle<>();

        for (Map.Entry<String, T> entry : newData.entrySet()) {
            T item = entry.getValue();
            int itemIndex = cachedData.indexOfKey(entry.getKey());

            if (itemIndex < 0) {
                // new item was added
                changes.addNewItem(item);
                cachedData.put(entry.getKey(), item);
            } else if (item == null) {
                // item was deleted
                T deletedItem = cachedData.valueAt(itemIndex);
                changes.addDeletedItem(deletedItem);
                cachedData.removeAt(itemIndex);
            } else {
                // item already exists and was updated
                T originalItem = cachedData.valueAt(itemIndex);
                T updatedItem = originalItem.update(item);

                changes.addUpdatedItem(updatedItem);
                cachedData.setValueAt(itemIndex, updatedItem);
            }
        }

        return changes;
    }

    /**
     * Value class that holds information about {@link #mNewItems}, {@link #mUpdatedItems}, {@link #mDeletedItems}
     *
     * @param <T> - type of data
     */
    public static class ChangesBundle<T> {

        @Nullable
        private List<T> mNewItems;

        @Nullable
        private List<T> mUpdatedItems;

        @Nullable
        private List<T> mDeletedItems;

        private void addNewItem(T newItem) {
            if (mNewItems == null) {
                mNewItems = new ArrayList<>();
            }

            mNewItems.add(newItem);
        }

        private void addUpdatedItem(T updatedItem) {
            if (mUpdatedItems == null) {
                mUpdatedItems = new ArrayList<>();
            }

            mUpdatedItems.add(updatedItem);
        }

        private void addDeletedItem(T deletedItem) {
            if (mDeletedItems == null) {
                mDeletedItems = new ArrayList<>();
            }

            mDeletedItems.add(deletedItem);
        }

        @Nullable
        public List<T> getNewItems() {
            return mNewItems;
        }

        @Nullable
        public List<T> getUpdatedItems() {
            return mUpdatedItems;
        }

        @Nullable
        public List<T> getDeletedItems() {
            return mDeletedItems;
        }

        @Override
        public String toString() {
            return "ChangesBundle{" +
                    "\nmNewItems=" + mNewItems +
                    "\nmUpdatedItems=" + mUpdatedItems +
                    "\nmDeletedItems=" + mDeletedItems + "\n" +
                    '}';
        }
    }
}