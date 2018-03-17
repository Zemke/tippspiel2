package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.Bet
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
open class BetRepositoryTest {

    @Autowired
    private lateinit var betRepository: BetRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    fun testSave() {
        val user = PersistenceUtils.createUser(testEntityManager)
        val bettingGame = PersistenceUtils.createBettingGame(testEntityManager, listOf(user))
        testEntityManager.persistAndFlush(PersistenceUtils.instantiateTeam())
        testEntityManager.persistAndFlush(PersistenceUtils.instantiateTeam()
                .copy(id = 2))
        val fixture = testEntityManager.persistFlushFind(PersistenceUtils.instantiateFixture())

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
}
