package com.surinov.alexander.sockettestapp.ui.sports;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.surinov.alexander.sockettestapp.R;
import com.surinov.alexander.sockettestapp.data.provider.SwarmRepositoryProvider;
import com.surinov.alexander.sockettestapp.data.rx.transformer.ReceivedDataTransformer.ChangesBundle;
import com.surinov.alexander.sockettestapp.data.source.response.SportsResponse.SportItem;
import com.surinov.alexander.sockettestapp.utils.Logger;

import java.util.Collection;

public class SportsFragment extends MvpAppCompatFragment implements SportsView {

    private final SportsAdapter mAdapter = new SportsAdapter();

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

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.fetchCachedData();
    }

    @Override
    public void onCachedDataLoaded(@NonNull Collection<SportItem> cachedData) {
        Logger.d("SimpleFragment.onViewCreated.onCachedDataLoaded: " + cachedData);
        mAdapter.setData(cachedData);
    }

    @Override
    public void onDataChanged(@NonNull ChangesBundle<SportItem> changesBundle) {
        Logger.d("SimpleFragment.onViewCreated.onDataChanged: " + changesBundle);
        mAdapter.onDataChanged(changesBundle);
    }
}