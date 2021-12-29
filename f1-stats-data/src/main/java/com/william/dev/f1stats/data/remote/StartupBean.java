package com.william.dev.f1stats.data.remote;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
@Slf4j
public class StartupBean {

    @Inject
    private DataUpdaterBean dataUpdaterBean;

    @PostConstruct
    private void init() {
        log.info("Application has started");
        dataUpdaterBean.updateCircuits();
        dataUpdaterBean.updateDrivers();
    }
}
