package com.surinov.alexander.sockettestapp.data.provider;

import com.surinov.alexander.sockettestapp.data.repository.SwarmRepository;
import com.surinov.alexander.sockettestapp.data.repository.SwarmRepositoryImpl;

public class SwarmRepositoryProvider {

    public static final SwarmRepository INSTANCE = new SwarmRepositoryImpl(DataSourceProvider.dataSource());

}

