package com.surinov.alexander.sockettestapp.ui.sports;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.surinov.alexander.sockettestapp.data.repository.SwarmRepository;
import com.surinov.alexander.sockettestapp.data.source.request.SportsRequest;
import com.surinov.alexander.sockettestapp.data.source.response.SportsResponse;
import com.surinov.alexander.sockettestapp.utils.Logger;

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

        mSubscription = mSwarmRepository.fetchSports(sportsRequest)
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

        handleUpdate(mOriginalData, sportsResponse);
    }

    private void handleUpdate(SportsResponse originalData, SportsResponse updatesData) {

    }
}