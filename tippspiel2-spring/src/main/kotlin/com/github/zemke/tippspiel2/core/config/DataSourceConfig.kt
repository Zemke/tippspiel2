package com.github.zemke.tippspiel2.core.config

import com.github.zemke.tippspiel2.core.profile.Dev
import com.github.zemke.tippspiel2.core.profile.Prod
import com.github.zemke.tippspiel2.core.properties.EmbeddedDataSourceProperties
import com.zaxxer.hikari.HikariDataSource
import org.apache.tomcat.jdbc.pool.DataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres
import java.net.URI
import java.nio.file.Paths

@Configuration
class DataSourceConfig {

    @Autowired
    private lateinit var embeddedDataSourceProperties: EmbeddedDataSourceProperties

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    @Primary
    @Dev
    fun embeddedPostgresDatabaseDataSource(): HikariDataSource {
        startEmbeddedPostgresDatabase()
        return DataSourceBuilder
                .create()
                .type(HikariDataSource::class.java)
                .build()
    }

    @Bean
    @ConfigurationProperties("spring.datasource")
    @Primary
    @Prod
    fun standalonePostgresDatabaseDataSource(): DataSource {
        return org.apache.tomcat.jdbc.pool.DataSource();
    }

    private fun startEmbeddedPostgresDatabase() {
        val uri = URI.create(embeddedDataSourceProperties.jdbcUrl.substring(5))
        EmbeddedPostgres { "9.1.0-1" }
                .start(EmbeddedPostgres.cachedRuntimeConfig(
                        Paths.get(embeddedDataSourceProperties.embeddedDirectory)),
                        uri.host, uri.port, uri.path.substring(1),
                        embeddedDataSourceProperties.username, embeddedDataSourceProperties.password, emptyList())
    }
}
