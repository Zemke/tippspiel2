package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Bet
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
@Import(BetService::class)
open class BetServiceTest {

    @Autowired
    private lateinit var betService: BetService

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    fun testSave() {
        val competition = testEntityManager.persistAndFlush(PersistenceUtils.instantiateCompetition())
        testEntityManager.persistAndFlush(PersistenceUtils.instantiateTeam())
        testEntityManager.persistAndFlush(PersistenceUtils.instantiateTeam()
                .copy(id = 2))
        val fixture = testEntityManager.persistFlushFind(PersistenceUtils.instantiateFixture())
        val user = PersistenceUtils.createUser(testEntityManager)
        val community = testEntityManager.persistFlushFind(PersistenceUtils.instantiateCommunity()
                .copy(users = listOf(user)))
        val bettingGame = testEntityManager.persistFlushFind(PersistenceUtils.instantiateBettingGame()
                .copy(competition = competition, community = community))

        val unmanagedBetEntity = Bet(
                id = null,
                fixture = fixture,
                goalsAwayTeamBet = 2,
                goalsHomeTeamBet = 3,
                modified = PersistenceUtils.now(),
                user = user,
                bettingGame = bettingGame
        )

        val actualBetEntity = betService.save(unmanagedBetEntity)
        val expectedBetEntity = unmanagedBetEntity.copy(id = actualBetEntity!!.id)

        Assert.assertEquals(actualBetEntity, expectedBetEntity)
    }
}
