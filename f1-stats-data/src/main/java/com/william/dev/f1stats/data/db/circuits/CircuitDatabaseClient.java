package com.william.dev.f1stats.data.db.circuits;

import com.william.dev.f1stats.data.api.Circuit;
import com.william.dev.f1stats.data.exception.DataInsertionException;
import com.william.dev.f1stats.data.exception.DataServiceException;

import java.util.Optional;
import java.util.Set;

public interface CircuitDatabaseClient {

    Set<Circuit> getAllCircuits() throws DataServiceException;

    Set<String> getAllCircuitNames() throws DataServiceException;

    Optional<Circuit> getCircuit(String name) throws DataServiceException;

    void addCircuits(Set<Circuit> circuits) throws DataInsertionException;
}
