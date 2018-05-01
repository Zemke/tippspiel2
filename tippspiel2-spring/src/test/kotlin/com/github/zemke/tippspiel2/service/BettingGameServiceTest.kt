package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.Community
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Standing
import com.github.zemke.tippspiel2.test.util.EmbeddedPostgresDataJpaTest
import com.github.zemke.tippspiel2.test.util.PersistenceUtils
import com.github.zemke.tippspiel2.view.util.JacksonUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import java.sql.Timestamp

@RunWith(SpringRunner::class)
@EmbeddedPostgresDataJpaTest
@Import(BettingGameService::class)
class BettingGameServiceTest {

    @Autowired
    private lateinit var bettingGameService: BettingGameService

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    fun testCreateBettingGame() {
        val community = Community(
                id = null,
                name = "Mah friends",
                users = listOf(
                        PersistenceUtils.createUser(testEntityManager),
                        PersistenceUtils.createUser(testEntityManager, "2")
                )
        )
        val competition = Competition(
                id = 1,
                caption = "World Cup 2018 Russia",
                league = "WC",
                year = "2018",
                currentMatchday = 1,
                numberOfMatchdays = 8,
                numberOfTeams = 32,
                numberOfGames = 64,
                lastUpdated = Timestamp(JacksonUtils.toDate("2018-01-10T14:10:08Z").time)
        )

        testEntityManager.entityManager.persist(community)
        testEntityManager.entityManager.persist(competition)

        val bettingGame = BettingGame(
                id = null,
                name = "Tipprunde",
                community = community,
                competition = competition
        )

        bettingGameService.createBettingGame(bettingGame)

        Assert.assertEquals(
                bettingGame,
                testEntityManager.entityManager.createQuery("select bg from BettingGame bg", BettingGame::class.java).singleResult)


        val standings = with(testEntityManager.entityManager.createQuery("select s from Standing s where s.bettingGame = :bettingGame", Standing::class.java)) {
            setParameter("bettingGame", bettingGame)
            resultList
        }

        Assert.assertEquals(standings.size, 2)

        Assert.assertEquals(
                listOf(Standing(
                        id = standings[0].id,
                        points = 0,
                        exactBets = 0,
                        goalDifferenceBets = 0,
                        winnerBets = 0,
                        wrongBets = 0,
                        missedBets = 0,
                        user = community.users[0],
                        bettingGame = bettingGame
                ), Standing(
                        id = standings[1].id,
                        points = 0,
                        exactBets = 0,
                        goalDifferenceBets = 0,
                        winnerBets = 0,
                        wrongBets = 0,
                        missedBets = 0,
                        user = community.users[1],
                        bettingGame = bettingGame
                )),
                standings
        )
    }
}
