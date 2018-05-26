package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.ChampionBet
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
class ChampionBetRepositoryTest {

    @Autowired
    private lateinit var championBetRepository: ChampionBetRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    fun testSave() {
        val competition = testEntityManager.persistAndFlush(PersistenceUtils.instantiateCompetition())
        val team = testEntityManager.persistAndFlush(PersistenceUtils.instantiateTeam()
                .copy(competition = competition))
        val bettingGame = testEntityManager.persistAndFlush(PersistenceUtils.instantiateBettingGame()
                .copy(competition = competition))
        val user = PersistenceUtils.createUser(testEntityManager, listOf(bettingGame))

        val unmanagedEntity = ChampionBet(
                id = null,
                bettingGame = bettingGame,
                modified = PersistenceUtils.now(),
                team = team,
                user = user
        )

        val actualEntity = championBetRepository.save(unmanagedEntity.copy())
        val expectedEntity = unmanagedEntity.copy(id = actualEntity.id)

        Assert.assertEquals(actualEntity, expectedEntity)
    }

    @Test
    fun testFindByBettingGameAndTeam() {
        val competition = testEntityManager.persist(PersistenceUtils.instantiateCompetition()
                .copy(id = 99))
        val team1 = testEntityManager.persist(PersistenceUtils.instantiateTeam()
                .copy(competition = competition))
        val team2 = testEntityManager.persist(PersistenceUtils.instantiateTeam()
                .copy(competition = competition))
        val bettingGame = testEntityManager.persist(PersistenceUtils.instantiateBettingGame()
                .copy(competition = competition))

        val user1 = PersistenceUtils.createUser(testEntityManager, listOf(bettingGame))
        val user2 = PersistenceUtils.createUser(testEntityManager, listOf(bettingGame))
        val user3 = PersistenceUtils.createUser(testEntityManager, listOf(bettingGame))

        val championBet1 = testEntityManager.persist(PersistenceUtils.instantiateChampionBet(bettingGame, user1, team1))
        testEntityManager.persist(PersistenceUtils.instantiateChampionBet(bettingGame, user2, team2))
        val championBet2 = testEntityManager.persist(PersistenceUtils.instantiateChampionBet(bettingGame, user3, team1))

        testEntityManager.flush()

        Assert.assertEquals(
                listOf(championBet1, championBet2),
                championBetRepository.findByBettingGameAndTeam(bettingGame, team1))
    }
}
