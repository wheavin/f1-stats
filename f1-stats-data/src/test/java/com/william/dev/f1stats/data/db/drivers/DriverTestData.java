package com.william.dev.f1stats.data.db.drivers;

import com.william.dev.f1stats.data.db.utils.ResultSetStub;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverTestData {

    public static ResultSet allDriversResultSet() {
        final List<Map<String, Object>> driverData = new ArrayList<>();
        driverData.add(createFernandoAlonso());
        driverData.add(createLewisHamilton());
        return new ResultSetStub(driverData);
    }

    public static ResultSet noDriversResultSet() {
        return new ResultSetStub();
    }

    public static ResultSet partialDriverResultSet() {
        final List<Map<String, Object>> driverData = new ArrayList<>();
        driverData.add(createDriverWithPartialData());
        return new ResultSetStub(driverData);
    }

    public static ResultSet defaultDriverResultSet() {
        final List<Map<String, Object>> driverData = new ArrayList<>();
        driverData.add(createDriverWithDefaultData());
        return new ResultSetStub(driverData);
    }

    public static ResultSet emptyDriverResultSet() {
        final List<Map<String, Object>> driverData = new ArrayList<>();
        driverData.add(new HashMap<>());
        return new ResultSetStub(driverData);
    }

    public static ResultSet fernandoAlonso() {
        final List<Map<String, Object>> driverData = new ArrayList<>();
        driverData.add(createFernandoAlonso());
        return new ResultSetStub(driverData);
    }

    private static Map<String, Object> createFernandoAlonso() {
        final Map<String, Object> fernandoAlonso = new HashMap<>();
        fernandoAlonso.put("firstName", "Fernando");
        fernandoAlonso.put("lastName", "Alonso");
        fernandoAlonso.put("nationality", "Spanish");
        fernandoAlonso.put("dateOfBirth", Date.valueOf("1981-07-29"));
        fernandoAlonso.put("wiki", "http://en.wikipedia.org/wiki/Fernando_Alonso");
        return fernandoAlonso;
    }

    private static Map<String, Object> createLewisHamilton() {
        final Map<String, Object> lewisHamilton = new HashMap<>();
        lewisHamilton.put("firstName", "Lewis");
        lewisHamilton.put("lastName", "Hamilton");
        lewisHamilton.put("nationality", "British");
        lewisHamilton.put("dateOfBirth", Date.valueOf("1985-01-07"));
        lewisHamilton.put("wiki", "http://en.wikipedia.org/wiki/Lewis_Hamilton");
        return lewisHamilton;
    }

    private static Map<String, Object> createDriverWithPartialData() {
        final Map<String, Object> driverWithPartialData = new HashMap<>();
        driverWithPartialData.put("firstName", "Lewis");
        driverWithPartialData.put("lastName", "Hamilton");
        driverWithPartialData.put("nationality", "");
        driverWithPartialData.put("dateOfBirth", "");
        driverWithPartialData.put("wiki", "");
        return driverWithPartialData;
    }

    private static Map<String, Object> createDriverWithDefaultData() {
        final Map<String, Object> driverWithDefaultData = new HashMap<>();
        driverWithDefaultData.put("firstName", "");
        driverWithDefaultData.put("lastName", "");
        driverWithDefaultData.put("nationality", "");
        driverWithDefaultData.put("dateOfBirth", "");
        driverWithDefaultData.put("wiki", "");
        return driverWithDefaultData;
    }
}
