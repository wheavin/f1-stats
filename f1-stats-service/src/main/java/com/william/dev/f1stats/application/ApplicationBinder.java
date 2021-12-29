package com.william.dev.f1stats.application;

import com.william.dev.f1.stats.remote.ErgastClient;
import com.william.dev.f1.stats.remote.RemoteClient;
import com.william.dev.f1stats.data.StatsDataServiceImpl;
import com.william.dev.f1stats.data.api.StatsDataService;
import com.william.dev.f1stats.data.db.ConnectionFactory;
import com.william.dev.f1stats.data.db.SqliteConnectionFactory;
import com.william.dev.f1stats.data.db.circuits.CircuitDatabaseClient;
import com.william.dev.f1stats.data.db.circuits.SqliteCircuitDatabaseClient;
import com.william.dev.f1stats.data.db.drivers.DriverDatabaseClient;
import com.william.dev.f1stats.data.db.drivers.SqliteDriverDatabaseClient;
import org.glassfish.jersey.internal.inject.AbstractBinder;

public class ApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(StatsDataServiceImpl.class).to(StatsDataService.class);
        bind(SqliteCircuitDatabaseClient.class).to(CircuitDatabaseClient.class);
        bind(SqliteDriverDatabaseClient.class).to(DriverDatabaseClient.class);
        bind(SqliteConnectionFactory.class).to(ConnectionFactory.class);
        bind(ErgastClient.class).to(RemoteClient.class);
    }
}
