package com.surinov.alexander.sockettestapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.surinov.alexander.sockettestapp.data.provider.DataSourceProvider;
import com.surinov.alexander.sockettestapp.data.source.DataSource;
import com.surinov.alexander.sockettestapp.utils.Logger;

import java.util.concurrent.TimeUnit;

import rx.Completable;
import rx.Subscription;
import rx.functions.Action0;

public class App extends Application {

    @Nullable
    private Subscription mTimerSubscription;

    private final DataSource mDataSource = DataSourceProvider.dataSource();

    private static Context sApplicationContext;

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

//            mTimerSubscription = Completable.complete()
//                    .delay(5, TimeUnit.SECONDS)
//                    .subscribe(new Action0() {
//                        @Override
//                        public void call() {
//                            if (mDataSource.isConnectionOpened()) {
//                                Logger.d("App.onActivityStopped: close connection");
//                                mDataSource.closeConnection();
//                            }
//                        }
//                    });
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("App.onCreate");

        sApplicationContext = getApplicationContext();

        registerActivityLifecycleCallbacks(mAppLifecycle);
    }

    public static Context getAppContext() {
        return sApplicationContext;
    }
}