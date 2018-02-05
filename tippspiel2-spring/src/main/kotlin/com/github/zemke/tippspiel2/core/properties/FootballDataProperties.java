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
}
