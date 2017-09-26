package com.surinov.alexander.sockettestapp.ui.sports;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.surinov.alexander.sockettestapp.data.repository.SwarmRepository;
import com.surinov.alexander.sockettestapp.data.rx.transformer.SwarmDataTransformer;
import com.surinov.alexander.sockettestapp.data.rx.transformer.SwarmDataTransformer.ChangesBundle;
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

    private final SwarmDataTransformer<SportItem> mSwarmDataTransformer = new SwarmDataTransformer<>();

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
                .map(new Func1<SportsResponse, Map<String, SportItem>>() {
                    @Override
                    public Map<String, SportItem> call(SportsResponse sportsResponse) {
                        return sportsResponse.getSportMap();
                    }
                })
                .compose(mSwarmDataTransformer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangesBundle<SportItem>>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("SportsPresenter.fetchSports.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("SportsPresenter.fetchSports.onError: " + e);
                    }

                    @Override
                    public void onNext(ChangesBundle<SportItem> changesBundle) {
                        Logger.d("SportsPresenter.fetchSports.onNext: " + changesBundle);
                        getViewState().onDataChanged(changesBundle);
                    }
                });
    }
}