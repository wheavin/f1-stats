package com.william.dev.f1stats.data.remote;

import com.william.dev.f1stats.data.api.Circuit;
import com.william.dev.f1stats.data.api.Driver;
import com.william.dev.f1stats.data.db.utils.TestDataReader;
import com.william.dev.f1stats.data.exception.DataParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataParserTest {

    private static final String TEST_DATA_FOLDER = "src/test/resources/data/";
    private static final String ALL_CIRCUITS_RETURNED_FILE_PATH = TEST_DATA_FOLDER + "all_circuits_data.txt";
    private static final String NO_CIRCUITS_RETURNED_FILE_PATH = TEST_DATA_FOLDER + "no_circuits_returned.txt";
    private static final String ALL_DRIVERS_RETURNED_FILE_PATH = TEST_DATA_FOLDER + "all_drivers_data.txt";
    private static final String NO_DRIVERS_RETURNED_FILE_PATH = TEST_DATA_FOLDER + "no_drivers_returned.txt";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private DataParser objUnderTest;

    @BeforeEach
    public void setup() {
        objUnderTest = new DataParser();
    }

    @Test
    public void parses_circuits_successfully() throws DataParseException {
        final String allCircuitData = TestDataReader.readData(ALL_CIRCUITS_RETURNED_FILE_PATH);
        final Set<Circuit> allCircuitsParsed = objUnderTest.parseCircuits(allCircuitData);
        assertThat(allCircuitsParsed).hasSize(76);
        assertThat(allCircuitsParsed)
                .contains(new Circuit("Circuit de Monaco", "Monaco", "http://en.wikipedia.org/wiki/Circuit_de_Monaco"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "{}",
            "{\"MRData\":",
            "{\"MRData\":{}}",
            "{\"MRData\":{\"CircuitTable\":{}}}"
    })
    public void does_not_parse_invalid_circuit_data(final String input) {
        final DataParseException exceptionThrown = assertThrows(
                DataParseException.class, () -> objUnderTest.parseCircuits(input));
        assertThat(exceptionThrown).hasMessageThat().contains("Invalid circuit data");
    }

    @Test
    public void empty_circuit_list_returned() {
        final String allCircuitData = TestDataReader.readData(NO_CIRCUITS_RETURNED_FILE_PATH);
        final DataParseException exceptionThrown = assertThrows(
                DataParseException.class, () -> objUnderTest.parseCircuits(allCircuitData));
        assertThat(exceptionThrown).hasMessageThat().contains("No circuits found");
    }

    @Test
    public void parses_drivers_successfully() throws DataParseException, ParseException {
        final String allDriverData = TestDataReader.readData(ALL_DRIVERS_RETURNED_FILE_PATH);
        final Set<Driver> allDriversParsed = objUnderTest.parseDrivers(allDriverData);
        assertThat(allDriversParsed).hasSize(853);
        assertThat(allDriversParsed).contains(new Driver("Fernando", "Alonso", "Spanish",
                dateFormat.parse("1981-07-29"), "http://en.wikipedia.org/wiki/Fernando_Alonso"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "{}",
            "{\"MRData\":",
            "{\"MRData\":{}}",
            "{\"MRData\":{\"DriverTable\":{}}}"
    })
    public void does_not_parse_invalid_driver_data(final String input) {
        final DataParseException exceptionThrown = assertThrows(
                DataParseException.class, () -> objUnderTest.parseDrivers(input));
        assertThat(exceptionThrown).hasMessageThat().contains("Invalid driver data");
    }

    @Test
    public void empty_driver_list_returned() {
        final String allDriverData = TestDataReader.readData(NO_DRIVERS_RETURNED_FILE_PATH);
        final DataParseException exceptionThrown = assertThrows(
                DataParseException.class, () -> objUnderTest.parseDrivers(allDriverData));
        assertThat(exceptionThrown).hasMessageThat().contains("No drivers found");
    }
}
