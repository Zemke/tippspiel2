package com.github.zemke.tippspiel2.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// Is written in Java to retain IDEA support for binding application.properties to this class.

@Component
@ConfigurationProperties("tippspiel2.football-data")
public class FootballDataProperties {

    /**
     * The auth token to authenticate toward the external API.
     */
    private String apiToken;

    /**
     * The name of the header to put the {@link com.github.zemke.tippspiel2.core.properties.FootballDataProperties#apiToken} in.
     */
    private String apiTokenHeader = "X-Auth-Token";

    /**
     * The endpoint to run requests against i.e. {@code https://www.football-data.org/v1/}.
     */
    private String endpoint;

    /**
     * Milliseconds between polling of endpoint.
     */
    private Long fixedDelay = 60000L;

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getApiTokenHeader() {
        return apiTokenHeader;
    }

    public void setApiTokenHeader(String apiTokenHeader) {
        this.apiTokenHeader = apiTokenHeader;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Long getFixedDelay() {
        return fixedDelay;
    }

    public void setFixedDelay(Long fixedDelay) {
        this.fixedDelay = fixedDelay;
    }
}
