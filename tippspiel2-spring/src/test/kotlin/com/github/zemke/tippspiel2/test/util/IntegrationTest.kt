package com.github.zemke.tippspiel2.test.util

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
    properties = [
        "spring.jpa.properties.hibernate.default_schema=public",
        "spring.datasource.hikari.driver-class-name=org.h2.Driver",
        "spring.datasource.hikari.jdbc-url=jdbc:h2:mem:db;DB_CLOSE_DELAY=-1",
        "spring.datasource.hikari.username=sa",
        "spring.datasource.hikari.password=sa",
        "spring.jpa.hibernate.ddl-auto=create-drop",
    ]
)
@DirtiesContext
annotation class IntegrationTest
