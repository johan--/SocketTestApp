package com.surinov.alexander.sockettestapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surinov.alexander.sockettestapp.R;
import com.surinov.alexander.sockettestapp.data.DataSourceProvider;
import com.surinov.alexander.sockettestapp.data.repository.SportEventsRepository;
import com.surinov.alexander.sockettestapp.data.repository.SportLiveEventsRepositoryImpl;
import com.surinov.alexander.sockettestapp.utils.Logger;

import rx.SingleSubscriber;
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

        final View openConnectionButton = view.findViewById(R.id.openConnectionButton);
        openConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConnection();
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

    private void openConnection() {
        final boolean requestDataWithUpdates = getArguments().getBoolean(BUNDLE_REQUEST_DATA_WITH_UPDATES, false);

        if (requestDataWithUpdates) {
            mSubscription = mSportEventsRepository.requestSportLiveEventsObservable()
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            Logger.d("SimpleFragment.openConnection.requestSportLiveEventsObservable.onCompleted");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.d("SimpleFragment.openConnection.requestSportLiveEventsObservable.onError: " + e);
                        }

                        @Override
                        public void onNext(String s) {
                            Logger.d("SimpleFragment.openConnection.requestSportLiveEventsObservable.onNext: " + s);
                        }
                    });
        } else {
            mSubscription = mSportEventsRepository.requestSportEventsSingle()
                    .subscribe(new SingleSubscriber<String>() {
                        @Override
                        public void onSuccess(String s) {
                            Logger.d("SimpleFragment.openConnection.requestSportEventsSingle.onSuccess: " + s);
                        }

                        @Override
                        public void onError(Throwable error) {
                            Logger.d("SimpleFragment.openConnection.requestSportEventsSingle.onError: " + error);
                        }
                    });
        }
    }

    private void closeConnection() {
        unsubscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        unsubscribe();
    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}