package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.view.util.JacksonUtils
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.LocalDateTime
import java.time.ZoneOffset
import com.github.zemke.tippspiel2.test.util.JpaTest

@JpaTest
class FixtureRepositoryTest {

    @Autowired private lateinit var fixtureRepository: FixtureRepository

    @Autowired private lateinit var testEntityManager: TestEntityManager

    @Test
    fun testSaveAll() {
        val competition = Competition(
                id = 1,
                caption = "World Cup 2018 Russia",
                league = "WC",
                numberOfAvailableSeasons = 8,
                year = 2018,
                currentMatchday = 1,
                lastUpdated = LocalDateTime.of(2018, 1, 10, 14, 10).toInstant(ZoneOffset.UTC),
                champion = null,
                current = false,
                championBetAllowed = true
        )
        val fixturesToPersist = listOf(
                Fixture(
                        id = 1,
                        date = LocalDateTime.of(2018, 1, 10, 10, 12).toInstant(ZoneOffset.UTC),
                        status = FixtureStatus.SCHEDULED,
                        matchday = 1,
                        goalsHomeTeam = null,
                        goalsAwayTeam = null,
                        homeTeam = Team(
                                id = 1,
                                name = "Russia",
                                competition = competition
                        ),
                        awayTeam = Team(
                                id = 2,
                                name = "Saudi Arabia",
                                competition = competition
                        ),
                        competition = competition),
                Fixture(
                        id = 2,
                        date = LocalDateTime.of(2018, 1, 11, 10, 12).toInstant(ZoneOffset.UTC),
                        status = FixtureStatus.SCHEDULED,
                        matchday = 1,
                        goalsHomeTeam = null,
                        goalsAwayTeam = null,
                        homeTeam = Team(
                                id = 3,
                                name = "Germany",
                                competition = competition
                        ),
                        awayTeam = Team(
                                id = 4,
                                name = "United States",
                                competition = competition
                        ),
                        competition = competition)
        )

        val managedFixturesToPersist = fixtureRepository.saveAll(fixturesToPersist)
        val persistedFixtures = testEntityManager.entityManager.createQuery("select f from Fixture f", Fixture::class.java).resultList

        Assert.assertArrayEquals(managedFixturesToPersist.toTypedArray(), persistedFixtures.toTypedArray())
    }
}
