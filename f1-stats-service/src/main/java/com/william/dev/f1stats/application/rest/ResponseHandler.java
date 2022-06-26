package com.william.dev.f1stats.application.rest;

import javax.ws.rs.core.Response;

public class ResponseHandler {

    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    public Response successResponse(final Object entity) {
        return Response.ok(entity).build();
    }

    public Response badRequestResponse(final String message) {
        return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
    }

    public Response serverErrorResponse() {
        return Response.serverError().entity(INTERNAL_SERVER_ERROR).build();
    }
}
