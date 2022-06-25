package com.william.dev.f1stats.data.db.teams;

import com.william.dev.f1stats.data.api.Team;
import com.william.dev.f1stats.data.db.ConnectionFactory;
import com.william.dev.f1stats.data.db.SqlStatements;
import com.william.dev.f1stats.data.exception.DataInsertionException;
import com.william.dev.f1stats.data.exception.DataServiceException;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.william.dev.f1stats.common.Constants.DB_WIKI_KEY;
import static com.william.dev.f1stats.common.Constants.TEAM_DB_NATIONALITY_KEY;
import static com.william.dev.f1stats.common.Constants.TEAM_DB_NAME_KEY;
import static com.william.dev.f1stats.common.StringUtils.isNullOrEmpty;

@Slf4j
public class SqliteTeamDatabaseClient implements TeamDatabaseClient {

    @Inject
    private ConnectionFactory connectionFactory;

    @Override
    public Set<Team> getAllTeams() throws DataServiceException {
        log.debug("Fetching all teams from DB");
        final Set<Team> allTeams = new HashSet<>();
        try (ResultSet resultSet = executeQuery(SqlStatements.GET_ALL_TEAMS_QUERY)) {
            while (resultSet.next()) {
                if (isResultSetValid(resultSet)) {
                    final Team team = toTeam(resultSet);
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

    private Team toTeam(final ResultSet resultSet) throws SQLException {
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
        try (ResultSet resultSet = executeQuery(SqlStatements.GET_ALL_TEAM_NAMES_QUERY)) {
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
        try (ResultSet resultSet = executeQuery(SqlStatements.GET_TEAM_BY_NAME_QUERY, name)) {
            if (resultSet.next()) {
                return Optional.of(toTeam(resultSet));
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
        try (PreparedStatement insertStatement = createPreparedStatement(SqlStatements.INSERT_TEAM_SQL)) {
            for (final Team team : teams) {
                insertTeam(insertStatement, team);
            }
            log.debug("All teams successfully inserted into database");
        } catch (final SQLException ex) {
            log.error("Error inserting teams into database", ex);
            throw new DataInsertionException(ex);
        }
    }

    private void insertTeam(final PreparedStatement insertStatement, final Team team) throws SQLException {
        log.debug("Inserting team: {}", team);
        insertStatement.setString(1, team.getName());
        insertStatement.setString(2, team.getNationality());
        insertStatement.setString(3, team.getWiki());
        insertStatement.execute();
    }

    private ResultSet executeQuery(final String sqlQuery) throws SQLException {
        final PreparedStatement statement = createPreparedStatement(sqlQuery);
        return statement.executeQuery();
    }

    private ResultSet executeQuery(final String sqlQuery, final String... params) throws SQLException {
        final PreparedStatement statement = createPreparedStatement(sqlQuery);
        for (int paramIndex = 1; paramIndex <= params.length; paramIndex++) {
            statement.setString(paramIndex, params[paramIndex - 1]);
        }
        return statement.executeQuery();
    }

    private PreparedStatement createPreparedStatement(final String sqlQuery) throws SQLException {
        final Connection connection = connectionFactory.getConnection();
        return connection.prepareStatement(sqlQuery);
    }
}
