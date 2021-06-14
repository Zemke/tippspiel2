package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.test.util.PersistenceUtils
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner
import com.github.zemke.tippspiel2.test.util.JpaTest

@JpaTest
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
