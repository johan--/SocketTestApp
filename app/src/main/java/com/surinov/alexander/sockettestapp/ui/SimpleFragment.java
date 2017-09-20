package com.surinov.alexander.sockettestapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surinov.alexander.sockettestapp.R;
import com.surinov.alexander.sockettestapp.data.provider.DataSourceProvider;
import com.surinov.alexander.sockettestapp.data.repository.SportEventsRepository;
import com.surinov.alexander.sockettestapp.data.repository.SportLiveEventsRepositoryImpl;
import com.surinov.alexander.sockettestapp.data.source.response.sport.SwarmSportListResponse;
import com.surinov.alexander.sockettestapp.data.source.response.sport.SwarmSportResponse;
import com.surinov.alexander.sockettestapp.utils.Logger;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

public class SimpleFragment extends Fragment {

    public static final String BUNDLE_REQUEST_DATA_WITH_UPDATES = "com.surinov.alexander.sockettestapp.ui.request_data_with_updates";

    private SportEventsRepository mSportEventsRepository =
            new SportLiveEventsRepositoryImpl(DataSourceProvider.provideWebSocketDataSourceInstance());

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

        mSubscription = mSportEventsRepository.requestSportLiveEventsObservable(1)
                .subscribe(new Subscriber<SwarmSportListResponse>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("SimpleFragment.performRequest.requestSportLiveEventsObservable.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("SimpleFragment.performRequest.requestSportLiveEventsObservable.onError: " + e);
                    }

                    @Override
                    public void onNext(SwarmSportListResponse response) {
                        List<SwarmSportResponse> sports = response.getSportList();
                        Logger.d("SimpleFragment.performRequest.requestSportLiveEventsObservable.onNext: " + sports);
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