package com.william.dev.f1stats.data.remote;

import com.william.dev.f1.stats.remote.RemoteClient;
import com.william.dev.f1stats.data.db.circuits.CircuitDatabaseClient;
import com.william.dev.f1stats.data.db.drivers.DriverDatabaseClient;
import com.william.dev.f1stats.data.db.teams.TeamDatabaseClient;
import com.william.dev.f1stats.data.db.utils.TestDataReader;
import com.william.dev.f1stats.data.exception.DataInsertionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DataUpdaterBeanTest {

    private static final String TEST_DATA_FOLDER = "src/test/resources/data/";
    private static final String ALL_CIRCUITS_RETURNED_FILE_PATH = TEST_DATA_FOLDER + "all_circuits_data.txt";
    private static final String ALL_DRIVERS_RETURNED_FILE_PATH = TEST_DATA_FOLDER + "all_drivers_data.txt";
    private static final String ALL_TEAMS_RETURNED_FILE_PATH = TEST_DATA_FOLDER + "all_teams_data.txt";

    @Mock
    private RemoteClient mockRemoteClient;

    @Mock
    private CircuitDatabaseClient mockCircuitDatabaseClient;

    @Mock
    private DriverDatabaseClient mockDriverDatabaseClient;

    @Mock
    private TeamDatabaseClient mockTeamDatabaseClient;

    @InjectMocks
    private DataUpdaterBean objUnderTest;

    @Test
    public void successfully_updates_circuits() throws DataInsertionException {
        final String response = TestDataReader.readData(ALL_CIRCUITS_RETURNED_FILE_PATH);
        when(mockRemoteClient.getAllCircuits()).thenReturn(response);
        objUnderTest.updateCircuits();
        verify(mockCircuitDatabaseClient).addCircuits(anySet());
    }

    @Test
    public void cannot_update_circuits_with_invalid_data() throws DataInsertionException {
        when(mockRemoteClient.getAllCircuits()).thenReturn("{\"MRData\":{\"CircuitTable\":{}}}");
        objUnderTest.updateCircuits();
        verify(mockCircuitDatabaseClient, never()).addCircuits(anySet());
    }

    @Test
    public void successfully_updates_drivers() throws DataInsertionException {
        final String response = TestDataReader.readData(ALL_DRIVERS_RETURNED_FILE_PATH);
        when(mockRemoteClient.getAllDrivers()).thenReturn(response);
        objUnderTest.updateDrivers();
        verify(mockDriverDatabaseClient).addDrivers(anySet());
    }

    @Test
    public void cannot_update_drivers_with_invalid_data() throws DataInsertionException {
        when(mockRemoteClient.getAllDrivers()).thenReturn("{\"MRData\":{\"DriverTable\":{}}}");
        objUnderTest.updateDrivers();
        verify(mockDriverDatabaseClient, never()).addDrivers(anySet());
    }

    @Test
    public void successfully_updates_teams() throws DataInsertionException {
        final String response = TestDataReader.readData(ALL_TEAMS_RETURNED_FILE_PATH);
        when(mockRemoteClient.getAllTeams()).thenReturn(response);
        objUnderTest.updateTeams();
        verify(mockTeamDatabaseClient).addTeams(anySet());
    }

    @Test
    public void cannot_update_teams_with_invalid_data() throws DataInsertionException {
        when(mockRemoteClient.getAllTeams()).thenReturn("{\"MRData\":{\"ConstructorTable\":{}}}");
        objUnderTest.updateTeams();
        verify(mockTeamDatabaseClient, never()).addTeams(anySet());
    }
}
