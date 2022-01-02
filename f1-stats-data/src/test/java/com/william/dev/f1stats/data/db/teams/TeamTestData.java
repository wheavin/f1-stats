package com.william.dev.f1stats.data.db.teams;

import com.william.dev.f1stats.data.db.utils.ResultSetStub;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamTestData {

    public static ResultSet allTeamsResultSet() {
        final List<Map<String, Object>> teamData = new ArrayList<>();
        teamData.add(createFerrari());
        teamData.add(createMinardi());
        return new ResultSetStub(teamData);
    }

    public static ResultSet noTeamsResultSet() {
        return new ResultSetStub();
    }

    public static ResultSet partialTeamResultSet() {
        final List<Map<String, Object>> teamData = new ArrayList<>();
        teamData.add(createTeamWithPartialData());
        return new ResultSetStub(teamData);
    }

    public static ResultSet defaultTeamResultSet() {
        final List<Map<String, Object>> teamData = new ArrayList<>();
        teamData.add(createTeamWithDefaultData());
        return new ResultSetStub(teamData);
    }

    public static ResultSet emptyTeamResultSet() {
        final List<Map<String, Object>> teamData = new ArrayList<>();
        teamData.add(new HashMap<>());
        return new ResultSetStub(teamData);
    }

    public static ResultSet ferrari() {
        final List<Map<String, Object>> driverData = new ArrayList<>();
        driverData.add(createFerrari());
        return new ResultSetStub(driverData);
    }

    private static Map<String, Object> createFerrari() {
        final Map<String, Object> ferrari = new HashMap<>();
        ferrari.put("name", "Scuderia Ferrari");
        ferrari.put("nationality", "Italy");
        ferrari.put("wiki", "https://en.wikipedia.org/wiki/Scuderia_Ferrari");
        return ferrari;
    }

    private static Map<String, Object> createMinardi() {
        final Map<String, Object> ferrari = new HashMap<>();
        ferrari.put("name", "Minardi F1 Team");
        ferrari.put("nationality", "Italy");
        ferrari.put("wiki", "https://en.wikipedia.org/wiki/Minardi");
        return ferrari;
    }

    private static Map<String, Object> createTeamWithPartialData() {
        final Map<String, Object> teamWithPartialData = new HashMap<>();
        teamWithPartialData.put("name", "Minardi F1 Team");
        teamWithPartialData.put("nationality", "");
        teamWithPartialData.put("wiki", "");
        return teamWithPartialData;
    }

    private static Map<String, Object> createTeamWithDefaultData() {
        final Map<String, Object> teamWithDefaultData = new HashMap<>();
        teamWithDefaultData.put("name", "");
        teamWithDefaultData.put("nationality", "");
        teamWithDefaultData.put("wiki", "");
        return teamWithDefaultData;
    }
}
