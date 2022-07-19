package com.william.dev.f1stats.application.rest;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/system")
@Produces(MediaType.TEXT_PLAIN)
public class SystemResource {

    @GET
    public Response checkSystemStarted() {
        log.debug("Received request checking if application is started and ready to accept requests");
        return Response.ok("Application started").build();
    }
}
