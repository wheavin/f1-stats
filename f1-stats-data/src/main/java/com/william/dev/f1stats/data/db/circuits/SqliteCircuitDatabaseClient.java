package com.william.dev.f1stats.data.db.circuits;

import com.william.dev.f1stats.data.api.Circuit;
import com.william.dev.f1stats.data.db.SqlStatements;
import com.william.dev.f1stats.data.db.SqliteDatabaseClient;
import com.william.dev.f1stats.data.exception.DataInsertionException;
import com.william.dev.f1stats.data.exception.DataServiceException;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
public class SqliteCircuitDatabaseClient extends SqliteDatabaseClient implements CircuitDatabaseClient {

    @Override
    public Set<Circuit> getAllCircuits() throws DataServiceException {
        log.debug("Fetching all circuits from DB");
        final Set<Circuit> allCircuits = new HashSet<>();
        try (Connection connection = getConnection();
             ResultSet resultSet = connection.prepareStatement(SqlStatements.GET_ALL_CIRCUITS_QUERY).executeQuery()) {
            while (resultSet.next()) {
                if (isResultSetValid(resultSet)) {
                    final Circuit circuit = newCircuit(resultSet);
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
        try (Connection connection = getConnection();
             ResultSet resultSet = connection.prepareStatement(SqlStatements.GET_ALL_CIRCUIT_NAMES_QUERY).executeQuery()) {
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
        try (Connection connection = getConnection();
             ResultSet resultSet = prepareStatement(connection, SqlStatements.GET_CIRCUIT_BY_NAME_QUERY, name).executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(newCircuit(resultSet));
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

    private Circuit newCircuit(final ResultSet resultSet) throws SQLException {
        final String circuitName = resultSet.getString(CIRCUIT_DB_NAME_KEY);
        final String country = resultSet.getString(CIRCUIT_DB_COUNTRY_KEY);
        final String wiki = resultSet.getString(DB_WIKI_KEY);
        return new Circuit(circuitName, country, wiki);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addCircuits(final Set<Circuit> circuits) throws DataInsertionException {
        log.debug("Inserting circuits into database: {}", circuits);
        try (Connection connection = getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(SqlStatements.INSERT_CIRCUIT_SQL)) {
            connection.setAutoCommit(false);
            for (final Circuit circuit : circuits) {
                addCircuitToBatch(insertStatement, circuit);
            }
            final int[] rowsUpdated = insertStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
            log.debug("Circuits successfully inserted into database. Number of rows updated = {}", rowsUpdated.length);
        } catch (final SQLException ex) {
            log.error("Error inserting circuits into database", ex);
            throw new DataInsertionException(ex);
        }
    }

    private void addCircuitToBatch(final PreparedStatement insertStatement, final Circuit circuit) throws SQLException {
        log.debug("Inserting circuit: {}", circuit);
        insertStatement.setString(1, circuit.getName());
        insertStatement.setString(2, circuit.getCountry());
        insertStatement.setString(3, circuit.getWiki());
        insertStatement.addBatch();
    }
}
