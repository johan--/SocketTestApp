package com.surinov.alexander.sockettestapp.ui.sports;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.surinov.alexander.sockettestapp.data.repository.SwarmRepository;
import com.surinov.alexander.sockettestapp.data.rx.transformer.ReceivedDataTransformer;
import com.surinov.alexander.sockettestapp.data.rx.transformer.ReceivedDataTransformer.ChangesBundle;
import com.surinov.alexander.sockettestapp.data.source.request.SportsRequest;
import com.surinov.alexander.sockettestapp.data.source.response.SportsResponse;
import com.surinov.alexander.sockettestapp.data.source.response.SportsResponse.SportItem;
import com.surinov.alexander.sockettestapp.utils.Logger;

import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

@InjectViewState
public class SportsPresenter extends MvpPresenter<SportsView> {

    @NonNull
    private final SwarmRepository mSwarmRepository;

    @Nullable
    private Subscription mSubscription;

    private final ReceivedDataTransformer<SportItem> mReceivedDataTransformer = new ReceivedDataTransformer<>();

    SportsPresenter(@NonNull SwarmRepository swarmRepository) {
        mSwarmRepository = swarmRepository;
    }

    @Override
    public void attachView(SportsView view) {
        super.attachView(view);
        fetchData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    void fetchCachedData() {
        ArrayMap<String, SportItem> cachedData = mReceivedDataTransformer.getCachedData();
        getViewState().onCachedDataLoaded(cachedData.values());
    }

    private void fetchData() {
        Logger.d("SportsPresenter.fetchData");
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            return;
        }

        SportsRequest sportsRequest = new SportsRequest(/*live sport events*/ 1, /*subscribe for updates*/ true);

        mSubscription = mSwarmRepository.fetchObservableSwarmData(SportsResponse.class, sportsRequest)
                .map(new Func1<SportsResponse, Map<String, SportItem>>() {
                    @Override
                    public Map<String, SportItem> call(SportsResponse sportsResponse) {
                        return sportsResponse.getSportMap();
                    }
                })
                .compose(mReceivedDataTransformer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangesBundle<SportItem>>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("SportsPresenter.fetchData.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("SportsPresenter.fetchData.onError: " + e);
                    }

                    @Override
                    public void onNext(ChangesBundle<SportItem> changesBundle) {
                        Logger.d("SportsPresenter.fetchData.onNext");
                        getViewState().onDataChanged(changesBundle);
                    }
                });
    }
}