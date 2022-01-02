package com.william.dev.f1stats.data.remote;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import static com.william.dev.f1stats.common.Constants.UPDATE_DATA_ON_STARTUP;

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
            dataUpdaterBean.updateTeams();
        } else {
            log.info("Skipping data update");
        }
    }

    private boolean isUpdateOnStartup() {
        try {
            return Boolean.parseBoolean(System.getenv(UPDATE_DATA_ON_STARTUP));
        } catch (final Exception ex) {
            log.info("System property not set", ex);
            return false;
        }
    }
}
