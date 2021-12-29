package com.william.dev.f1stats.application;

import com.william.dev.f1stats.application.dto.CircuitDto;
import com.william.dev.f1stats.application.dto.CircuitsDto;
import com.william.dev.f1stats.application.dto.DriverDto;
import com.william.dev.f1stats.application.dto.DriversDto;
import com.william.dev.f1stats.data.db.SqliteConnectionFactory;
import de.hilling.junit.cdi.CdiTestJunitExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(CdiTestJunitExtension.class)
@ExtendWith(MockitoExtension.class)
public class F1StatsServiceTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private SqliteConnectionFactory mockConnectionFactory;

    @Inject
    private F1StatsServiceResource objUnderTest;

    @BeforeEach
    public void setup() throws Exception {
        lenient().doReturn(mockStatement).when(mockConnection).prepareStatement(anyString());
        lenient().doReturn(mockConnection).when(mockConnectionFactory).getConnection();
    }

    @Test
    public void list_all_circuits_returns_list_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.allCircuitsDataSet());

        final Response response = objUnderTest.listAllCircuits();
        final CircuitsDto circuitsReturned = (CircuitsDto) response.getEntity();
        assertThat(circuitsReturned.getCircuits()).hasSize(2);

        final CircuitDto circuitReturned = circuitsReturned.getCircuits().iterator().next();
        assertThat(circuitReturned.getName()).isEqualTo("Silverstone");
        assertThat(circuitReturned.getCountry()).isEqualTo("United Kingdom");
        assertThat(circuitReturned.getWiki()).isEqualTo("http://en.wikipedia.org/wiki/Silverstone_Circuit");
    }

    @Test
    public void list_all_circuits_returns_empty_list() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.emptyDataSet());

        final Response response = objUnderTest.listAllCircuits();
        final CircuitsDto circuitsDtoReturned = (CircuitsDto) response.getEntity();
        assertThat(circuitsDtoReturned.getCircuits()).isEmpty();
    }

    @Test
    public void list_all_circuits_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("Error when executing query"));
        final Response response = objUnderTest.listAllCircuits();
        assertInternalServerError(response);
    }

    @Test
    public void list_all_circuit_names_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.allCircuitsDataSet());

        final Response response = objUnderTest.listAllCircuitNames();
        assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
        final Set<String> circuitNames = (Set<String>) response.getEntity();
        assertThat(circuitNames).containsExactly("Monza", "Silverstone");
    }

    @Test
    public void list_all_circuit_names_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("some error"));
        final Response response = objUnderTest.listAllCircuitNames();
        assertInternalServerError(response);
    }

    @Test
    public void get_specified_circuit_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.silverstoneDataSet());

        final Response response = objUnderTest.getCircuit("Silverstone");
        assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());

        final CircuitDto circuitReturned = (CircuitDto) response.getEntity();
        assertThat(circuitReturned.getName()).isEqualTo("Silverstone");
        assertThat(circuitReturned.getCountry()).isEqualTo("United Kingdom");
        assertThat(circuitReturned.getWiki()).isEqualTo("http://en.wikipedia.org/wiki/Silverstone_Circuit");
    }

    @Test
    public void specified_circuit_not_found() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.emptyDataSet());
        final Response response = objUnderTest.getCircuit("Silverstone");
        assertBadRequest(response, "Circuit 'Silverstone' not found");
    }

    @Test
    public void get_specified_circuit_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("some error"));
        final Response response = objUnderTest.getCircuit("Silverstone");
        assertInternalServerError(response);
    }

    @Test
    public void lists_all_drivers_returns_list_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.allDriversDataSet());

        final Response response = objUnderTest.listAllDrivers();
        final DriversDto driversReturned = (DriversDto) response.getEntity();
        assertThat(driversReturned.getDrivers()).hasSize(1);

        final DriverDto driverReturned = driversReturned.getDrivers().iterator().next();
        assertThat(driverReturned.getFirstName()).isEqualTo("Fernando");
        assertThat(driverReturned.getLastName()).isEqualTo("Alonso");
        assertThat(driverReturned.getNationality()).isEqualTo("Spanish");
    }

    @Test
    public void lists_all_drivers_returns_empty_list() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.emptyDataSet());

        final Response response = objUnderTest.listAllDrivers();
        final DriversDto driversDtoReturned = (DriversDto) response.getEntity();
        assertThat(driversDtoReturned.getDrivers()).isEmpty();
    }

    @Test
    public void lists_all_drivers_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("some error"));
        final Response response = objUnderTest.listAllDrivers();
        assertInternalServerError(response);
    }

    @Test
    public void get_specified_driver_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.allDriversDataSet());

        final Response response = objUnderTest.getDriver("Fernando", "Alonso");
        final DriverDto driverReturned = (DriverDto) response.getEntity();
        assertThat(driverReturned.getFirstName()).isEqualTo("Fernando");
        assertThat(driverReturned.getLastName()).isEqualTo("Alonso");
        assertThat(driverReturned.getNationality()).isEqualTo("Spanish");
    }

    @Test
    public void get_specified_driver_not_found() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.emptyDataSet());
        final Response response = objUnderTest.getDriver("Fernando", "Alonso");
        assertBadRequest(response, "Driver 'Fernando Alonso' not found");
    }

    @Test
    public void get_specified_driver_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("some error"));
        final Response response = objUnderTest.getDriver("Fernando", "Alonso");
        assertInternalServerError(response);
    }

    private static void assertInternalServerError(final Response response) {
        assertThat(response.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR.getStatusCode());
        final String message = (String) response.getEntity();
        assertThat(message).isEqualTo("Internal Server Error");
    }

    private static void assertBadRequest(final Response response, final String expectedMessage) {
        assertThat(response.getStatus()).isEqualTo(Status.BAD_REQUEST.getStatusCode());
        final String responseMessage = (String) response.getEntity();
        assertThat(responseMessage).isEqualTo(expectedMessage);
    }

}
