package com.william.dev.f1stats.application;

import com.william.dev.f1stats.application.dto.CircuitDto;
import com.william.dev.f1stats.application.dto.CircuitsDto;
import com.william.dev.f1stats.application.dto.DriverDto;
import com.william.dev.f1stats.application.dto.DriversDto;
import com.william.dev.f1stats.application.dto.TeamDto;
import com.william.dev.f1stats.application.dto.TeamsDto;
import com.william.dev.f1stats.data.api.Circuit;
import com.william.dev.f1stats.data.api.Driver;
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
import javax.ws.rs.core.Response.Status;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Path("/stats")
@Produces(MediaType.APPLICATION_JSON)
public class F1StatsServiceResource implements F1StatsService {

    @Inject
    private StatsDataService dataService;

    @Override
    @GET
    @Path("/circuit/all")
    public Response listAllCircuits() {
        log.info("Received request to get all circuits");
        try {
            final Set<Circuit> circuits = dataService.listAllCircuits();
            log.debug("Found circuits: {}", circuits);
            return successResponse(new CircuitsDto(circuits));
        } catch (final Exception ex) {
            log.error("Error occurred when getting all circuits", ex);
            return serverErrorResponse();
        }
    }

    @Override
    @GET
    @Path("/circuit/names")
    public Response listAllCircuitNames() {
        log.info("Received request to get all circuit names");
        try {
            final Set<String> circuitNames = dataService.listAllCircuitNames();
            log.debug("Found circuit names: {}", circuitNames);
            return successResponse(circuitNames);
        } catch (final Exception ex) {
            log.error("Error occurred when getting all circuit names", ex);
            return serverErrorResponse();
        }
    }

    @Override
    @GET
    @Path("/circuit")
    public Response getCircuit(@QueryParam("name") final String name) {
        log.info("Received request to get circuit: {}", name);
        try {
            final Optional<Circuit> circuit = dataService.getCircuit(name);
            if (circuit.isPresent()) {
                return successResponse(new CircuitDto(circuit.get()));
            }
            return badRequestResponse(String.format("Circuit '%s' not found", name));
        } catch (final Exception ex) {
            log.error("Error occurred when getting circuit: {}", name, ex);
            return serverErrorResponse();
        }
    }

    @Override
    @GET
    @Path("/driver/all")
    public Response listAllDrivers() {
        log.info("Received request to get all drivers");
        try {
            final Set<Driver> drivers = dataService.listAllDrivers();
            log.debug("Found circuits: {}", drivers);
            return successResponse(new DriversDto(drivers));
        } catch (final Exception ex) {
            log.error("Error occurred when getting all drivers", ex);
            return serverErrorResponse();
        }
    }

    @Override
    @GET
    @Path("/driver")
    public Response getDriver(@QueryParam("firstName") final String firstName,
                              @QueryParam("lastName") final String lastName) {
        log.info("Received request to get driver: {} {}", firstName, lastName);
        try {
            final Optional<Driver> driver = dataService.getDriver(firstName, lastName);
            if (driver.isPresent()) {
                return successResponse(new DriverDto(driver.get()));
            }
            return badRequestResponse(String.format("Driver '%s %s' not found", firstName, lastName));
        } catch (final Exception ex) {
            log.error("Error occurred when getting driver: {} {}", firstName, lastName, ex);
            return serverErrorResponse();
        }
    }

    @Override
    @GET
    @Path("/team/all")
    public Response listAllTeams() {
        log.info("Received request to get all teams");
        try {
            final Set<Team> teams = dataService.listAllTeams();
            log.debug("Found teams: {}", teams);
            return successResponse(new TeamsDto(teams));
        } catch (final Exception ex) {
            log.error("Error occurred when getting all teams", ex);
            return serverErrorResponse();
        }
    }

    @Override
    public Response listAllTeamNames() {
        return null;
    }

    @Override
    @GET
    @Path("/team")
    public Response getTeam(@QueryParam("name") final String name) {
        log.info("Received request to get team: {}", name);
        try {
            final Optional<Team> team = dataService.getTeam(name);
            if (team.isPresent()) {
                return successResponse(new TeamDto(team.get()));
            }
            return badRequestResponse(String.format("Team '%s' not found", name));
        } catch (final Exception ex) {
            log.error("Error occurred when getting team: {}", name);
            return serverErrorResponse();
        }
    }

    private Response successResponse(final Object entity) {
        return Response.ok(entity).build();
    }

    private Response badRequestResponse(final String message) {
        return Response.status(Status.BAD_REQUEST).entity(message).build();
    }

    private Response serverErrorResponse() {
        return Response.serverError().entity("Internal Server Error").build();
    }
}
