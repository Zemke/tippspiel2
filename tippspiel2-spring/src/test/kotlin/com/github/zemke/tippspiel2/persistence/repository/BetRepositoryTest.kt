package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.Bet
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.test.util.EmbeddedPostgresDataJpaTest
import com.github.zemke.tippspiel2.test.util.PersistenceUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@EmbeddedPostgresDataJpaTest
class BetRepositoryTest {

    @Autowired
    private lateinit var betRepository: BetRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    fun testSave() {
        val user = PersistenceUtils.createUser(testEntityManager)
        val bettingGame = PersistenceUtils.createBettingGame(testEntityManager, listOf(user))
        val homeTeam = testEntityManager.persistAndFlush(PersistenceUtils.instantiateTeam())
        val awayTeam = testEntityManager.persistAndFlush(PersistenceUtils.instantiateTeam().copy(id = 2))
        val fixture = testEntityManager.persistFlushFind(PersistenceUtils.instantiateFixture().copy(homeTeam = homeTeam, awayTeam = awayTeam))

        val unmanagedBetEntity = Bet(
                id = null,
                fixture = fixture,
                goalsAwayTeamBet = 2,
                goalsHomeTeamBet = 3,
                modified = PersistenceUtils.now(),
                user = user,
                bettingGame = bettingGame
        )

        val actualBetEntity = betRepository.save(unmanagedBetEntity.copy())
        val expectedBetEntity = unmanagedBetEntity.copy(id = actualBetEntity.id)

        Assert.assertEquals(actualBetEntity, expectedBetEntity)
    }

    @Test
    fun testFindByCompetitionAndFixtureStatus() {
        val user1 = PersistenceUtils.createUser(testEntityManager)
        val user2 = PersistenceUtils.createUser(testEntityManager, "2")
        val bettingGame = PersistenceUtils.createBettingGame(testEntityManager, listOf(user1, user2))
        val competition = testEntityManager.persistAndFlush(PersistenceUtils.instantiateCompetition().copy(id = 99))
        val homeTeam = testEntityManager.persistFlushFind(PersistenceUtils.instantiateTeam().copy(id = 1, competition = bettingGame.competition))
        val awayTeam = testEntityManager.persistFlushFind(PersistenceUtils.instantiateTeam().copy(id = 2, competition = bettingGame.competition))

        val fixtures = listOf(
                testEntityManager.persistAndFlush(
                        PersistenceUtils.instantiateFixture()
                                .copy(status = FixtureStatus.FINISHED, competition = bettingGame.competition, homeTeam = homeTeam, awayTeam = awayTeam)),
                testEntityManager.persistAndFlush(
                        PersistenceUtils.instantiateFixture()
                                .copy(status = FixtureStatus.TIMED, competition = bettingGame.competition, homeTeam = homeTeam, awayTeam = awayTeam)),
                testEntityManager.persistAndFlush(
                        PersistenceUtils.instantiateFixture()
                                .copy(status = FixtureStatus.FINISHED, competition = competition, homeTeam = homeTeam, awayTeam = awayTeam)),
                testEntityManager.persistAndFlush(
                        PersistenceUtils.instantiateFixture()
                                .copy(status = FixtureStatus.POSTPONED, competition = competition, homeTeam = homeTeam, awayTeam = awayTeam)),
                testEntityManager.persistAndFlush(
                        PersistenceUtils.instantiateFixture()
                                .copy(status = FixtureStatus.IN_PLAY, competition = bettingGame.competition, homeTeam = homeTeam, awayTeam = awayTeam)),
                testEntityManager.persistAndFlush(
                        PersistenceUtils.instantiateFixture()
                                .copy(status = FixtureStatus.FINISHED, competition = competition, homeTeam = homeTeam, awayTeam = awayTeam))
        )

        val bets = listOf(
                testEntityManager.persistFlushFind(
                        PersistenceUtils.instantiateBet()
                                .copy(bettingGame = bettingGame, user = bettingGame.community.users[0], fixture = fixtures[0])),
                testEntityManager.persistFlushFind(
                        PersistenceUtils.instantiateBet()
                                .copy(bettingGame = bettingGame, user = bettingGame.community.users[1], fixture = fixtures[0])),
                testEntityManager.persistFlushFind(
                        PersistenceUtils.instantiateBet()
                                .copy(bettingGame = bettingGame, user = bettingGame.community.users[0], fixture = fixtures[1])),
                testEntityManager.persistFlushFind(
                        PersistenceUtils.instantiateBet()
                                .copy(bettingGame = bettingGame, user = bettingGame.community.users[0], fixture = fixtures[2])),
                testEntityManager.persistFlushFind(
                        PersistenceUtils.instantiateBet()
                                .copy(bettingGame = bettingGame, user = bettingGame.community.users[0], fixture = fixtures[3])),
                testEntityManager.persistFlushFind(
                        PersistenceUtils.instantiateBet()
                                .copy(bettingGame = bettingGame, user = bettingGame.community.users[0], fixture = fixtures[4])),
                testEntityManager.persistFlushFind(
                        PersistenceUtils.instantiateBet()
                                .copy(bettingGame = bettingGame, user = bettingGame.community.users[0], fixture = fixtures[5]))
        )
        val actualBets = betRepository.findByCompetitionAndFixtureStatus(bettingGame.competition, FixtureStatus.FINISHED)

        Assert.assertArrayEquals(listOf(bets[0], bets[1]).toTypedArray(), actualBets.toTypedArray())
    }
}
