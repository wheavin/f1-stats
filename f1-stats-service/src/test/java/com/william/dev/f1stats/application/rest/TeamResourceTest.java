package com.william.dev.f1stats.application.rest;

import com.william.dev.f1stats.application.TestData;
import com.william.dev.f1stats.application.dto.TeamDto;
import com.william.dev.f1stats.application.dto.TeamsDto;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static com.william.dev.f1stats.application.rest.assertions.ResponseAssertions.assertThatResponse;
import static org.mockito.Mockito.when;

public class TeamResourceTest extends ResourceTest {

    @Inject
    private TeamResource objUnderTest;

    @Test
    public void list_all_teams_returns_list_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.allTeamsDataSet());

        final Response response = objUnderTest.listAllTeams();
        assertThatResponse(response).isOk();

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
        assertThatResponse(response).isOk();

        final TeamsDto teamsReturned = (TeamsDto) response.getEntity();
        assertThat(teamsReturned.getTeams()).isEmpty();
    }

    @Test
    public void list_all_teams_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("Error when executing query"));
        final Response response = objUnderTest.listAllTeams();
        assertThatResponse(response).isInternalServerError();
    }

    @Test
    public void list_all_team_names_successfully() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(TestData.allTeamsDataSet());
        final Response response = objUnderTest.listAllTeamNames();
        assertThatResponse(response).isOk();

        final Set<String> allTeamNames = (Set<String>) response.getEntity();
        assertThat(allTeamNames).contains("Scuderia Ferrari");
    }

    @Test
    public void list_all_team_names_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("some error"));
        final Response response = objUnderTest.listAllTeamNames();
        assertThatResponse(response).isInternalServerError();
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

        assertThatResponse(response).isBadRequest();
        assertThatResponse(response).hasMessage("Team 'Scuderia Ferrari' not found");
    }

    @Test
    public void get_specified_team_throws_exception() throws Exception {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("some error"));
        final Response response = objUnderTest.getTeam("Scuderia Ferrari");
        assertThatResponse(response).isInternalServerError();
    }

}
