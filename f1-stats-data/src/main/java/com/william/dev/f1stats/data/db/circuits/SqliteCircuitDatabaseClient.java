package com.william.dev.f1stats.data.db.circuits;

import com.william.dev.f1stats.data.api.Circuit;
import com.william.dev.f1stats.data.db.ConnectionFactory;
import com.william.dev.f1stats.data.db.SqlStatements;
import com.william.dev.f1stats.data.exception.DataInsertionException;
import com.william.dev.f1stats.data.exception.DataServiceException;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.william.dev.f1stats.common.Constants.CIRCUIT_DB_COUNTRY_KEY;
import static com.william.dev.f1stats.common.Constants.CIRCUIT_DB_NAME_KEY;
import static com.william.dev.f1stats.common.Constants.DB_WIKI_KEY;
import static com.william.dev.f1stats.common.StringUtils.isNullOrEmpty;

@Slf4j
public class SqliteCircuitDatabaseClient implements CircuitDatabaseClient {
    @Inject
    private ConnectionFactory connectionFactory;

    @Override
    public Set<Circuit> getAllCircuits() throws DataServiceException {
        log.debug("Fetching all circuits from DB");
        final Set<Circuit> allCircuits = new HashSet<>();
        try (ResultSet resultSet = executeQuery(SqlStatements.GET_ALL_CIRCUITS_QUERY)) {
            while (resultSet.next()) {
                if (isResultSetValid(resultSet)) {
                    final Circuit circuit = toCircuit(resultSet);
                    allCircuits.add(circuit);
                }
            }
        } catch (final SQLException ex) {
            throw new DataServiceException("Error fetching all circuits from database", ex);
        }
        log.debug("Found circuits: {}", allCircuits);
        return allCircuits;
    }

    @Override
    public Set<String> getAllCircuitNames() throws DataServiceException {
        log.debug("Fetching all circuit names from DB");
        final Set<String> allCircuitNames = new HashSet<>();
        try (ResultSet resultSet = executeQuery(SqlStatements.GET_ALL_CIRCUIT_NAMES_QUERY)) {
            while (resultSet.next()) {
                allCircuitNames.add(resultSet.getString(CIRCUIT_DB_NAME_KEY));
            }
        } catch (final SQLException ex) {
            throw new DataServiceException("Error fetching all circuit names from database", ex);
        }
        log.debug("Found circuit names: {}", allCircuitNames);
        return allCircuitNames;
    }

    @Override
    public Optional<Circuit> getCircuit(final String name) throws DataServiceException {
        try (ResultSet resultSet = executeQuery(SqlStatements.GET_CIRCUIT_BY_NAME_QUERY, name)) {
            if (resultSet.next()) {
                return Optional.of(toCircuit(resultSet));
            }
        } catch (final SQLException ex) {
            final String errorMessage = String.format("Error fetching circuit '%s' from database", name);
            throw new DataServiceException(errorMessage, ex);
        }
        return Optional.empty();
    }

    private boolean isResultSetValid(final ResultSet resultSet) throws SQLException {
        return !isNullOrEmpty(resultSet.getString(CIRCUIT_DB_NAME_KEY)) &&
                !isNullOrEmpty(resultSet.getString(CIRCUIT_DB_COUNTRY_KEY)) &&
                !isNullOrEmpty(resultSet.getString(DB_WIKI_KEY));
    }

    private Circuit toCircuit(final ResultSet resultSet) throws SQLException {
        final String circuitName = resultSet.getString(CIRCUIT_DB_NAME_KEY);
        final String country = resultSet.getString(CIRCUIT_DB_COUNTRY_KEY);
        final String wiki = resultSet.getString(DB_WIKI_KEY);
        return new Circuit(circuitName, country, wiki);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addCircuits(final Set<Circuit> circuits) throws DataInsertionException {
        log.debug("Inserting circuits into database: {}", circuits);
        try (PreparedStatement insertStatement = createPreparedStatement(SqlStatements.INSERT_CIRCUIT_SQL)) {
            for (final Circuit circuit : circuits) {
                insertCircuit(insertStatement, circuit);
            }
            log.debug("All circuits successfully inserted into database");
        } catch (final SQLException ex) {
            log.error("Error inserting circuits into database", ex);
            throw new DataInsertionException(ex);
        }
    }

    private void insertCircuit(final PreparedStatement insertStatement, final Circuit circuit) throws SQLException {
        log.debug("Inserting circuit: {}", circuit);
        insertStatement.setString(1, circuit.getName());
        insertStatement.setString(2, circuit.getCountry());
        insertStatement.setString(3, circuit.getWiki());
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
