package com.surinov.alexander.sockettestapp.data.source.request.generator;

public class SwarmRequestsGeneratorProvider {

    private static volatile SwarmRequestsGenerator sRequestsGenerator;

    public static SwarmRequestsGenerator requestsGenerator() {
        SwarmRequestsGenerator requestsGenerator = sRequestsGenerator;
        if (sRequestsGenerator == null) {
            synchronized (SwarmRequestsGeneratorProvider.class) {
                requestsGenerator = sRequestsGenerator;
                if (requestsGenerator == null) {
                    requestsGenerator = sRequestsGenerator = getDefaultSwarmRequestsGenerator();
                }
            }
        }

        return requestsGenerator;
    }

    private static SwarmRequestsGenerator getDefaultSwarmRequestsGenerator() {
        return new AppResourcesSwarmRequestsGenerator();
    }
}
