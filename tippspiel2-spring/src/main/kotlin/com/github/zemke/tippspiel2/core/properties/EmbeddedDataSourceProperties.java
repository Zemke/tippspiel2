package com.github.zemke.tippspiel2.core.properties;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

// Is written in Java to retain IDEA support for binding application.properties to this class.

@Component
@Primary
@ConfigurationProperties("spring.datasource.hikari")
public class EmbeddedDataSourceProperties extends HikariConfig {

    /**
     * Location of embedded database.
     */
    private String embeddedDirectory = "target";

    public String getEmbeddedDirectory() {
        return embeddedDirectory;
    }

    public void setEmbeddedDirectory(String embeddedDirectory) {
        this.embeddedDirectory = embeddedDirectory;
    }
}
