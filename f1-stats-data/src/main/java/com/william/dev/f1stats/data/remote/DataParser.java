package com.william.dev.f1stats.data.remote;

import com.william.dev.f1stats.data.api.Circuit;
import com.william.dev.f1stats.data.api.Driver;
import com.william.dev.f1stats.data.exception.DataParseException;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.william.dev.f1stats.common.Constants.CIRCUIT_JSON_CIRCUITS_KEY;
import static com.william.dev.f1stats.common.Constants.CIRCUIT_JSON_COUNTRY_KEY;
import static com.william.dev.f1stats.common.Constants.CIRCUIT_JSON_LOCATION_KEY;
import static com.william.dev.f1stats.common.Constants.CIRCUIT_JSON_NAME_KEY;
import static com.william.dev.f1stats.common.Constants.CIRCUIT_JSON_TABLE_KEY;
import static com.william.dev.f1stats.common.Constants.DRIVER_JSON_DOB_KEY;
import static com.william.dev.f1stats.common.Constants.DRIVER_JSON_DRIVERS_KEY;
import static com.william.dev.f1stats.common.Constants.DRIVER_JSON_FIRST_NAME_KEY;
import static com.william.dev.f1stats.common.Constants.DRIVER_JSON_LAST_NAME_KEY;
import static com.william.dev.f1stats.common.Constants.DRIVER_JSON_NATIONALITY_KEY;
import static com.william.dev.f1stats.common.Constants.DRIVER_JSON_TABLE_KEY;
import static com.william.dev.f1stats.common.Constants.JSON_WIKI_KEY;
import static com.william.dev.f1stats.common.Constants.MR_DATA_KEY;

@Slf4j
public class DataParser {

    private static final String INVALID_CIRCUIT_DATA = "Invalid circuit data";
    private static final String NO_CIRCUITS_FOUND = "No circuits found";
    private static final String INVALID_DRIVER_DATA = "Invalid driver data";
    private static final String NO_DRIVERS_FOUND = "No drivers found";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public Set<Circuit> parseCircuits(final String circuitData) throws DataParseException {
        if (circuitData == null || circuitData.trim().isEmpty()) {
            throw new DataParseException(INVALID_CIRCUIT_DATA);
        }
        final JSONArray circuitsJson = Optional.ofNullable(parseJsonObject(circuitData))
                .map(jsonObject -> (JSONObject) jsonObject.get(MR_DATA_KEY))
                .map(mrDataJson -> (JSONObject) mrDataJson.get(CIRCUIT_JSON_TABLE_KEY))
                .map(circuitTableJson -> (JSONArray) circuitTableJson.get(CIRCUIT_JSON_CIRCUITS_KEY))
                .orElseThrow(() -> new DataParseException(INVALID_CIRCUIT_DATA));

        if (circuitsJson.isEmpty()) {
            throw new DataParseException(NO_CIRCUITS_FOUND);
        }
        final Set<Circuit> circuitsParsed = new HashSet<>();
        for (final Object circuitJson : circuitsJson) {
            final Circuit circuit = toCircuit((JSONObject) circuitJson);
            circuitsParsed.add(circuit);
        }
        return circuitsParsed;
    }

    private Circuit toCircuit(final JSONObject jsonObject) {
        final String circuitName = (String) jsonObject.get(CIRCUIT_JSON_NAME_KEY);
        final String wiki = (String) jsonObject.get(JSON_WIKI_KEY);
        final JSONObject locationJson = (JSONObject) jsonObject.get(CIRCUIT_JSON_LOCATION_KEY);
        final String country = (String) locationJson.get(CIRCUIT_JSON_COUNTRY_KEY);
        return new Circuit(circuitName, country, wiki);
    }

    public Set<Driver> parseDrivers(final String driverData) throws DataParseException {
        if (driverData == null || driverData.trim().isEmpty()) {
            throw new DataParseException(INVALID_DRIVER_DATA);
        }
        final JSONArray driversJson = Optional.ofNullable(parseJsonObject(driverData))
                .map(jsonObject -> (JSONObject) jsonObject.get(MR_DATA_KEY))
                .map(mrDataJson -> (JSONObject) mrDataJson.get(DRIVER_JSON_TABLE_KEY))
                .map(driverTableJson -> (JSONArray) driverTableJson.get(DRIVER_JSON_DRIVERS_KEY))
                .orElseThrow(() -> new DataParseException(INVALID_DRIVER_DATA));
        if (driversJson.isEmpty()) {
            throw new DataParseException(NO_DRIVERS_FOUND);
        }

        final Set<Driver> driversParsed = new HashSet<>();
        for (final Object driverJson : driversJson) {
            try {
                final Driver driver = toDriver((JSONObject) driverJson);
                driversParsed.add(driver);
            } catch (final java.text.ParseException ex) {
                log.error("Error occurred creating driver instance", ex);
            }
        }
        return driversParsed;
    }

    private JSONObject parseJsonObject(final String rawJsonData) {
        final JSONParser jsonParser = new JSONParser();
        try {
            return (JSONObject) jsonParser.parse(rawJsonData);
        } catch (final ParseException ex) {
            log.error("Error occurred parsing raw json data", ex);
        }
        return null;
    }

    public synchronized Driver toDriver(final JSONObject jsonObject) throws java.text.ParseException {
        final String firstName = (String) jsonObject.get(DRIVER_JSON_FIRST_NAME_KEY);
        final String lastName = (String) jsonObject.get(DRIVER_JSON_LAST_NAME_KEY);
        final String nationality = (String) jsonObject.get(DRIVER_JSON_NATIONALITY_KEY);
        final Date dateOfBirth = DATE_FORMAT.parse((String) jsonObject.get(DRIVER_JSON_DOB_KEY));
        final String wiki = (String) jsonObject.get(JSON_WIKI_KEY);
        return new Driver(firstName, lastName, nationality, dateOfBirth, wiki);
    }
}
