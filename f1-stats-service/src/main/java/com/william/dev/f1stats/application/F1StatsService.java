package com.william.dev.f1stats.application;

import javax.ws.rs.core.Response;

public interface F1StatsService {

    Response listAllTeams();

    Response listAllTeamNames();

    Response getTeam(String name);
}
