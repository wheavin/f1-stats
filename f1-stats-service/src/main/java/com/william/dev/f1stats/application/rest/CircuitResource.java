package com.william.dev.f1stats.application.rest;

import com.william.dev.f1stats.application.dto.CircuitDto;
import com.william.dev.f1stats.application.dto.CircuitsDto;
import com.william.dev.f1stats.data.api.Circuit;
import com.william.dev.f1stats.data.api.StatsDataService;
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
@Path("/stats/circuit")
@Produces(MediaType.APPLICATION_JSON)
public class CircuitResource {

    @Inject
    private StatsDataService dataService;

    @Inject
    private ResponseHandler responseHandler;

    @GET
    @Path("/all")
    public Response listAllCircuits() {
        log.info("Received request to get all circuits");
        try {
            final Set<Circuit> circuits = dataService.listAllCircuits();
            log.debug("Found circuits: {}", circuits);
            return responseHandler.successResponse(new CircuitsDto(circuits));
        } catch (final Exception ex) {
            log.error("Error occurred when getting all circuits", ex);
            return responseHandler.serverErrorResponse();
        }
    }

    @GET
    @Path("/names")
    public Response listAllCircuitNames() {
        log.info("Received request to get all circuit names");
        try {
            final Set<String> circuitNames = dataService.listAllCircuitNames();
            log.debug("Found circuit names: {}", circuitNames);
            return responseHandler.successResponse(circuitNames);
        } catch (final Exception ex) {
            log.error("Error occurred when getting all circuit names", ex);
            return responseHandler.serverErrorResponse();
        }
    }

    @GET
    public Response getCircuit(@QueryParam("name") final String name) {
        log.info("Received request to get circuit: {}", name);
        try {
            final Optional<Circuit> circuit = dataService.getCircuit(name);
            if (circuit.isPresent()) {
                return responseHandler.successResponse(new CircuitDto(circuit.get()));
            }
            return responseHandler.badRequestResponse(String.format("Circuit '%s' not found", name));
        } catch (final Exception ex) {
            log.error("Error occurred when getting circuit: {}", name, ex);
            return responseHandler.serverErrorResponse();
        }
    }
}
