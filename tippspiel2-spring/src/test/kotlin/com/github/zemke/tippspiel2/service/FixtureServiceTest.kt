package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.test.util.EmbeddedPostgresDataJpaTest
import com.github.zemke.tippspiel2.test.util.JacksonUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import java.sql.Timestamp
import java.util.*

@RunWith(SpringRunner::class)
@EmbeddedPostgresDataJpaTest
@Import(FixtureService::class)
open class FixtureServiceTest {

    @Autowired
    private lateinit var fixtureService: FixtureService

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    fun testSaveMany() {
        val competition = Competition(
                id = 1,
                caption = "World Cup 2018 Russia",
                league = "WC",
                year = "2018",
                currentMatchday = 1,
                numberOfMatchdays = 8,
                numberOfTeams = 32,
                numberOfGames = 64,
                lastUpdated = Timestamp(JacksonUtils.toDate("2018-01-10T14:10:08Z").time)
        )
        val fixturesToPersist = listOf(
                Fixture(
                        id = 1,
                        date = Timestamp(GregorianCalendar(2018, 1, 10, 10, 12).timeInMillis),
                        status = FixtureStatus.TIMED,
                        matchday = 1,
                        odds = null,
                        goalsHomeTeam = null,
                        goalsAwayTeam = null,
                        homeTeam = Team(
                                id = 1,
                                name = "Russia",
                                squadMarketValue = null,
                                competition = competition
                        ),
                        awayTeam = Team(
                                id = 2,
                                name = "Saudi Arabia",
                                squadMarketValue = null,
                                competition = competition
                        ),
                        competition = competition),
                Fixture(
                        id = 2,
                        date = Timestamp(GregorianCalendar(2018, 1, 11, 10, 12).timeInMillis),
                        status = FixtureStatus.TIMED,
                        matchday = 1,
                        odds = null,
                        goalsHomeTeam = null,
                        goalsAwayTeam = null,
                        homeTeam = Team(
                                id = 3,
                                name = "Germany",
                                squadMarketValue = null,
                                competition = competition
                        ),
                        awayTeam = Team(
                                id = 4,
                                name = "United States",
                                squadMarketValue = null,
                                competition = competition
                        ),
                        competition = competition)
        )

        fixtureService.saveMany(fixturesToPersist)
        val persistedFixtures = testEntityManager.entityManager.createQuery("select f from Fixture f", Fixture::class.java).resultList
        Assert.assertArrayEquals(fixturesToPersist.toTypedArray(), persistedFixtures.toTypedArray())
    }
}
