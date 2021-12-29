package com.william.dev.f1stats.data.db.circuits;

import com.google.common.collect.ImmutableMap;
import com.william.dev.f1stats.data.db.utils.ResultSetStub;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircuitTestData {

    public static ResultSet allCircuitsResultSet() {
        final List<Map<String, Object>> circuitData = new ArrayList<>();
        circuitData.add(createSilverstoneCircuit());
        circuitData.add(createMonzaCircuit());
        return new ResultSetStub(circuitData);
    }

    public static ResultSet noCircuitsResultSet() {
        return new ResultSetStub();
    }

    public static ResultSet silverstoneCircuit() {
        final List<Map<String, Object>> circuitData = new ArrayList<>();
        circuitData.add(createSilverstoneCircuit());
        return new ResultSetStub(circuitData);
    }

    public static ResultSet partialCircuitResultSet() {
        final List<Map<String, Object>> circuitData = new ArrayList<>();
        circuitData.add(createCircuitWithPartialData());
        return new ResultSetStub(circuitData);
    }

    public static ResultSet defaultCircuitResultSet() {
        final List<Map<String, Object>> circuitData = new ArrayList<>();
        circuitData.add(createCircuitWithDefaultData());
        return new ResultSetStub(circuitData);
    }

    public static ResultSet emptyCircuitResultSet() {
        final List<Map<String, Object>> circuitData = new ArrayList<>();
        circuitData.add(new HashMap<>());
        return new ResultSetStub(circuitData);
    }

    public static ResultSet allCircuitNamesResultSet() {
        final List<Map<String, Object>> circuitData = new ArrayList<>();
        circuitData.add(ImmutableMap.of("name", "Silverstone"));
        circuitData.add(ImmutableMap.of("name", "Monza"));
        return new ResultSetStub(circuitData);
    }

    private static Map<String, Object> createSilverstoneCircuit() {
        final Map<String, Object> silverstone = new HashMap<>();
        silverstone.put("name", "Silverstone");
        silverstone.put("country", "United Kingdom");
        silverstone.put("wiki", "http://en.wikipedia.org/wiki/Silverstone_Circuit");
        return silverstone;
    }

    private static Map<String, Object> createMonzaCircuit() {
        final Map<String, Object> monza = new HashMap<>();
        monza.put("name", "Monza");
        monza.put("country", "Italy");
        monza.put("wiki", "http://en.wikipedia.org/wiki/Autodromo_Nazionale_Monza");
        return monza;
    }

    private static Map<String, Object> createCircuitWithPartialData() {
        final Map<String, Object> circuitWithDefaultData = new HashMap<>();
        circuitWithDefaultData.put("name", "Paul Ricard");
        circuitWithDefaultData.put("country", "");
        circuitWithDefaultData.put("wiki", "");
        return circuitWithDefaultData;
    }

    private static Map<String, Object> createCircuitWithDefaultData() {
        final Map<String, Object> circuitWithDefaultData = new HashMap<>();
        circuitWithDefaultData.put("name", "");
        circuitWithDefaultData.put("country", "");
        circuitWithDefaultData.put("wiki", "");
        return circuitWithDefaultData;
    }
}
