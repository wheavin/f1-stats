package com.william.dev.f1stats.data.remote;

import com.william.dev.f1.stats.remote.RemoteClient;
import com.william.dev.f1stats.data.api.Circuit;
import com.william.dev.f1stats.data.api.Driver;
import com.william.dev.f1stats.data.api.Team;
import com.william.dev.f1stats.data.db.circuits.CircuitDatabaseClient;
import com.william.dev.f1stats.data.db.drivers.DriverDatabaseClient;
import com.william.dev.f1stats.data.db.teams.TeamDatabaseClient;
import com.william.dev.f1stats.data.exception.DataInsertionException;
import com.william.dev.f1stats.data.exception.DataParseException;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DataUpdaterBean {

    @Inject
    private RemoteClient remoteClient;

    @Inject
    private CircuitDatabaseClient circuitDatabaseClient;

    @Inject
    private DriverDatabaseClient driverDatabaseClient;

    @Inject
    private TeamDatabaseClient teamDatabaseClient;

    private final DataParser dataParser = new DataParser();

    public void updateCircuits() {
        log.info("Updating all circuits");
        final long startTime = System.nanoTime();
        final String circuitData = remoteClient.getAllCircuits();
        try {
            final Set<Circuit> circuitsToAdd = dataParser.parseCircuits(circuitData);
            circuitDatabaseClient.addCircuits(circuitsToAdd);
            log.info("Update of circuits completed in {}s. Number of circuits inserted = {}",
                    durationAsSeconds(startTime), circuitsToAdd.size());
        } catch (final DataParseException | DataInsertionException ex) {
            log.error("Error updating circuit data", ex);
        }
    }

    public void updateDrivers() {
        log.info("Updating all drivers");
        final long startTime = System.nanoTime();
        final String driverData = remoteClient.getAllDrivers();
        try {
            final Set<Driver> driversToAdd = dataParser.parseDrivers(driverData);
            driverDatabaseClient.addDrivers(driversToAdd);
            log.info("Update of drivers completed in {}s. Number of drivers inserted = {}",
                    durationAsSeconds(startTime), driversToAdd.size());
        } catch (final DataParseException | DataInsertionException ex) {
            log.error("Error updating driver data", ex);
        }
    }

    public void updateTeams() {
        log.info("Updating all teams");
        final long startTime = System.nanoTime();
        final String teamData = remoteClient.getAllTeams();
        try {
            final Set<Team> teamsToAdd = dataParser.parseTeams(teamData);
            teamDatabaseClient.addTeams(teamsToAdd);
            log.info("Update of teams completed in {}s. Number of teams inserted = {}",
                    durationAsSeconds(startTime), teamsToAdd.size());
        } catch (final DataParseException | DataInsertionException ex) {
            log.error("Error updating team data", ex);
        }
    }

    private long durationAsSeconds(final long startTimeNanoSeconds) {
        return TimeUnit.SECONDS.convert(System.nanoTime() - startTimeNanoSeconds, TimeUnit.NANOSECONDS);
    }
}
