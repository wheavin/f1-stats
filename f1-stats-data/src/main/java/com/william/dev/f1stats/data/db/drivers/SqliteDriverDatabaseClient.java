package com.william.dev.f1stats.data.db.drivers;

import com.william.dev.f1stats.data.api.Driver;
import com.william.dev.f1stats.data.db.ConnectionFactory;
import com.william.dev.f1stats.data.db.SqlStatements;
import com.william.dev.f1stats.data.exception.DataInsertionException;
import com.william.dev.f1stats.data.exception.DataServiceException;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.william.dev.f1stats.common.Constants.DRIVER_DB_DOB_KEY;
import static com.william.dev.f1stats.common.Constants.DRIVER_DB_FIRST_NAME_KEY;
import static com.william.dev.f1stats.common.Constants.DRIVER_DB_LAST_NAME_KEY;
import static com.william.dev.f1stats.common.Constants.DRIVER_DB_NATIONALITY_KEY;
import static com.william.dev.f1stats.common.Constants.DRIVER_DB_WIKI_KEY;
import static com.william.dev.f1stats.common.StringUtils.isNullOrEmpty;

@Slf4j
public class SqliteDriverDatabaseClient implements DriverDatabaseClient {

    @Inject
    private ConnectionFactory connectionFactory;

    @Override
    public Set<Driver> getAllDrivers() throws DataServiceException {
        log.debug("Fetching all drivers from DB");
        final Set<Driver> allDrivers = new HashSet<>();
        try (ResultSet resultSet = executeQuery(SqlStatements.GET_ALL_DRIVERS_QUERY)) {
            while (resultSet.next()) {
                if (isResultSetValid(resultSet)) {
                    final Driver driver = toDriver(resultSet);
                    allDrivers.add(driver);
                }
            }
        } catch (SQLException ex) {
            throw new DataServiceException("Error fetching all drivers from database", ex);
        }
        log.debug("Found drivers: {}", allDrivers);
        return allDrivers;
    }

    private boolean isResultSetValid(final ResultSet resultSet) throws SQLException {
        return !isNullOrEmpty(resultSet.getString(DRIVER_DB_FIRST_NAME_KEY)) &&
                !isNullOrEmpty(resultSet.getString(DRIVER_DB_LAST_NAME_KEY)) &&
                !isNullOrEmpty(resultSet.getString(DRIVER_DB_NATIONALITY_KEY)) &&
                !isNullOrEmpty(resultSet.getString(DRIVER_DB_WIKI_KEY));
    }

    private Driver toDriver(final ResultSet resultSet) throws SQLException {
        final String firstName = resultSet.getString(DRIVER_DB_FIRST_NAME_KEY);
        final String lastName = resultSet.getString(DRIVER_DB_LAST_NAME_KEY);
        final String nationality = resultSet.getString(DRIVER_DB_NATIONALITY_KEY);
        final java.util.Date dateOfBirth = resultSet.getDate(DRIVER_DB_DOB_KEY);
        final String wiki = resultSet.getString(DRIVER_DB_WIKI_KEY);
        return new Driver(firstName, lastName, nationality, dateOfBirth, wiki);
    }

    @Override
    public Optional<Driver> getDriver(String firstName, String lastName) throws DataServiceException {
        try (ResultSet resultSet = executeQuery(SqlStatements.GET_DRIVER_BY_NAME_QUERY, firstName, lastName)) {
            if (resultSet.next()) {
                return Optional.of(toDriver(resultSet));
            }
        } catch (final SQLException ex) {
            final String errorMessage = String.format("Error fetching driver '%s %s' from database", firstName, lastName);
            throw new DataServiceException(errorMessage, ex);
        }
        return Optional.empty();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addDrivers(Set<Driver> drivers) throws DataInsertionException {
        log.debug("Inserting drivers into database: {}", drivers);
        try (PreparedStatement insertStatement = createPreparedStatement(SqlStatements.INSERT_DRIVER_SQL)) {
            for (final Driver driver : drivers) {
                insertDriver(insertStatement, driver);
            }
            log.debug("All drivers successfully inserted into database");
        } catch (final SQLException ex) {
            log.error("Error inserting circuits", ex);
            throw new DataInsertionException(ex);
        }
    }

    private void insertDriver(final PreparedStatement insertStatement, final Driver driver) throws SQLException {
        log.debug("Inserting driver: {}", driver);
        insertStatement.setString(1, driver.getFirstName());
        insertStatement.setString(2, driver.getLastName());
        insertStatement.setString(3, driver.getNationality());
        insertStatement.setDate(4, new Date(driver.getDateOfBirth().getTime()));
        insertStatement.setString(5, driver.getWiki());
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
