package com.surinov.alexander.sockettestapp.ui.sports;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.surinov.alexander.sockettestapp.R;
import com.surinov.alexander.sockettestapp.data.provider.SwarmRepositoryProvider;
import com.surinov.alexander.sockettestapp.data.rx.transformer.SwarmDataTransformer;
import com.surinov.alexander.sockettestapp.data.source.response.SportsResponse;
import com.surinov.alexander.sockettestapp.utils.Logger;

import java.util.List;
import java.util.Map;

public class SportsFragment extends MvpAppCompatFragment implements SportsView {

    @InjectPresenter
    SportsPresenter mPresenter;

    @ProvidePresenter
    SportsPresenter providePresenter() {
        return new SportsPresenter(SwarmRepositoryProvider.INSTANCE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.framgent_simple, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.d("SimpleFragment.onViewCreated");

        final View requestButton = view.findViewById(R.id.requestButton);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final View closeConnectionButton = view.findViewById(R.id.closeConnectionButton);
        closeConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void setSportItems(List<SportsResponse.SportItem> sportItems) {
        Logger.d("SimpleFragment.onViewCreated.setSportItems: " + sportItems);
    }

    @Override
    public void updateSportItems(List<SwarmDataTransformer.UpdateItem<SportsResponse.SportItem>> updateItems) {
        Logger.d("SimpleFragment.onViewCreated.updateSportItems: " + updateItems);
    }
}