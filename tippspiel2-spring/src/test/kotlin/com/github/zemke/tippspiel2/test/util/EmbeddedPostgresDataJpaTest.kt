package com.github.zemke.tippspiel2.test.util

import com.github.zemke.tippspiel2.core.config.DataSourceConfig
import com.github.zemke.tippspiel2.core.properties.EmbeddedDataSourceProperties
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(DataSourceConfig::class, EmbeddedDataSourceProperties::class)
annotation class EmbeddedPostgresDataJpaTest
