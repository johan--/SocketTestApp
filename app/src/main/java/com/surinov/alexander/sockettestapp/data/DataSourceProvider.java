package com.surinov.alexander.sockettestapp.data;

import com.surinov.alexander.sockettestapp.data.source.DataSource;
import com.surinov.alexander.sockettestapp.data.source.WebSocketDataSource;
import com.surinov.alexander.sockettestapp.utils.Logger;

import okhttp3.OkHttpClient;

public class DataSourceProvider {
    private static volatile OkHttpClient sOkHttpClient;

    private static volatile DataSource sDataSource;

    private static OkHttpClient provideOkHttpClientInstance() {
        Logger.d("DataSourceProvider.provideOkHttpClientInstance");

        OkHttpClient okHttpClient = sOkHttpClient;
        if (okHttpClient == null) {
            synchronized (DataSourceProvider.class) {
                okHttpClient = sOkHttpClient;
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient();

                    // configure OkHttpClient
                    // ...

                    Logger.d("DataSourceProvider.provideOkHttpClientInstance: " + "Create new instance");
                    sOkHttpClient = okHttpClient;
                }
            }
        }

        return okHttpClient;
    }

    public static DataSource provideWebSocketDataSourceInstance() {
        Logger.d("DataSourceProvider.provideWebSocketDataSourceInstance");

        DataSource dataSource = sDataSource;
        if (dataSource == null) {
            synchronized (DataSourceProvider.class) {
                dataSource = sDataSource;
                if (dataSource == null) {
                    OkHttpClient okHttpClient = provideOkHttpClientInstance();

                    Logger.d("DataSourceProvider.provideWebSocketDataSourceInstance: " + "Create new instance");
                    sDataSource = dataSource = new WebSocketDataSource(okHttpClient);
                }
            }
        }

        return dataSource;
    }
}