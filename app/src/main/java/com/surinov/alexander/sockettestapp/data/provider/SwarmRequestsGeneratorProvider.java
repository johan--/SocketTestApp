package com.surinov.alexander.sockettestapp.data.provider;

import com.surinov.alexander.sockettestapp.data.source.request.generator.AppResourcesSwarmRequestsGenerator;
import com.surinov.alexander.sockettestapp.data.source.request.generator.SwarmRequestsGenerator;

public class SwarmRequestsGeneratorProvider {

    public static final SwarmRequestsGenerator INSTANCE = new AppResourcesSwarmRequestsGenerator();

}
