package com.surinov.alexander.sockettestapp.ui.sports;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.surinov.alexander.sockettestapp.data.source.response.SportsResponse;

import java.util.List;
import java.util.Map;

public interface SportsView extends MvpView {

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void setSportItems(List<SportsResponse.SportItem> sportItems);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void updateSportItems(Map<Integer, SportsResponse.SportItem> sportItemsWithPositions);
}
