package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Standing
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.test.util.EmbeddedPostgresDataJpaTest
import com.github.zemke.tippspiel2.test.util.PersistenceUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@EmbeddedPostgresDataJpaTest
@Import(StandingService::class)
open class StandingServiceTest {

    @Autowired
    private lateinit var standingService: StandingService

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    fun testUpdateByFixtures() {
        val user1 = PersistenceUtils.createUser(testEntityManager, "1")
        val user2 = PersistenceUtils.createUser(testEntityManager, "2")

        val bettingGame = PersistenceUtils.createBettingGame(testEntityManager, listOf(user1, user2))
        val teams = persistAndFlushTeams(bettingGame.competition, "Russia", "Norway", "England", "Italy", "Ireland", "Iceland", "Poland", "Spain")

        val fixtures = listOf(
                testEntityManager.persistAndFlush(PersistenceUtils.instantiateFixture().copy(
                        competition = bettingGame.competition,
                        goalsHomeTeam = 2,
                        goalsAwayTeam = 0,
                        homeTeam = teams[0],
                        awayTeam = teams[1],
                        status = FixtureStatus.FINISHED)),
                testEntityManager.persistAndFlush(PersistenceUtils.instantiateFixture().copy(
                        competition = bettingGame.competition,
                        goalsHomeTeam = 2,
                        goalsAwayTeam = 1,
                        homeTeam = teams[2],
                        awayTeam = teams[3],
                        status = FixtureStatus.FINISHED)),
                testEntityManager.persistAndFlush(PersistenceUtils.instantiateFixture().copy(
                        competition = bettingGame.competition,
                        goalsHomeTeam = 1,
                        goalsAwayTeam = 1,
                        homeTeam = teams[4],
                        awayTeam = teams[5],
                        status = FixtureStatus.FINISHED))
        )

        val bets = listOf(
                // Exact.
                testEntityManager.persistAndFlush(PersistenceUtils.instantiateBet().copy(
                        fixture = fixtures[0],
                        bettingGame = bettingGame,
                        user = user1,
                        goalsHomeTeamBet = 2,
                        goalsAwayTeamBet = 0
                )),
                // Entirely wrong.
                testEntityManager.persistAndFlush(PersistenceUtils.instantiateBet().copy(
                        fixture = fixtures[0],
                        bettingGame = bettingGame,
                        user = user2,
                        goalsHomeTeamBet = 0,
                        goalsAwayTeamBet = 2
                )),
                // Goal diff.
                testEntityManager.persistAndFlush(PersistenceUtils.instantiateBet().copy(
                        fixture = fixtures[1],
                        bettingGame = bettingGame,
                        user = user1,
                        goalsHomeTeamBet = 1,
                        goalsAwayTeamBet = 0
                )),
                // Winner.
                testEntityManager.persistAndFlush(PersistenceUtils.instantiateBet().copy(
                        fixture = fixtures[1],
                        bettingGame = bettingGame,
                        user = user2,
                        goalsHomeTeamBet = 3,
                        goalsAwayTeamBet = 0
                )),
                // Exact.
                testEntityManager.persistAndFlush(PersistenceUtils.instantiateBet().copy(
                        fixture = fixtures[2],
                        bettingGame = bettingGame,
                        user = user1,
                        goalsHomeTeamBet = 1,
                        goalsAwayTeamBet = 1
                ))
                // No bet by user2.
        )

        val standingsBefore = listOf(
                testEntityManager.persistAndFlush(Standing(id = null, user = user1, bettingGame = bettingGame)),
                testEntityManager.persistAndFlush(Standing(id = null, user = user2, bettingGame = bettingGame)))

        val standingsActual = standingService.updateStandings(bettingGame.competition)

        Assert.assertEquals(
                listOf(
                        standingsBefore[0].copy(
                                exactBets = 2,
                                goalDifferenceBets = 1,
                                winnerBets = 0,
                                wrongBets = 0,
                                missedBets = 0,
                                points = 13
                        ),
                        standingsBefore[1].copy(
                                exactBets = 0,
                                goalDifferenceBets = 0,
                                winnerBets = 1,
                                wrongBets = 1,
                                missedBets = 1,
                                points = 1
                        )),
                standingsActual)
    }

    private fun persistAndFlushTeams(competition: Competition, vararg teamNames: String): List<Team> =
            teamNames.map {
                testEntityManager.persistAndFlush(
                        PersistenceUtils.instantiateTeam().copy(
                                competition = competition, name = it, id = (teamNames.indexOf(it) + 1).toLong()))
            }
}
