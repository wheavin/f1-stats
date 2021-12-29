package com.william.dev.f1stats.data.api;

import com.william.dev.f1stats.data.exception.DataServiceException;

import java.util.Optional;
import java.util.Set;

public interface StatsDataService {

    Set<Circuit> listAllCircuits() throws DataServiceException;

    Set<String> listAllCircuitNames() throws DataServiceException;

    Optional<Circuit> getCircuit(String name) throws DataServiceException;

    Set<Driver> listAllDrivers() throws DataServiceException;

    Optional<Driver> getDriver(String firstName, String lastName) throws DataServiceException;
}
