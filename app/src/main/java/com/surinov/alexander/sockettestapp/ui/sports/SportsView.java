package com.surinov.alexander.sockettestapp.ui.sports;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.surinov.alexander.sockettestapp.data.rx.transformer.SwarmDataTransformer.ChangesBundle;
import com.surinov.alexander.sockettestapp.data.source.response.SportsResponse.SportItem;
import com.surinov.alexander.sockettestapp.utils.strategy.ClearStateStrategy;

import java.util.List;

interface SportsView extends MvpView {

    @StateStrategyType(value = ClearStateStrategy.class)
    void onCachedDataSet(List<SportItem> cachedData);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void onDataChanged(ChangesBundle<SportItem> changesBundle);
}
