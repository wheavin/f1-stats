package com.william.dev.f1.stats.remote;

import com.william.dev.f1.stats.remote.http.HttpClient;
import lombok.extern.slf4j.Slf4j;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

@Slf4j
public class ErgastClient implements RemoteClient {

    private String hostname = "http://ergast.com";
    private final HttpClient httpClient = new HttpClient();

    @Override
    public String getAllCircuits() {
        return httpClient.executeGET(hostname + "/api/f1/circuits.json?limit=9999", APPLICATION_JSON.toString());
    }

    @Override
    public String getAllDrivers() {
        return httpClient.executeGET(hostname + "/api/f1/drivers.json?limit=9999", APPLICATION_JSON.toString());
    }

    protected void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    public static void main(String[] args) {
        new ErgastClient().getAllCircuits();
    }
}
