package com.william.dev.f1stats.data.db.drivers;

import com.william.dev.f1stats.data.api.Driver;
import com.william.dev.f1stats.data.exception.DataInsertionException;
import com.william.dev.f1stats.data.exception.DataServiceException;

import java.util.Optional;
import java.util.Set;

public interface DriverDatabaseClient {

    Set<Driver> getAllDrivers() throws DataServiceException;

    Optional<Driver> getDriver(String firstName, String lastName) throws DataServiceException;

    void addDrivers(Set<Driver> drivers) throws DataInsertionException;
}
