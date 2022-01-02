package com.william.dev.f1stats.application;

import com.william.dev.f1stats.data.db.utils.ResultSetStub;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.*;

public class TestData {

    public static ResultSet allCircuitsDataSet() {
        final List<Map<String, Object>> circuitData = new ArrayList<>();
        final Map<String, Object> silverstone = new HashMap<>();
        silverstone.put("name", "Silverstone");
        silverstone.put("country", "United Kingdom");
        silverstone.put("wiki", "http://en.wikipedia.org/wiki/Silverstone_Circuit");
        circuitData.add(silverstone);

        final Map<String, Object> monza = new HashMap<>();
        monza.put("name", "Monza");
        monza.put("country", "Italy");
        monza.put("wiki", "https://en.wikipedia.org/wiki/Monza_Circuit");
        circuitData.add(monza);

        return new ResultSetStub(circuitData);
    }

    public static ResultSet silverstoneDataSet() {
        final List<Map<String, Object>> circuitData = new ArrayList<>();
        final Map<String, Object> silverstone = new HashMap<>();
        silverstone.put("name", "Silverstone");
        silverstone.put("country", "United Kingdom");
        silverstone.put("wiki", "http://en.wikipedia.org/wiki/Silverstone_Circuit");
        circuitData.add(silverstone);
        return new ResultSetStub(circuitData);
    }

    public static ResultSet allDriversDataSet() {
        final List<Map<String, Object>> driverData = new ArrayList<>();
        final Map<String, Object> fernandoAlonso = new HashMap<>();
        fernandoAlonso.put("firstName", "Fernando");
        fernandoAlonso.put("lastName", "Alonso");
        fernandoAlonso.put("nationality", "Spanish");
        fernandoAlonso.put("dateOfBirth", Date.valueOf("1981-07-29"));
        fernandoAlonso.put("wiki", "https://en.wikipedia.org/wiki/Fernando_Alonso");
        driverData.add(fernandoAlonso);
        return new ResultSetStub(driverData);
    }

    public static ResultSet allTeamsDataSet() {
        final List<Map<String, Object>> teamData = new ArrayList<>();
        final Map<String, Object> ferrari = new HashMap<>();
        ferrari.put("name", "Scuderia Ferrari");
        ferrari.put("nationality", "Italy");
        ferrari.put("wiki", "https://en.wikipedia.org/wiki/Scuderia_Ferrari");
        teamData.add(ferrari);
        return new ResultSetStub(teamData);
    }

    static ResultSet emptyDataSet() {
        return new ResultSetStub(Collections.emptyList());
    }
}
