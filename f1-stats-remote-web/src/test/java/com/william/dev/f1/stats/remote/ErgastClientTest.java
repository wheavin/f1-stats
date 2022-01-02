package com.william.dev.f1.stats.remote;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.google.common.truth.Truth.assertThat;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class ErgastClientTest {

    private static final String GET_ALL_CIRCUITS_ENDPOINT = "/api/f1/circuits.json?limit=9999";
    private static final String GET_ALL_DRIVERS_ENDPOINT = "/api/f1/drivers.json?limit=9999";
    private static final String GET_ALL_CONSTRUCTORS_ENDPOINT = "/api/f1/constructors.json?limit=9999";

    private static WireMockServer wireMockServer;
    private ErgastClient objUnderTest;

    @BeforeAll
    public static void setupClass() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
    }

    @AfterAll
    public static void teardownClass() {
        wireMockServer.stop();
    }

    @BeforeEach
    public void setupTest() {
        objUnderTest = new ErgastClient();
        objUnderTest.setHostname("http://localhost:" + wireMockServer.port());
        configureFor("localhost", wireMockServer.port());
    }

    @Test
    public void gets_all_circuits_successfully() {
        stubFor(get(urlEqualTo(GET_ALL_CIRCUITS_ENDPOINT))
                .willReturn(aResponse()
                        .withHeader("Content-Type", APPLICATION_JSON.toString())
                        .withStatus(200)
                        .withBodyFile("all_circuits_response.json")));
        final String response = objUnderTest.getAllCircuits();
        assertThat(response).contains("Silverstone");
    }

    @Test
    public void get_all_circuits_request_fails() {
        stubFor(get(urlEqualTo(GET_ALL_CIRCUITS_ENDPOINT))
                .willReturn(aResponse()
                        .withHeader("Content-Type", APPLICATION_JSON.toString())
                        .withStatus(404)
                        .withBody("ergast.com: Temporary failure in name resolution")));
        final String response = objUnderTest.getAllCircuits();
        assertThat(response).isEmpty();
    }

    @Test
    public void gets_all_drivers_successfully() {
        stubFor(get(urlEqualTo(GET_ALL_DRIVERS_ENDPOINT))
                .willReturn(aResponse()
                        .withHeader("Content-Type", APPLICATION_JSON.toString())
                        .withStatus(200)
                        .withBodyFile("all_drivers_response.json")));
        final String response = objUnderTest.getAllDrivers();
        assertThat(response).contains("Schumacher");
    }

    @Test
    public void get_all_drivers_request_fails() {
        stubFor(get(urlEqualTo(GET_ALL_DRIVERS_ENDPOINT))
                .willReturn(aResponse()
                        .withHeader("Content-Type", APPLICATION_JSON.toString())
                        .withStatus(404)
                        .withBody("ergast.com: Temporary failure in name resolution")));
        final String response = objUnderTest.getAllCircuits();
        assertThat(response).isEmpty();
    }

    @Test
    public void gets_all_teams_successfully() {
        stubFor(get(urlEqualTo(GET_ALL_CONSTRUCTORS_ENDPOINT))
                .willReturn(aResponse()
                        .withHeader("Content-Type", APPLICATION_JSON.toString())
                        .withStatus(200)
                        .withBodyFile("all_teams_response.json")));
        final String response = objUnderTest.getAllTeams();
        assertThat(response).contains("Racing Point");
    }

    @Test
    public void get_all_teams_request_fails() {
        stubFor(get(urlEqualTo(GET_ALL_CONSTRUCTORS_ENDPOINT))
                .willReturn(aResponse()
                        .withHeader("Content-Type", APPLICATION_JSON.toString())
                        .withStatus(404)
                        .withBody("ergast.com: Temporary failure in name resolution")));
        final String response = objUnderTest.getAllTeams();
        assertThat(response).isEmpty();
    }
}
