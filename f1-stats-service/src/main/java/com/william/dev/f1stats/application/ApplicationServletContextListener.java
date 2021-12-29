package com.william.dev.f1stats.application;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Slf4j
public class ApplicationServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(final ServletContextEvent contextEvent) {
        log.info("Context initialized");
    }

    @Override
    public void contextDestroyed(final ServletContextEvent contextEvent) {
        log.info("Context destroyed");
    }
}
