package com.william.dev.f1stats.application.rest;

import com.william.dev.f1stats.application.dto.DriverDto;
import com.william.dev.f1stats.application.dto.DriversDto;
import com.william.dev.f1stats.data.api.Driver;
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
@Path("/stats/driver")
@Produces(MediaType.APPLICATION_JSON)
public class DriverResource {

    @Inject
    private StatsDataService dataService;

    @Inject
    private ResponseHandler responseHandler;

    @GET
    @Path("/all")
    public Response listAllDrivers() {
        log.info("Received request to get all drivers");
        try {
            final Set<Driver> drivers = dataService.listAllDrivers();
            log.debug("Found circuits: {}", drivers);
            return responseHandler.successResponse(new DriversDto(drivers));
        } catch (final Exception ex) {
            log.error("Error occurred when getting all drivers", ex);
            return responseHandler.serverErrorResponse();
        }
    }

    @GET
    public Response getDriver(@QueryParam("firstName") final String firstName,
                              @QueryParam("lastName") final String lastName) {
        log.info("Received request to get driver: {} {}", firstName, lastName);
        try {
            final Optional<Driver> driver = dataService.getDriver(firstName, lastName);
            if (driver.isPresent()) {
                return responseHandler.successResponse(new DriverDto(driver.get()));
            }
            return responseHandler.badRequestResponse(String.format("Driver '%s %s' not found", firstName, lastName));
        } catch (final Exception ex) {
            log.error("Error occurred when getting driver: {} {}", firstName, lastName, ex);
            return responseHandler.serverErrorResponse();
        }
    }
}
