package com.william.dev.f1stats.data.db.teams;

import com.google.common.collect.ImmutableSet;
import com.william.dev.f1stats.data.api.Team;
import com.william.dev.f1stats.data.db.DatabaseClientTestBase;
import com.william.dev.f1stats.data.exception.DataInsertionException;
import com.william.dev.f1stats.data.exception.DataServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static com.william.dev.f1stats.data.db.teams.TeamTestData.allTeamNamesResultSet;
import static com.william.dev.f1stats.data.db.teams.TeamTestData.allTeamsResultSet;
import static com.william.dev.f1stats.data.db.teams.TeamTestData.defaultTeamResultSet;
import static com.william.dev.f1stats.data.db.teams.TeamTestData.emptyTeamResultSet;
import static com.william.dev.f1stats.data.db.teams.TeamTestData.ferrari;
import static com.william.dev.f1stats.data.db.teams.TeamTestData.noTeamsResultSet;
import static com.william.dev.f1stats.data.db.teams.TeamTestData.partialTeamResultSet;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SqliteTeamDatabaseClientTest extends DatabaseClientTestBase {

    @InjectMocks
    private SqliteTeamDatabaseClient objUnderTest;

    @Test
    public void gets_all_teams_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(allTeamsResultSet());
        final Set<Team> teams = objUnderTest.getAllTeams();
        assertThat(teams).hasSize(2);
    }

    @Test
    public void gets_empty_list_of_teams() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(noTeamsResultSet());
        final Set<Team> teams = objUnderTest.getAllTeams();
        assertThat(teams).isEmpty();
    }

    @Test
    public void gets_all_teams_with_exception() throws Exception {
        mockSqlExceptionOnQuery();
        final Exception exceptionThrown = assertThrows(DataServiceException.class, () -> objUnderTest.getAllTeams());
        assertThat(exceptionThrown.getMessage()).isEqualTo("Error fetching all teams from database");
    }

    @ParameterizedTest
    @MethodSource
    public void gets_all_teams_invalid_data_returns_empty_list(final ResultSet queryResult) throws Exception {
        when(mockStatement.executeQuery()).thenReturn(queryResult);
        final Set<Team> teams = objUnderTest.getAllTeams();
        assertThat(teams).isEmpty();
    }

    private static Stream<ResultSet> gets_all_teams_invalid_data_returns_empty_list() {
        return Stream.of(partialTeamResultSet(), defaultTeamResultSet(), emptyTeamResultSet());
    }

    @Test
    public void gets_all_team_names_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(allTeamNamesResultSet());
        final Set<String> teamNames = objUnderTest.getAllTeamNames();
        assertThat(teamNames).contains("Scuderia Ferrari");
        assertThat(teamNames).contains("Minardi F1 Team");
    }

    @Test
    public void gets_empty_team_names() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(noTeamsResultSet());
        final Set<String> teamNames = objUnderTest.getAllTeamNames();
        assertThat(teamNames).isEmpty();
    }

    @Test
    public void gets_all_team_names_with_exception() throws Exception {
        mockSqlExceptionOnQuery();
        final Exception exceptionThrown = assertThrows(DataServiceException.class, ()-> objUnderTest.getAllTeamNames());
        assertThat(exceptionThrown.getMessage()).isEqualTo("Error fetching all team names from database");
    }

    @Test
    public void gets_specified_team() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(ferrari());
        final Optional<Team> team = objUnderTest.getTeam("Scuderia Ferrari");
        assertThat(team.isPresent()).isTrue();
        final Team teamReturned = team.get();
        assertThat(teamReturned.getName()).isEqualTo("Scuderia Ferrari");
        assertThat(teamReturned.getNationality()).isEqualTo("Italy");
    }

    @Test
    public void does_not_find_specified_team() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(noTeamsResultSet());
        final Optional<Team> team = objUnderTest.getTeam("Scuderia Ferrari");
        assertThat(team.isPresent()).isFalse();
    }

    @Test
    public void gets_specified_team_with_exception() throws Exception {
        mockSqlExceptionOnQuery();
        final Exception exceptionThrown = assertThrows(DataServiceException.class, () ->
                objUnderTest.getTeam("Scuderia Ferrari"));
        assertThat(exceptionThrown.getMessage()).isEqualTo("Error fetching team 'Scuderia Ferrari' from database");
    }

    @Test
    public void adds_teams_successfully() throws Exception {
        final Set<Team> teamsToAdd = createTeams();
        objUnderTest.addTeams(teamsToAdd);
        assertExecuteBatch(2);
    }

    @Test
    public void add_empty_teams_does_no_inserts() throws Exception {
        objUnderTest.addTeams(Collections.emptySet());
        verify(mockStatement, never()).execute();
    }

    @Test
    public void add_teams_throws_exception() throws Exception {
        mockSqlExceptionOnAdd();
        final Set<Team> teamsToAdd = createTeams();
        final Exception exceptionThrown = assertThrows(DataInsertionException.class, () ->
                objUnderTest.addTeams(teamsToAdd));
        assertThat(exceptionThrown).hasMessageThat().contains("Some error occurred");
    }

    private Set<Team> createTeams() {
        return ImmutableSet.of(
                Team.builder().name("Williams").nationality("UK").wiki("wiki").build(),
                Team.builder().name("McLaren").nationality("UK").wiki("wiki").build()
        );
    }
}
