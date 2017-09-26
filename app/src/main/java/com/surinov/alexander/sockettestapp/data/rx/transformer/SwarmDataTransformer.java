package com.surinov.alexander.sockettestapp.data.rx.transformer;

import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.surinov.alexander.sockettestapp.data.source.response.Updatable;
import com.surinov.alexander.sockettestapp.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SwarmDataTransformer<T extends Updatable<T>> implements Observable.Transformer<Map<String, T>, SwarmDataTransformer.ChangesBundle<T>> {

    private final ArrayMap<String, T> mOriginalData = new ArrayMap<>();

    public ArrayMap<String, T> getOriginalData() {
        return mOriginalData;
    }

    @Override
    public Observable<ChangesBundle<T>> call(Observable<Map<String, T>> source) {
        return source
                .observeOn(Schedulers.computation())
                .map(new Func1<Map<String, T>, ChangesBundle<T>>() {
                    @Override
                    public ChangesBundle<T> call(Map<String, T> newData) {
                        Logger.d("SwarmDataTransformer.call before prepareChangesBundle: " + newData);
                        ChangesBundle<T> changes = prepareChangesBundle(mOriginalData, newData);
                        Logger.d("SwarmDataTransformer.call after prepareChangesBundle: " + newData);
                        return changes;
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mOriginalData.clear();
                    }
                });
    }

    private ChangesBundle<T> prepareChangesBundle(ArrayMap<String, T> originalData, Map<String, T> newData) {
        ChangesBundle<T> changes = new ChangesBundle<>();

        for (Map.Entry<String, T> entry : newData.entrySet()) {
            T item = entry.getValue();
            int originalItemIndex = originalData.indexOfKey(entry.getKey());

            if (originalItemIndex < 0) {
                // new item was added
                changes.addNewItem(item);
                originalData.put(entry.getKey(), item);
            } else if (item == null) {
                // item was deleted
                changes.addDeletedItemPosition(originalItemIndex);
                originalData.removeAt(originalItemIndex);
            } else {
                // item was updated
                T originalItem = originalData.valueAt(originalItemIndex);
                T updatedItem = originalItem.update(item);

                changes.addUpdatedItem(updatedItem, originalItemIndex);
                originalData.setValueAt(originalItemIndex, updatedItem);
            }
        }

        return changes;
    }

    public static class ChangesBundle<T> {

        @Nullable
        private List<T> mNewItems;

        @Nullable
        private List<ItemWithPosition<T>> mUpdatedItems;

        @Nullable
        private List<Integer> mDeletedItemsPositions;

        private void addNewItem(T newItem) {
            if (mNewItems == null) {
                mNewItems = new ArrayList<>();
            }

            mNewItems.add(newItem);
        }

        private void addUpdatedItem(T updatedItem, int position) {
            if (mUpdatedItems == null) {
                mUpdatedItems = new ArrayList<>();
            }

            mUpdatedItems.add(new ItemWithPosition<>(updatedItem, position));
        }

        private void addDeletedItemPosition(int position) {
            if (mDeletedItemsPositions == null) {
                mDeletedItemsPositions = new ArrayList<>();
            }

            mDeletedItemsPositions.add(position);
        }

        @Nullable
        public List<T> getNewItems() {
            return mNewItems;
        }

        @Nullable
        public List<ItemWithPosition<T>> getUpdatedItems() {
            return mUpdatedItems;
        }

        @Nullable
        public List<Integer> getDeletedItemsPositions() {
            return mDeletedItemsPositions;
        }

        @Override
        public String toString() {
            return "ChangesBundle{" +
                    "\nmNewItems=" + mNewItems +
                    "\n, mUpdatedItems=" + mUpdatedItems +
                    "\n, mDeletedItemsPositions=" + mDeletedItemsPositions +
                    '}';
        }
    }

    public static class ItemWithPosition<T> {
        @Nullable
        private final T mItem;

        private final int mPosition;


        public ItemWithPosition(@Nullable T item, int position) {
            mItem = item;
            mPosition = position;
        }

        @Nullable
        public T getItem() {
            return mItem;
        }

        public int getPosition() {
            return mPosition;
        }

        @Override
        public String toString() {
            return "ItemWithPosition{" +
                    "mItem=" + mItem +
                    ", mPosition=" + mPosition +
                    '}';
        }
    }
}