package com.william.dev.f1stats.application.rest;

import com.william.dev.f1stats.application.dto.TeamDto;
import com.william.dev.f1stats.application.dto.TeamsDto;
import com.william.dev.f1stats.data.api.StatsDataService;
import com.william.dev.f1stats.data.api.Team;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Path("/stats/team")
@Produces(MediaType.APPLICATION_JSON)
public class TeamResource {

    @Inject
    private StatsDataService dataService;

    @Inject
    private ResponseHandler responseHandler;

    @GET
    @Path("/all")
    public Response listAllTeams() {
        log.info("Received request to get all teams");
        try {
            final Set<Team> teams = dataService.listAllTeams();
            log.debug("Found teams: {}", teams);
            return responseHandler.successResponse(new TeamsDto(teams));
        } catch (final Exception ex) {
            log.error("Error occurred when getting all teams", ex);
            return responseHandler.serverErrorResponse();
        }
    }

    @GET
    @Path("/names")
    public Response listAllTeamNames() {
        log.info("Received request to get all team names");
        try {
            final Set<String> teamNames = dataService.listAllTeamNames();
            log.debug("Found team names: {}", teamNames);
            return responseHandler.successResponse(teamNames);
        } catch (final Exception ex) {
            log.error("Error occurred when getting all team names", ex);
            return responseHandler.serverErrorResponse();
        }
    }

    @GET
    public Response getTeam(@QueryParam("name") final String name) {
        log.info("Received request to get team: {}", name);
        try {
            final Optional<Team> team = dataService.getTeam(name);
            if (team.isPresent()) {
                return responseHandler.successResponse(new TeamDto(team.get()));
            }
            return responseHandler.badRequestResponse(String.format("Team '%s' not found", name));
        } catch (final Exception ex) {
            log.error("Error occurred when getting team: {}", name);
            return responseHandler.serverErrorResponse();
        }
    }
}
