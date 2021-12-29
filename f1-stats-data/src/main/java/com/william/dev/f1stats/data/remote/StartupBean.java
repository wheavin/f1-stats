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
        if (isUpdateOnStartup()) {
            log.info("Updating data...");
            dataUpdaterBean.updateCircuits();
            dataUpdaterBean.updateDrivers();
        } else {
            log.info("Skipping data update");
        }
    }

    private boolean isUpdateOnStartup() {
        try {
            return Boolean.parseBoolean(System.getProperty("f1stats.updateonstartup"));
        } catch (final Exception ex) {
            log.info("System property not set", ex);
            return false;
        }
    }
}
