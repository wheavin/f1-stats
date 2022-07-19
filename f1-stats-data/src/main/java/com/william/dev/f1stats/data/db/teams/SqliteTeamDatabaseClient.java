package com.william.dev.f1stats.data.db.teams;

import com.william.dev.f1stats.data.api.Team;
import com.william.dev.f1stats.data.db.SqlStatements;
import com.william.dev.f1stats.data.db.SqliteDatabaseClient;
import com.william.dev.f1stats.data.exception.DataInsertionException;
import com.william.dev.f1stats.data.exception.DataServiceException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.william.dev.f1stats.common.Constants.DB_WIKI_KEY;
import static com.william.dev.f1stats.common.Constants.TEAM_DB_NAME_KEY;
import static com.william.dev.f1stats.common.Constants.TEAM_DB_NATIONALITY_KEY;
import static com.william.dev.f1stats.common.StringUtils.isNullOrEmpty;

@Slf4j
public class SqliteTeamDatabaseClient extends SqliteDatabaseClient implements TeamDatabaseClient {

    @Override
    public Set<Team> getAllTeams() throws DataServiceException {
        log.debug("Fetching all teams from DB");
        final Set<Team> allTeams = new HashSet<>();
        try (Connection connection = getConnection();
             ResultSet resultSet = connection.prepareStatement(SqlStatements.GET_ALL_TEAMS_QUERY).executeQuery()) {
            while (resultSet.next()) {
                if (isResultSetValid(resultSet)) {
                    final Team team = newTeam(resultSet);
                    allTeams.add(team);
                }
            }
        } catch (final SQLException ex) {
            throw new DataServiceException("Error fetching all teams from database", ex);
        }
        log.debug("Found items: {}", allTeams);
        return allTeams;
    }

    private boolean isResultSetValid(final ResultSet resultSet) throws SQLException {
        return !isNullOrEmpty(resultSet.getString(TEAM_DB_NAME_KEY)) &&
                !isNullOrEmpty(resultSet.getString(TEAM_DB_NATIONALITY_KEY)) &&
                !isNullOrEmpty(resultSet.getString(DB_WIKI_KEY));
    }

    private Team newTeam(final ResultSet resultSet) throws SQLException {
        return Team.builder()
                .name(resultSet.getString(TEAM_DB_NAME_KEY))
                .nationality(resultSet.getString(TEAM_DB_NATIONALITY_KEY))
                .wiki(resultSet.getString(DB_WIKI_KEY))
                .build();
    }

    @Override
    public Set<String> getAllTeamNames() throws DataServiceException {
        log.debug("Fetching all team names from DB");
        final Set<String> allTeamNames = new HashSet<>();
        try (Connection connection = getConnection();
             ResultSet resultSet = connection.prepareStatement(SqlStatements.GET_ALL_TEAM_NAMES_QUERY).executeQuery()) {
            while (resultSet.next()) {
                allTeamNames.add(resultSet.getString(TEAM_DB_NAME_KEY));
            }
        } catch (final SQLException ex) {
            throw new DataServiceException("Error fetching all team names from database", ex);
        }
        log.debug("Found team names: {}", allTeamNames);
        return allTeamNames;
    }

    @Override
    public Optional<Team> getTeam(final String name) throws DataServiceException {
        try (Connection connection = getConnection();
             ResultSet resultSet = prepareStatement(connection, SqlStatements.GET_TEAM_BY_NAME_QUERY, name).executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(newTeam(resultSet));
            }
            return Optional.empty();
        } catch (final SQLException ex) {
            final String errorMessage = String.format("Error fetching team '%s' from database", name);
            throw new DataServiceException(errorMessage, ex);
        }
    }

    @Override
    public void addTeams(final Set<Team> teams) throws DataInsertionException {
        log.debug("Inserting teams into database: {}", teams);
        try (Connection connection = getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(SqlStatements.INSERT_TEAM_SQL)) {
            connection.setAutoCommit(false);
            for (final Team team : teams) {
                addTeamToBatch(insertStatement, team);
            }
            final int[] rowsUpdated = insertStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
            log.debug("Teams successfully inserted into database. Number of rows updated = {}", rowsUpdated.length);
        } catch (final SQLException ex) {
            log.error("Error inserting teams into database", ex);
            throw new DataInsertionException(ex);
        }
    }

    private void addTeamToBatch(final PreparedStatement insertStatement, final Team team) throws SQLException {
        log.debug("Inserting team: {}", team);
        insertStatement.setString(1, team.getName());
        insertStatement.setString(2, team.getNationality());
        insertStatement.setString(3, team.getWiki());
        insertStatement.addBatch();
    }

}
