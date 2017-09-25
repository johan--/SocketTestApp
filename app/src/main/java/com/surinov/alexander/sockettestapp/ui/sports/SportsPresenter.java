package com.surinov.alexander.sockettestapp.ui.sports;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.surinov.alexander.sockettestapp.data.repository.SwarmRepository;
import com.surinov.alexander.sockettestapp.data.source.request.SportsRequest;
import com.surinov.alexander.sockettestapp.data.source.response.SportsResponse;
import com.surinov.alexander.sockettestapp.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class SportsPresenter extends MvpPresenter<SportsView> {

    @NonNull
    private final SwarmRepository mSwarmRepository;

    @Nullable
    private Subscription mSubscription;

    @Nullable
    private SportsResponse mOriginalData;

    private ArrayMap<String, SportsResponse.SportItem> mOriginalSportsMap = new ArrayMap<>();

    SportsPresenter(@NonNull SwarmRepository swarmRepository) {
        mSwarmRepository = swarmRepository;
    }

    @Override
    public void attachView(SportsView view) {
        super.attachView(view);
        fetchSports();
    }

    private void fetchSports() {
        Logger.d("SportsPresenter.fetchSports");

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            return;
        }


        SportsRequest sportsRequest = new SportsRequest(/*live sport events*/ 1, /*subscribe for updates*/ true);

        mSubscription = mSwarmRepository.fetchSwarmDataObservable(SportsResponse.class, sportsRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SportsResponse>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("SportsPresenter.fetchSports.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("SportsPresenter.fetchSports.onError: " + e);
                    }

                    @Override
                    public void onNext(SportsResponse sportsResponse) {
                        Logger.d("SportsPresenter.fetchSports.onNext: " + sportsResponse);
                        mOriginalSportsMap.putAll(sportsResponse.getSportMap());
                        handleSportsResponse(sportsResponse);
                    }
                });
    }

    private void handleSportsResponse(SportsResponse sportsResponse) {
        if (mOriginalData == null) {
            mOriginalData = sportsResponse;
            getViewState().setSportItems(sportsResponse.getSportItems());
            return;
        }

        prepareModifiedItemsList(sportsResponse);
    }

    private List<ModifiedItem> prepareModifiedItemsList(SportsResponse updatesData) {
        List<ModifiedItem> modifiedItems = new ArrayList<>();

        Map<String, SportsResponse.SportItem> updatedMap = updatesData.getSportMap();

        for (Map.Entry<String, SportsResponse.SportItem> updatedEntry : updatedMap.entrySet()) {
            SportsResponse.SportItem updatedItem = updatedEntry.getValue();
            int originalItemIndex = mOriginalSportsMap.indexOfKey(updatedEntry.getKey());

            if (originalItemIndex == -1) {
                // new sport item was added
                modifiedItems.add(new ModifiedItem(ModificationType.ADDED, updatedItem));
            } else if (updatedItem == null) {
                // sport item was deleted
                modifiedItems.add(new ModifiedItem(ModificationType.DELETED, originalItemIndex));
                mOriginalSportsMap.removeAt(originalItemIndex);
            } else {
                SportsResponse.SportItem originalItem = mOriginalSportsMap.valueAt(originalItemIndex);

                // update sport item logic
                // ...

                modifiedItems.add(new ModifiedItem(ModificationType.UPDATED, updatedItem, originalItemIndex));
            }
        }

        return modifiedItems;
    }

    enum ModificationType {
        ADDED, DELETED, UPDATED
    }

    private static class ModifiedItem {
        private final ModificationType mModificationType;

        @Nullable
        private final SportsResponse.SportItem mSportItem;

        private final int mPosition;

        public ModifiedItem(ModificationType modificationType, @Nullable SportsResponse.SportItem sportItem, int position) {
            mModificationType = modificationType;
            mSportItem = sportItem;
            mPosition = position;
        }

        public ModifiedItem(ModificationType modificationType, @Nullable SportsResponse.SportItem sportItem) {
            mModificationType = modificationType;
            mSportItem = sportItem;
            mPosition = -1;
        }

        public ModifiedItem(ModificationType modificationType, int position) {
            mModificationType = modificationType;
            mSportItem = null;
            mPosition = position;
        }
    }
}