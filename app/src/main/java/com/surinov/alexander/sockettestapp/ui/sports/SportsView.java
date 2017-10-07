package com.surinov.alexander.sockettestapp.ui.sports;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.surinov.alexander.sockettestapp.data.rx.transformer.ReceivedDataTransformer.ChangesBundle;
import com.surinov.alexander.sockettestapp.data.source.response.SportsResponse.SportItem;
import com.surinov.alexander.sockettestapp.utils.strategy.ClearStateStrategy;

import java.util.Collection;
import java.util.List;

interface SportsView extends MvpView {

    @StateStrategyType(value = ClearStateStrategy.class)
    void onCachedDataLoaded(@NonNull Collection<SportItem> cachedData);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void onDataChanged(@NonNull ChangesBundle<SportItem> changesBundle);
}
