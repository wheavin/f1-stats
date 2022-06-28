package com.william.dev.f1stats.application.rest;

import com.william.dev.f1stats.application.TestData;
import com.william.dev.f1stats.application.dto.DriverDto;
import com.william.dev.f1stats.application.dto.DriversDto;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

import static com.google.common.truth.Truth.assertThat;
import static com.william.dev.f1stats.application.rest.assertions.ResponseAssertions.assertThatResponse;
import static org.mockito.Mockito.when;

public class DriverResourceTest extends ResourceTest {

    @Inject
    private DriverResource objUnderTest;

    @Test
    public void lists_all_drivers_returns_list_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.allDriversDataSet());

        final Response response = objUnderTest.listAllDrivers();
        assertThatResponse(response).isOk();

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
        assertThatResponse(response).isOk();

        final DriversDto driversDtoReturned = (DriversDto) response.getEntity();
        assertThat(driversDtoReturned.getDrivers()).isEmpty();
    }

    @Test
    public void lists_all_drivers_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("some error"));
        final Response response = objUnderTest.listAllDrivers();
        assertThatResponse(response).isInternalServerError();
    }

    @Test
    public void get_specified_driver_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.allDriversDataSet());

        final Response response = objUnderTest.getDriver("Fernando", "Alonso");
        assertThatResponse(response).isOk();

        final DriverDto driverReturned = (DriverDto) response.getEntity();
        assertThat(driverReturned.getFirstName()).isEqualTo("Fernando");
        assertThat(driverReturned.getLastName()).isEqualTo("Alonso");
        assertThat(driverReturned.getNationality()).isEqualTo("Spanish");
    }

    @Test
    public void get_specified_driver_not_found() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.emptyDataSet());
        final Response response = objUnderTest.getDriver("Fernando", "Alonso");
        assertThatResponse(response).isBadRequest();
        assertThatResponse(response).hasMessage("Driver 'Fernando Alonso' not found");
    }

    @Test
    public void get_specified_driver_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("some error"));
        final Response response = objUnderTest.getDriver("Fernando", "Alonso");
        assertThatResponse(response).isInternalServerError();
    }

}
