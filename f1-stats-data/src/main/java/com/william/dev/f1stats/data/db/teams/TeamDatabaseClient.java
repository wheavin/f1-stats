package com.william.dev.f1stats.data.db.teams;

import com.william.dev.f1stats.data.api.Team;
import com.william.dev.f1stats.data.exception.DataInsertionException;
import com.william.dev.f1stats.data.exception.DataServiceException;

import java.util.Optional;
import java.util.Set;

public interface TeamDatabaseClient {

    Set<Team> getAllTeams() throws DataServiceException;

    Set<String> getAllTeamNames() throws DataServiceException;

    Optional<Team> getTeam(String name) throws DataServiceException;

    void addTeams(Set<Team> teams) throws DataInsertionException;
}
