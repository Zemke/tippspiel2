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
import com.github.zemke.tippspiel2.persistence.repository.CompetitionRepository
import com.github.zemke.tippspiel2.persistence.repository.FixtureRepository
import com.github.zemke.tippspiel2.persistence.repository.StandingRepository
import com.github.zemke.tippspiel2.test.util.PersistenceUtils
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class StandingServiceTest {

    @InjectMocks
    private lateinit var standingService: StandingService

    @Mock
    private lateinit var betRepository: BetRepository

    @Mock
    private lateinit var standingRepository: StandingRepository

    @Mock
    private lateinit var fixtureRepository: FixtureRepository

    @Mock
    private lateinit var championBetService: ChampionBetService

    @Mock
    private lateinit var competitionRepository: CompetitionRepository

    @Test
    fun testUpdateStandings() {
        val bettingGame = PersistenceUtils.instantiateBettingGame()

        val user1 = PersistenceUtils.instantiateUser("1").copy(bettingGames = listOf(bettingGame))
        val user2 = PersistenceUtils.instantiateUser("2").copy(bettingGames = listOf(bettingGame))
        val teams = createTeams(bettingGame.competition, "Russia", "Norway", "England", "Italy", "Ireland", "Iceland", "Poland", "Spain")
        val fixtures = createFixtures(bettingGame, teams)

        Mockito
                .`when`(betRepository.findByBettingGame(bettingGame))
                .thenReturn(createBets(fixtures, bettingGame, user1, user2))

        Mockito
                .`when`(standingRepository.findByBettingGame(bettingGame))
                .thenReturn(createStandings(user1, bettingGame, user2))

        Mockito
                .`when`(fixtureRepository.findFixturesByCompetition(bettingGame.competition))
                .thenReturn(fixtures)

        Mockito
                .`when`(standingRepository.saveAll(Mockito.anyList<Standing>()))
                .thenAnswer { it.getArgument(0) }

        val standingsActual = standingService.updateStandings(bettingGame)

        Mockito
                .verify(competitionRepository, Mockito.never()).save(Mockito.any(Competition::class.java))

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

    @Test
    fun testUpdateStandingsWithChampionBet() {
        val bettingGame = PersistenceUtils.instantiateBettingGame()
        val user1 = PersistenceUtils.instantiateUser("1").copy(id = 1, bettingGames = listOf(bettingGame))
        val user2 = PersistenceUtils.instantiateUser("2").copy(id = 2, bettingGames = listOf(bettingGame))

        val teams = createTeams(bettingGame.competition, "Russia", "Norway", "England", "Italy", "Ireland", "Iceland", "Poland", "Spain")
        val fixtures = createFixtures(bettingGame, teams)
        val competitionChampion = fixtures[0].homeTeam!!

        Mockito
                .`when`(betRepository.findByBettingGame(bettingGame))
                .thenReturn(createBets(fixtures, bettingGame, user1, user2))

        Mockito
                .`when`(standingRepository.findByBettingGame(bettingGame))
                .thenReturn(createStandings(user1, bettingGame, user2))

        Mockito
                .`when`(fixtureRepository.findFixturesByCompetition(bettingGame.competition))
                .thenReturn(fixtures)

        Mockito
                .`when`(standingRepository.saveAll(Mockito.anyList<Standing>()))
                .thenAnswer { it.getArgument(0) }

        // TODO GH-84 Test champion bet.

        val standingsActual = standingService.updateStandings(bettingGame)

        Assert.assertEquals(
                listOf(
                        createStandings(user1, bettingGame, user2)[0].copy(
                                exactBets = 2,
                                goalDifferenceBets = 1,
                                winnerBets = 0,
                                wrongBets = 0,
                                missedBets = 0,
                                points = 13 // TODO GH-84 + 10 for champion bet
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

    @Test
    fun testUpdateStandingsWithChangedFixture() {
        val bettingGame = PersistenceUtils.instantiateBettingGame()

        val user1 = PersistenceUtils.instantiateUser("1").copy(bettingGames = listOf(bettingGame))
        val user2 = PersistenceUtils.instantiateUser("2").copy(bettingGames = listOf(bettingGame))
        val teams = createTeams(bettingGame.competition, "Russia", "Norway", "England", "Italy", "Ireland", "Iceland", "Poland", "Spain")
        val fixtures = createFixtures(bettingGame, teams)

        Mockito
                .`when`(betRepository.findByBettingGame(bettingGame))
                .thenReturn(createBets(fixtures, bettingGame, user1, user2))

        Mockito
                .`when`(standingRepository.findByBettingGame(bettingGame))
                .thenReturn(createStandings(user1, bettingGame, user2))

        Mockito
                .`when`(fixtureRepository.findFixturesByCompetition(bettingGame.competition))
                .thenReturn(fixtures)

        Mockito
                .`when`(standingRepository.saveAll(Mockito.anyList<Standing>()))
                .thenAnswer { it.getArgument(0) }

        val standingsActual = standingService.updateStandings(bettingGame)

        Mockito
                .verify(competitionRepository, Mockito.never()).save(Mockito.any(Competition::class.java))

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

        fixtures[0] = fixtures[0].copy(goalsHomeTeam = 3, goalsAwayTeam = 1)

        Mockito
                .`when`(betRepository.findByBettingGame(bettingGame))
                .thenReturn(createBets(fixtures, bettingGame, user1, user2))

        Mockito
                .`when`(fixtureRepository.findFixturesByCompetition(bettingGame.competition))
                .thenReturn(fixtures)

        val standingsActual2 = standingService.updateStandings(bettingGame)

        Assert.assertEquals(
                listOf(
                        createStandings(user1, bettingGame, user2)[0].copy(
                                exactBets = 1,
                                goalDifferenceBets = 2,
                                winnerBets = 0,
                                wrongBets = 0,
                                missedBets = 0,
                                points = 11
                        ),
                        createStandings(user1, bettingGame, user2)[1].copy(
                                exactBets = 0,
                                goalDifferenceBets = 0,
                                winnerBets = 1,
                                wrongBets = 1,
                                missedBets = 1,
                                points = 1
                        )),
                standingsActual2)
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

    private fun createFixtures(bettingGame: BettingGame, teams: List<Team>): MutableList<Fixture> =
            mutableListOf(
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
