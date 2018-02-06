package com.github.zemke.tippspiel2.core.properties;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

// Is written in Java to retain IDEA support for binding application.properties to this class.

@Component
@Primary
@ConfigurationProperties("spring.datasource")
public class EmbeddedDataSourceProperties extends DataSourceProperties {

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
