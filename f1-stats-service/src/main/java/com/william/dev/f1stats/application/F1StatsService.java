package com.william.dev.f1stats.application;

import javax.ws.rs.core.Response;

public interface F1StatsService {

    Response listAllCircuits();

    Response listAllCircuitNames();

    Response getCircuit(String name);

    Response listAllDrivers();

    Response getDriver(String firstName, String lastName);

    Response listAllTeams();

    Response listAllTeamNames();

    Response getTeam(String name);
}
