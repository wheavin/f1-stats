package com.william.dev.f1stats.application;

import com.william.dev.f1stats.application.dto.TeamDto;
import com.william.dev.f1stats.application.dto.TeamsDto;
import com.william.dev.f1stats.data.db.SqliteConnectionFactory;
import de.hilling.junit.cdi.CdiTestJunitExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(CdiTestJunitExtension.class)
@ExtendWith(MockitoExtension.class)
public class F1StatsServiceTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private SqliteConnectionFactory mockConnectionFactory;

    @Inject
    private F1StatsServiceResource objUnderTest;

    @BeforeEach
    public void setup() throws Exception {
        lenient().doReturn(mockStatement).when(mockConnection).prepareStatement(anyString());
        lenient().doReturn(mockConnection).when(mockConnectionFactory).getConnection();
    }

    @Test
    public void list_all_teams_returns_list_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.allTeamsDataSet());

        final Response response = objUnderTest.listAllTeams();
        final TeamsDto teamsReturned = (TeamsDto) response.getEntity();
        assertThat(teamsReturned.getTeams()).hasSize(1);

        final TeamDto teamReturned = teamsReturned.getTeams().iterator().next();
        assertThat(teamReturned.getName()).isEqualTo("Scuderia Ferrari");
        assertThat(teamReturned.getNationality()).isEqualTo("Italy");
        assertThat(teamReturned.getWiki()).isEqualTo("https://en.wikipedia.org/wiki/Scuderia_Ferrari");
    }

    @Test
    public void list_all_teams_returns_empty_list() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.emptyDataSet());

        final Response response = objUnderTest.listAllTeams();
        final TeamsDto teamsReturned = (TeamsDto) response.getEntity();
        assertThat(teamsReturned.getTeams()).isEmpty();
    }

    @Test
    public void list_all_teams_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("Error when executing query"));
        final Response response = objUnderTest.listAllTeams();
        assertInternalServerError(response);
    }

    @Test
    public void list_all_team_names_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.allTeamsDataSet());
        final Response response = objUnderTest.listAllTeamNames();
        assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());

        final Set<String> allTeamNames = (Set<String>) response.getEntity();
        assertThat(allTeamNames).contains("Scuderia Ferrari");
    }

    @Test
    public void list_all_team_names_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("some error"));
        final Response response = objUnderTest.listAllTeamNames();
        assertInternalServerError(response);
    }

    @Test
    public void get_specified_team_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.allTeamsDataSet());

        final Response response = objUnderTest.getTeam("Scuderia Ferrari");
        final TeamDto teamReturned = (TeamDto) response.getEntity();
        assertThat(teamReturned.getName()).isEqualTo("Scuderia Ferrari");
        assertThat(teamReturned.getNationality()).isEqualTo("Italy");
        assertThat(teamReturned.getWiki()).isEqualTo("https://en.wikipedia.org/wiki/Scuderia_Ferrari");
    }

    @Test
    public void get_specified_team_not_found() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.emptyDataSet());
        final Response response = objUnderTest.getTeam("Scuderia Ferrari");
        assertBadRequest(response, "Team 'Scuderia Ferrari' not found");
    }

    @Test
    public void get_specified_team_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("some error"));
        final Response response = objUnderTest.getTeam("Scuderia Ferrari");
        assertInternalServerError(response);
    }

    private static void assertInternalServerError(final Response response) {
        assertThat(response.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR.getStatusCode());
        final String message = (String) response.getEntity();
        assertThat(message).isEqualTo("Internal Server Error");
    }

    private static void assertBadRequest(final Response response, final String expectedMessage) {
        assertThat(response.getStatus()).isEqualTo(Status.BAD_REQUEST.getStatusCode());
        final String responseMessage = (String) response.getEntity();
        assertThat(responseMessage).isEqualTo(expectedMessage);
    }

}
