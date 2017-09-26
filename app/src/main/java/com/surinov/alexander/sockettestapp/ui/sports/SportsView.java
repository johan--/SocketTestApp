package com.surinov.alexander.sockettestapp.ui.sports;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.surinov.alexander.sockettestapp.data.rx.transformer.SwarmDataTransformer.ChangesBundle;
import com.surinov.alexander.sockettestapp.data.source.response.SportsResponse.SportItem;

public interface SportsView extends MvpView {

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void onDataChanged(ChangesBundle<SportItem> changesBundle);
}
