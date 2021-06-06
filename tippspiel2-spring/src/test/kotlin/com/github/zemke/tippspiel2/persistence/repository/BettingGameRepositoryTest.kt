package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import com.github.zemke.tippspiel2.test.util.PersistenceUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class BettingGameRepositoryTest {

    @Autowired
    private lateinit var bettingGameRepository: BettingGameRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    fun testSave() {
        val managedEntityCompetition = testEntityManager.persist(PersistenceUtils.instantiateCompetition())
        val managedEntityBettingGame = BettingGame(
                name = "Bobabel",
                competition = managedEntityCompetition
        )

        bettingGameRepository.save(managedEntityBettingGame)

        Assert.assertEquals(
                BettingGame(
                        id = managedEntityBettingGame.id,
                        name = "Bobabel",
                        competition = managedEntityCompetition,
                        created = managedEntityBettingGame.created,
                        invitationToken = managedEntityBettingGame.invitationToken
                ),
                managedEntityBettingGame
        )
    }
}
