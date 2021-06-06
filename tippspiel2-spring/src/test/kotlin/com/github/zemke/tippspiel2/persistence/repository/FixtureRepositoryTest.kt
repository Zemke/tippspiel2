package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import com.github.zemke.tippspiel2.view.util.JacksonUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner
import java.sql.Timestamp
import java.util.*

@RunWith(SpringRunner::class)
@DataJpaTest
class FixtureRepositoryTest {

    @Autowired
    private lateinit var fixtureRepository: FixtureRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    fun testSaveAll() {
        val competition = Competition(
                id = 1,
                caption = "World Cup 2018 Russia",
                league = "WC",
                year = "2018",
                currentMatchday = 1,
                numberOfMatchdays = 8,
                numberOfTeams = 32,
                numberOfGames = 64,
                lastUpdated = Timestamp(JacksonUtils.toDate("2018-01-10T14:10:08Z").time),
                champion = null,
                current = false,
                championBetAllowed = true
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

        val managedFixturesToPersist = fixtureRepository.saveAll(fixturesToPersist)
        val persistedFixtures = testEntityManager.entityManager.createQuery("select f from Fixture f", Fixture::class.java).resultList

        Assert.assertArrayEquals(managedFixturesToPersist.toTypedArray(), persistedFixtures.toTypedArray())
    }
}
