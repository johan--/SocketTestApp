package com.surinov.alexander.sockettestapp.ui.sports;

import android.support.v4.util.SimpleArrayMap;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.surinov.alexander.sockettestapp.data.rx.transformer.SwarmDataTransformer.ChangesBundle;
import com.surinov.alexander.sockettestapp.data.source.response.SportsResponse.SportItem;
import com.surinov.alexander.sockettestapp.utils.strategy.ClearStateStrategy;

public interface SportsView extends MvpView {

    @StateStrategyType(value = ClearStateStrategy.class)
    void onCachedDataSet(SimpleArrayMap<String, SportItem> cachedData);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void onChangesReceive(ChangesBundle<SportItem> changesBundle);

}
