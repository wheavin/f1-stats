package com.william.dev.f1stats.application.rest.assertions;

import javax.ws.rs.core.Response;

import static com.google.common.truth.Truth.assertThat;

public class ResponseAssertions {

    private final Response response;

    private ResponseAssertions(final Response response) {
        this.response = response;
    }

    public static ResponseAssertions assertThatResponse(final Response response) {
        return new ResponseAssertions(response);
    }

    public void isOk() {
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    public void isBadRequest() {
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    public void isInternalServerError() {
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        hasMessage("Internal Server Error");
    }

    public void hasMessage(final String expectedMessage) {
        final String responseMessage = (String) response.getEntity();
        assertThat(responseMessage).isEqualTo(expectedMessage);
    }
}
