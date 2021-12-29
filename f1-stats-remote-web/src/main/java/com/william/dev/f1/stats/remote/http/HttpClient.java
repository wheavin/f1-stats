package com.william.dev.f1.stats.remote.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Slf4j
public class HttpClient {
    private static final String ACCEPT_HEADER = "Accept";
    private static final String CONTENT_TYPE_HEADER = "Content-type";

    public String executeGET(final String url, final String mediaType) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader(ACCEPT_HEADER, mediaType);
            httpGet.setHeader(CONTENT_TYPE_HEADER, mediaType);

            log.info("Executing request " + httpGet.getRequestLine());
            final ResponseHandler<String> responseHandler = createResponseHandler();
            return httpClient.execute(httpGet, responseHandler);
        } catch (final IOException ex) {
            log.error("Error occurred executing HTTP GET request", ex);
        }
        return "";
    }

    private static ResponseHandler<String> createResponseHandler() {
        return httpResponse -> {
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (successStatusCode(statusCode)) {
                final HttpEntity responseEntity = httpResponse.getEntity();
                return responseEntity != null ? EntityUtils.toString(responseEntity) : "";
            } else {
                throw new ClientProtocolException("Unexpected response status: " + statusCode);
            }
        };
    }

    private static boolean successStatusCode(final int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }
}
