package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.Bet
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import com.github.zemke.tippspiel2.test.util.PersistenceUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner
import java.time.Instant

@RunWith(SpringRunner::class)
@DataJpaTest
class BetRepositoryTest {

    @Autowired
    private lateinit var betRepository: BetRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    fun testSave() {
        val bettingGame = PersistenceUtils.createBettingGame(testEntityManager)
        val user = PersistenceUtils.createUser(testEntityManager, listOf(bettingGame))
        val homeTeam = testEntityManager.persistAndFlush(PersistenceUtils.instantiateTeam())
        val awayTeam = testEntityManager.persistAndFlush(PersistenceUtils.instantiateTeam().copy(id = 2))
        val fixture = testEntityManager.persistFlushFind(PersistenceUtils.instantiateFixture().copy(homeTeam = homeTeam, awayTeam = awayTeam))

        val unmanagedBetEntity = Bet(
                id = null,
                fixture = fixture,
                goalsAwayTeamBet = 2,
                goalsHomeTeamBet = 3,
                modified = Instant.now(),
                user = user,
                bettingGame = bettingGame
        )

        val actualBetEntity = betRepository.save(unmanagedBetEntity.copy())
        val expectedBetEntity = unmanagedBetEntity.copy(id = actualBetEntity.id)

        Assert.assertEquals(actualBetEntity, expectedBetEntity)
    }
}
