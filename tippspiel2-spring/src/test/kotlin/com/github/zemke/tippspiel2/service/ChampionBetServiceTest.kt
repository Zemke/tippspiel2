package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.ChampionBet
import com.github.zemke.tippspiel2.test.util.EmbeddedPostgresDataJpaTest
import com.github.zemke.tippspiel2.test.util.PersistenceUtils
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@EmbeddedPostgresDataJpaTest
@Import(ChampionBetService::class)
open class ChampionBetServiceTest {

    @Autowired
    private lateinit var championBetService: ChampionBetService

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    fun testSaveChampionBet() {
        val user = PersistenceUtils.createUser(testEntityManager)
        val community = testEntityManager.persistAndFlush(PersistenceUtils.instantiateCommunity()
                .copy(users = listOf(user)))
        val competition = testEntityManager.persistAndFlush(PersistenceUtils.instantiateCompetition())
        val team = testEntityManager.persistAndFlush(PersistenceUtils.instantiateTeam()
                .copy(competition = competition))
        val bettingGame = testEntityManager.persistAndFlush(PersistenceUtils.instantiateBettingGame()
                .copy(community = community, competition = competition))

        val unmanagedEntity = ChampionBet(
                id = null,
                bettingGame = bettingGame,
                modified = PersistenceUtils.now(),
                team = team,
                user = user
        )

        val actualEntity = championBetService.saveChampionBet(unmanagedEntity.copy())
        val expectedEntity = unmanagedEntity.copy(id = actualEntity.id)

        Assert.assertEquals(actualEntity, expectedEntity)
    }
}
