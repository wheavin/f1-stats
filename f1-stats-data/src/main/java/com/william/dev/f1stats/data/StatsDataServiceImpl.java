package com.william.dev.f1stats.data;

import com.william.dev.f1stats.data.api.Circuit;
import com.william.dev.f1stats.data.api.Driver;
import com.william.dev.f1stats.data.api.StatsDataService;
import com.william.dev.f1stats.data.api.Team;
import com.william.dev.f1stats.data.db.circuits.CircuitDatabaseClient;
import com.william.dev.f1stats.data.db.drivers.DriverDatabaseClient;
import com.william.dev.f1stats.data.db.teams.TeamDatabaseClient;
import com.william.dev.f1stats.data.exception.DataServiceException;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

public class StatsDataServiceImpl implements StatsDataService {

    @Inject
    private CircuitDatabaseClient circuitDatabaseClient;

    @Inject
    private DriverDatabaseClient driverDatabaseClient;

    @Inject
    private TeamDatabaseClient teamDatabaseClient;

    @Override
    public Set<Circuit> listAllCircuits() throws DataServiceException {
        return circuitDatabaseClient.getAllCircuits();
    }

    @Override
    public Set<String> listAllCircuitNames() throws DataServiceException {
        return circuitDatabaseClient.getAllCircuitNames();
    }

    @Override
    public Optional<Circuit> getCircuit(final String name) throws DataServiceException {
        return circuitDatabaseClient.getCircuit(name);
    }

    @Override
    public Set<Driver> listAllDrivers() throws DataServiceException {
        return driverDatabaseClient.getAllDrivers();
    }

    @Override
    public Optional<Driver> getDriver(String firstName, String lastName) throws DataServiceException {
        return driverDatabaseClient.getDriver(firstName, lastName);
    }

    @Override
    public Set<Team> listAllTeams() throws DataServiceException {
        return teamDatabaseClient.getAllTeams();
    }

    @Override
    public Set<String> listAllTeamNames() throws DataServiceException {
        return teamDatabaseClient.getAllTeamNames();
    }

    @Override
    public Optional<Team> getTeam(final String name) throws DataServiceException {
        return teamDatabaseClient.getTeam(name);
    }
}
