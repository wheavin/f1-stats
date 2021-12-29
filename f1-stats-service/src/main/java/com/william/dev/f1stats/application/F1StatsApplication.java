package com.william.dev.f1stats.application;

import org.glassfish.jersey.server.ResourceConfig;

public class F1StatsApplication extends ResourceConfig {

    public F1StatsApplication() {
        register(new ApplicationBinder());
        packages(true, "com.william.dev.f1stats.application");
    }
}
