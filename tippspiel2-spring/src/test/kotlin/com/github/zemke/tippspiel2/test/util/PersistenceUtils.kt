package com.github.zemke.tippspiel2.test.util

import com.github.zemke.tippspiel2.persistence.model.Bet
import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.ChampionBet
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.model.User
import com.github.zemke.tippspiel2.persistence.model.embeddable.FullName
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.sql.Timestamp
import java.util.*


object PersistenceUtils {

    /**
     * Create a John Doe with his attributes appended with `appendToAttribute` to distinguish multiple John Does. :)
     */
    fun createUser(testEntityManager: TestEntityManager, bettingGames: List<BettingGame>, appendToAttribute: String = "1"): User =
            testEntityManager.persist(instantiateUser(appendToAttribute))

    fun createBettingGame(testEntityManager: TestEntityManager): BettingGame =
            testEntityManager.persistAndFlush(instantiateBettingGame().copy(
                    competition = testEntityManager.persistAndFlush(instantiateCompetition())))

    fun instantiateUser(appendToAttribute: String = "1"): User =
            User(
                    fullName = FullName("Jon$appendToAttribute", "Doe$appendToAttribute"),
                    email = "jon@doe.com$appendToAttribute",
                    password = "jondoe$appendToAttribute",
                    roles = emptyList())

    fun instantiateCompetition(): Competition =
            Competition(
                    id = 1,
                    caption = "1",
                    currentMatchday = 1,
                    lastUpdated = now(),
                    league = "ZC",
                    numberOfGames = 1,
                    numberOfMatchdays = 1,
                    numberOfTeams = 1,
                    year = "2018",
                    current = true,
                    championBetAllowed = true,
                    champion = null
            )

    fun instantiateTeam(competition: Competition = instantiateCompetition()): Team =
            Team(
                    id = (100..999).random(),
                    competition = competition,
                    name = "1",
                    squadMarketValue = null
            )

    fun instantiateFixture(competition: Competition = instantiateCompetition()): Fixture =
            Fixture(
                    id = (100..999).random(),
                    date = now(),
                    status = FixtureStatus.TIMED,
                    awayTeam = instantiateTeam(competition),
                    homeTeam = instantiateTeam(competition).copy(id = 2),
                    competition = competition,
                    goalsAwayTeam = 2,
                    goalsHomeTeam = 3,
                    matchday = 1,
                    odds = null
            )

    fun instantiateBettingGame(): BettingGame =
            BettingGame(
                    name = "Zemke Tipprunde",
                    competition = instantiateCompetition(),
                    created = now()
            )

    fun instantiateBet(): Bet =
            Bet(
                    id = null,
                    user = instantiateUser(),
                    modified = now(),
                    bettingGame = instantiateBettingGame(),
                    goalsHomeTeamBet = 1,
                    goalsAwayTeamBet = 1,
                    fixture = instantiateFixture()
            )

    fun instantiateChampionBet(bettingGame: BettingGame = instantiateBettingGame(), user: User = instantiateUser(),
                               team: Team = instantiateTeam()): ChampionBet =
            ChampionBet(
                    id = null,
                    bettingGame = bettingGame,
                    user = user,
                    modified = now(),
                    team = team
            )

    fun now() = Timestamp(Date().time)

    private fun ClosedRange<Int>.random() =
            (Random().nextInt(endInclusive - start) + start).toLong()
}
