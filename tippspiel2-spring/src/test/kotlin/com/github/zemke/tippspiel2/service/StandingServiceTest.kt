package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Bet
import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.Standing
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.model.User
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.persistence.repository.BetRepository
import com.github.zemke.tippspiel2.persistence.repository.FixtureRepository
import com.github.zemke.tippspiel2.persistence.repository.StandingRepository
import com.github.zemke.tippspiel2.test.util.PersistenceUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class StandingServiceTest {

    @InjectMocks
    @Spy
    private lateinit var standingService: StandingService

    @Mock
    private lateinit var betRepository: BetRepository

    @Mock
    private lateinit var standingRepository: StandingRepository

    @Mock
    private lateinit var fixtureRepository: FixtureRepository

    @Test
    fun testUpdateStandings() {
        val user1 = PersistenceUtils.instantiateUser("1")
        val user2 = PersistenceUtils.instantiateUser("2")

        val community = PersistenceUtils.instantiateCommunity().copy(users = listOf(user1, user2))
        val bettingGame = PersistenceUtils.instantiateBettingGame().copy(community = community)
        val teams = createTeams(bettingGame.competition, "Russia", "Norway", "England", "Italy", "Ireland", "Iceland", "Poland", "Spain")

        Mockito
                .`when`(betRepository.findByCompetitionAndFixtureStatus(bettingGame.competition, FixtureStatus.FINISHED))
                .thenReturn(createBets(createFixtures(bettingGame, teams), bettingGame, user1, user2))

        Mockito
                .`when`(standingRepository.findAll())
                .thenReturn(createStandings(user1, bettingGame, user2))

        Mockito
                .`when`(fixtureRepository.findAll())
                .thenReturn(createFixtures(bettingGame, teams))

        Mockito
                .`when`(standingRepository.saveAll(Mockito.anyList<Standing>()))
                .thenAnswer { it.getArgument(0) }

        val standingsActual = standingService.updateStandings(bettingGame.competition)

        Assert.assertEquals(
                listOf(
                        createStandings(user1, bettingGame, user2)[0].copy(
                                exactBets = 2,
                                goalDifferenceBets = 1,
                                winnerBets = 0,
                                wrongBets = 0,
                                missedBets = 0,
                                points = 13
                        ),
                        createStandings(user1, bettingGame, user2)[1].copy(
                                exactBets = 0,
                                goalDifferenceBets = 0,
                                winnerBets = 1,
                                wrongBets = 1,
                                missedBets = 1,
                                points = 1
                        )),
                standingsActual)
    }

    private fun createStandings(user1: User, bettingGame: BettingGame, user2: User): List<Standing> =
            listOf(
                    Standing(id = null, user = user1, bettingGame = bettingGame),
                    Standing(id = null, user = user2, bettingGame = bettingGame))

    private fun createBets(fixtures: List<Fixture>, bettingGame: BettingGame, user1: User, user2: User): List<Bet> =
            listOf(
                    // Exact.
                    PersistenceUtils.instantiateBet().copy(
                            fixture = fixtures[0],
                            bettingGame = bettingGame,
                            user = user1,
                            goalsHomeTeamBet = 2,
                            goalsAwayTeamBet = 0
                    ),
                    // Entirely wrong.
                    PersistenceUtils.instantiateBet().copy(
                            fixture = fixtures[0],
                            bettingGame = bettingGame,
                            user = user2,
                            goalsHomeTeamBet = 0,
                            goalsAwayTeamBet = 2
                    ),
                    // Goal diff.
                    PersistenceUtils.instantiateBet().copy(
                            fixture = fixtures[1],
                            bettingGame = bettingGame,
                            user = user1,
                            goalsHomeTeamBet = 1,
                            goalsAwayTeamBet = 0
                    ),
                    // Winner.
                    PersistenceUtils.instantiateBet().copy(
                            fixture = fixtures[1],
                            bettingGame = bettingGame,
                            user = user2,
                            goalsHomeTeamBet = 3,
                            goalsAwayTeamBet = 0
                    ),
                    // Exact.
                    PersistenceUtils.instantiateBet().copy(
                            fixture = fixtures[2],
                            bettingGame = bettingGame,
                            user = user1,
                            goalsHomeTeamBet = 1,
                            goalsAwayTeamBet = 1
                    )                // No bet by user2.
            )

    private fun createFixtures(bettingGame: BettingGame, teams: List<Team>): List<Fixture> =
            listOf(
                    PersistenceUtils.instantiateFixture().copy(
                            competition = bettingGame.competition,
                            goalsHomeTeam = 2,
                            goalsAwayTeam = 0,
                            homeTeam = teams[0],
                            awayTeam = teams[1],
                            status = FixtureStatus.FINISHED),
                    PersistenceUtils.instantiateFixture().copy(
                            competition = bettingGame.competition,
                            goalsHomeTeam = 2,
                            goalsAwayTeam = 1,
                            homeTeam = teams[2],
                            awayTeam = teams[3],
                            status = FixtureStatus.FINISHED),
                    PersistenceUtils.instantiateFixture().copy(
                            competition = bettingGame.competition,
                            goalsHomeTeam = 1,
                            goalsAwayTeam = 1,
                            homeTeam = teams[4],
                            awayTeam = teams[5],
                            status = FixtureStatus.FINISHED))

    private fun createTeams(competition: Competition, vararg teamNames: String): List<Team> =
            teamNames.map {
                PersistenceUtils.instantiateTeam().copy(
                        competition = competition, name = it, id = (teamNames.indexOf(it) + 1).toLong())
            }
}
