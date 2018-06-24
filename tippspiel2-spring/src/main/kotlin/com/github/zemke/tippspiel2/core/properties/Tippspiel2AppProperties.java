package com.github.zemke.tippspiel2.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("tippspiel2.app")
public class Tippspiel2AppProperties {

    private String url;
    private String supportMail;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSupportMail() {
        return supportMail;
    }

    public void setSupportMail(String supportMail) {
        this.supportMail = supportMail;
    }
}
