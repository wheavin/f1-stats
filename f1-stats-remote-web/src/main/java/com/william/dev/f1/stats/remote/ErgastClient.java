package com.william.dev.f1.stats.remote;

import com.william.dev.f1.stats.remote.http.HttpClient;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

import static com.william.dev.f1stats.common.Constants.DATA_SOURCE_HOSTNAME;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

@Slf4j
public class ErgastClient implements RemoteClient {

    private static final String DEFAULT_DATA_SOURCE_HOSTNAME = "http://ergast.com";
    private final HttpClient httpClient;
    private String hostname;

    @Inject
    public ErgastClient() {
        this.httpClient = new HttpClient();
        this.hostname = loadHostname();
    }

    private String loadHostname() {
        final String hostnameSetInEnv = System.getenv(DATA_SOURCE_HOSTNAME);
        if (hostnameSetInEnv == null || hostnameSetInEnv.trim().isEmpty()) {
            return DEFAULT_DATA_SOURCE_HOSTNAME;
        }
        log.info("Remote data source hostname set to '{}'", hostnameSetInEnv);
        return hostnameSetInEnv;
    }

    @Override
    public String getAllCircuits() {
        return httpClient.executeGET(hostname + "/api/f1/circuits.json?limit=9999", APPLICATION_JSON.toString());
    }

    @Override
    public String getAllDrivers() {
        return httpClient.executeGET(hostname + "/api/f1/drivers.json?limit=9999", APPLICATION_JSON.toString());
    }

    @Override
    public String getAllTeams() {
        return httpClient.executeGET(hostname + "/api/f1/constructors.json?limit=9999", APPLICATION_JSON.toString());
    }

    protected void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    public static void main(String[] args) {
        final String test = new ErgastClient().getAllTeams();
    }
}
