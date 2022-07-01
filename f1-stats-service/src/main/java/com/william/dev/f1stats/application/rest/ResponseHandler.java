package com.william.dev.f1stats.application.rest;

import com.william.dev.f1stats.application.dto.FailureMessage;

import javax.ws.rs.core.Response;

public class ResponseHandler {

    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    public Response successResponse(final Object entity) {
        return Response.ok(entity).build();
    }

    public Response badRequestResponse(final String message) {
        final FailureMessage failureMessage = new FailureMessage(message);
        return Response.status(Response.Status.BAD_REQUEST).entity(failureMessage).build();
    }

    public Response serverErrorResponse() {
        final FailureMessage failureMessage = new FailureMessage(INTERNAL_SERVER_ERROR);
        return Response.serverError().entity(failureMessage).build();
    }
}
