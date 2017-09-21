package com.surinov.alexander.sockettestapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.surinov.alexander.sockettestapp.R;
import com.surinov.alexander.sockettestapp.data.provider.DataSourceProvider;
import com.surinov.alexander.sockettestapp.data.repository.SwarmRepository;
import com.surinov.alexander.sockettestapp.data.repository.SwarmRepositoryImpl;
import com.surinov.alexander.sockettestapp.data.source.request.SwarmRequest;
import com.surinov.alexander.sockettestapp.data.source.request.SwarmSportRequest;
import com.surinov.alexander.sockettestapp.utils.Logger;

import rx.Subscriber;
import rx.Subscription;

public class SimpleFragment extends Fragment {

    public static final String BUNDLE_REQUEST_DATA_WITH_UPDATES = "com.surinov.alexander.sockettestapp.ui.request_data_with_updates";

    private SwarmRepository mSwarmRepository =
            new SwarmRepositoryImpl(DataSourceProvider.webSocketDataSourceInstance());

    @Nullable
    private Subscription mSubscription;

    public static Fragment newInstance(boolean requestDataWithUpdates) {

        Bundle args = new Bundle();
        args.putBoolean(BUNDLE_REQUEST_DATA_WITH_UPDATES, requestDataWithUpdates);

        Fragment fragment = new SimpleFragment();
        fragment.setArguments(args);
        return fragment;
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
                performRequest();
            }
        });

        final View closeConnectionButton = view.findViewById(R.id.closeConnectionButton);
        closeConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeConnection();
            }
        });
    }

    private void performRequest() {
        unsubscribe();

//        mSubscription = mSwarmRepository.requestSwarmDataWithUpdates(1)
//                .subscribe(new Subscriber<JsonObject>() {
//                    @Override
//                    public void onCompleted() {
//                        Logger.d("SimpleFragment.performRequest.requestSwarmDataWithUpdates.onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Logger.d("SimpleFragment.performRequest.requestSwarmDataWithUpdates.onError: " + e);
//                    }
//
//                    @Override
//                    public void onNext(JsonObject response) {
//                        Logger.d("SimpleFragment.performRequest.requestSwarmDataWithUpdates.onNext: " + response);
//                    }
//                });

        SwarmRequest swarmRequest = new SwarmSportRequest(1, true);
        mSubscription = mSwarmRepository.requestSwarmDataWithUpdates(swarmRequest)
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("SimpleFragment.performRequest.requestSwarmDataWithUpdates.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("SimpleFragment.performRequest.requestSwarmDataWithUpdates.onError: " + e);
                    }

                    @Override
                    public void onNext(JsonObject response) {
                        Logger.d("SimpleFragment.performRequest.requestSwarmDataWithUpdates.onNext: " + response);
                    }
                });

    }

    private void closeConnection() {
        unsubscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}