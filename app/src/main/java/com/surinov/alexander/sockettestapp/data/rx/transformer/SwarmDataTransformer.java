package com.surinov.alexander.sockettestapp.data.rx.transformer;

import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.surinov.alexander.sockettestapp.data.source.response.Updatable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SwarmDataTransformer<T extends Updatable<T>> implements Observable.Transformer<Map<String, T>, SwarmDataTransformer.Result<T>> {

    private final ArrayMap<String, T> mOriginalData = new ArrayMap<>();

    public List<T> getOriginalItems() {
        return new ArrayList<>(mOriginalData.values());
    }

    @Override
    public Observable<Result<T>> call(Observable<Map<String, T>> source) {
        return source
                .observeOn(Schedulers.computation())
                .map(new Func1<Map<String, T>, Result<T>>() {

                    private boolean firstEmission = true;

                    @Override
                    public Result<T> call(Map<String, T> newData) {
                        if (firstEmission) {
                            firstEmission = false;
                            mOriginalData.putAll(newData);
                            return Result.withOriginalData(new ArrayList<>(newData.values()));
                        }

                        List<UpdateItem<T>> updateData = prepareUpdateData(mOriginalData, newData);
                        return Result.withUpdateData(updateData);
                    }
                });
    }

    private List<UpdateItem<T>> prepareUpdateData(ArrayMap<String, T> originalData, Map<String, T> newData) {
        List<UpdateItem<T>> result = new ArrayList<>();

        for (Map.Entry<String, T> newEntry : newData.entrySet()) {
            T newItem = newEntry.getValue();
            int originalItemIndex = originalData.indexOfKey(newEntry.getKey());

            if (originalItemIndex == -1) {
                // new item was added
                result.add(UpdateItem.added(newItem));
            } else if (newItem == null) {
                // item was deleted
                result.add(UpdateItem.<T>deleted(originalItemIndex));
                originalData.removeAt(originalItemIndex);
            } else {
                // item was updated
                T originalItem = originalData.valueAt(originalItemIndex);
                T updatedItem = originalItem.update(newItem);

                originalData.setValueAt(originalItemIndex, updatedItem);
                result.add(UpdateItem.updated(updatedItem, originalItemIndex));
            }
        }

        return result;
    }

    public static class Result<T> {
        @Nullable
        private final List<T> mOriginalData;

        @Nullable
        private final List<UpdateItem<T>> mUpdateData;

        private Result(@Nullable List<T> originalData, @Nullable List<UpdateItem<T>> updateData) {
            mOriginalData = originalData;
            mUpdateData = updateData;
        }

        @Nullable
        public List<T> getOriginalData() {
            return mOriginalData;
        }

        @Nullable
        public List<UpdateItem<T>> getUpdateData() {
            return mUpdateData;
        }

        public boolean isOriginalDataResult() {
            return mOriginalData != null;
        }

        public static <T> Result<T> withOriginalData(List<T> originalData) {
            return new Result<>(originalData, null);
        }

        public static <T> Result<T> withUpdateData(List<UpdateItem<T>> updateData) {
            return new Result<>(null, updateData);
        }
    }

    public static class UpdateItem<T> {
        public enum Type {
            ADDED, DELETED, UPDATED
        }

        private final Type mType;

        @Nullable
        private final T mItem;

        private final int mPositionInDataSet;

        public Type getType() {
            return mType;
        }

        @Nullable
        public T getItem() {
            return mItem;
        }

        public int getPositionInDataSet() {
            return mPositionInDataSet;
        }

        private UpdateItem(Type type, @Nullable T item, int positionInDataSet) {
            mType = type;
            mItem = item;
            mPositionInDataSet = positionInDataSet;
        }

        public static <T> UpdateItem<T> deleted(int position) {
            return new UpdateItem<>(Type.DELETED, null, position);
        }

        public static <T> UpdateItem<T> added(T item) {
            return new UpdateItem<>(Type.ADDED, item, -1);
        }

        public static <T> UpdateItem<T> updated(T item, int position) {
            return new UpdateItem<>(Type.UPDATED, item, position);
        }

        @Override
        public String toString() {
            return "UpdateItem{" +
                    "mType=" + mType +
                    ", mItem=" + mItem +
                    ", mPositionInDataSet=" + mPositionInDataSet +
                    '}';
        }
    }
}