package com.surinov.alexander.sockettestapp;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.Nullable;

import com.surinov.alexander.sockettestapp.data.provider.DataSourceProvider;
import com.surinov.alexander.sockettestapp.data.source.ConnectionControl;
import com.surinov.alexander.sockettestapp.utils.Logger;

import java.util.concurrent.TimeUnit;

import rx.Completable;
import rx.Subscription;
import rx.functions.Action0;

public class App extends Application {

    @Nullable
    private Subscription mTimerSubscription;

    private final ConnectionControl mWebSocketConnectionControl = DataSourceProvider.provideWebSocketDataSourceInstance();

    private final AppLifecycle mAppLifecycle = new AppLifecycle() {
        @Override
        public void onActivityStarted(Activity activity) {
            if (mTimerSubscription != null && !mTimerSubscription.isUnsubscribed()) {
                Logger.d("App.onActivityStopped: prevent close connection");
                mTimerSubscription.unsubscribe();
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Logger.d("App.onActivityStopped: start close connection timer");

            mTimerSubscription = Completable.complete()
                    .delay(5, TimeUnit.SECONDS)
                    .subscribe(new Action0() {
                        @Override
                        public void call() {
                            if (mWebSocketConnectionControl.isConnectionOpened()) {
                                Logger.d("App.onActivityStopped: close connection");
                                mWebSocketConnectionControl.closeConnection();
                            }
                        }
                    });
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("App.onCreate");

        registerActivityLifecycleCallbacks(mAppLifecycle);
    }
}