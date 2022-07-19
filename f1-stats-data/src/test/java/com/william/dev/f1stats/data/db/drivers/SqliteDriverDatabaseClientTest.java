package com.william.dev.f1stats.data.db.drivers;

import com.william.dev.f1stats.data.api.Driver;
import com.william.dev.f1stats.data.db.DatabaseClientTestBase;
import com.william.dev.f1stats.data.exception.DataInsertionException;
import com.william.dev.f1stats.data.exception.DataServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static com.william.dev.f1stats.data.db.drivers.DriverTestData.allDriversResultSet;
import static com.william.dev.f1stats.data.db.drivers.DriverTestData.defaultDriverResultSet;
import static com.william.dev.f1stats.data.db.drivers.DriverTestData.emptyDriverResultSet;
import static com.william.dev.f1stats.data.db.drivers.DriverTestData.fernandoAlonso;
import static com.william.dev.f1stats.data.db.drivers.DriverTestData.noDriversResultSet;
import static com.william.dev.f1stats.data.db.drivers.DriverTestData.partialDriverResultSet;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SqliteDriverDatabaseClientTest extends DatabaseClientTestBase {

    @InjectMocks
    private SqliteDriverDatabaseClient objUnderTest;

    @Test
    public void gets_all_drivers_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(allDriversResultSet());
        final Set<Driver> drivers = objUnderTest.getAllDrivers();
        assertThat(drivers).hasSize(2);
    }

    @Test
    public void gets_empty_list_of_drivers() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(noDriversResultSet());
        final Set<Driver> drivers = objUnderTest.getAllDrivers();
        assertThat(drivers).isEmpty();
    }

    @Test
    public void gets_all_circuits_with_exception() throws Exception {
        mockSqlExceptionOnQuery();
        final Exception exceptionThrown = assertThrows(DataServiceException.class, () -> objUnderTest.getAllDrivers());
        assertThat(exceptionThrown.getMessage()).isEqualTo("Error fetching all drivers from database");
    }

    @ParameterizedTest
    @MethodSource
    public void gets_all_drivers_invalid_data_returns_empty_list(final ResultSet queryResult) throws Exception {
        when(mockStatement.executeQuery()).thenReturn(queryResult);
        final Set<Driver> drivers = objUnderTest.getAllDrivers();
        assertThat(drivers).isEmpty();
    }

    private static Stream<ResultSet> gets_all_drivers_invalid_data_returns_empty_list() {
        return Stream.of(partialDriverResultSet(), defaultDriverResultSet(), emptyDriverResultSet());
    }

    @Test
    public void gets_specified_driver() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(fernandoAlonso());
        final Optional<Driver> driver = objUnderTest.getDriver("Fernando", "Alonso");
        assertThat(driver.isPresent()).isTrue();
        final Driver driverReturned = driver.get();
        assertThat(driverReturned.getFirstName()).isEqualTo("Fernando");
        assertThat(driverReturned.getLastName()).isEqualTo("Alonso");
    }

    @Test
    public void does_not_find_specified_driver() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(noDriversResultSet());
        final Optional<Driver> driver = objUnderTest.getDriver("Fernando", "Alonso");
        assertThat(driver.isPresent()).isFalse();
    }

    @Test
    public void get_specified_driver_with_exception() throws Exception {
        mockSqlExceptionOnQuery();
        final Exception exceptionThrown = assertThrows(DataServiceException.class, () -> objUnderTest.getDriver("Fernando", "Alonso"));
        assertThat(exceptionThrown.getMessage()).isEqualTo("Error fetching driver 'Fernando Alonso' from database");
    }

    @Test
    public void adds_drivers_successfully() throws Exception {
        final Set<Driver> driversToAdd = createDrivers();
        objUnderTest.addDrivers(driversToAdd);
        assertExecuteBatch(2);
    }

    @Test
    public void add_empty_drivers_does_no_inserts() throws Exception {
        objUnderTest.addDrivers(Collections.emptySet());
        verify(mockStatement, never()).execute();
    }

    @Test
    public void add_drivers_throws_exception() throws Exception {
        mockSqlExceptionOnAdd();
        final Set<Driver> driversToAdd = createDrivers();
        final Exception exceptionThrown = assertThrows(
                DataInsertionException.class, () -> objUnderTest.addDrivers(driversToAdd));
        assertThat(exceptionThrown).hasMessageThat().contains("Some error occurred");
    }

    private Set<Driver> createDrivers() {
        final Set<Driver> drivers = new HashSet<>();
        drivers.add(new Driver("Fernando", "Alonso", "Spanish", new Date(), "wiki"));
        drivers.add(new Driver("Lewis", "Hamilton", "British", new Date(), "wiki"));
        return drivers;
    }
}
