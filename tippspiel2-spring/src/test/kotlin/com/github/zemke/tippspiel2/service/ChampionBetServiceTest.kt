package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.persistence.repository.ChampionBetRepository
import com.github.zemke.tippspiel2.test.util.PersistenceUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class ChampionBetServiceTest {

    @InjectMocks
    @Spy
    private lateinit var championBetService: ChampionBetService

    @Mock
    private lateinit var championBetRepository: ChampionBetRepository

    @Test
    fun getCompetitionChampionFromFixtures() {
        val fixtures = listOf(
                PersistenceUtils.instantiateFixture().copy(
                        matchday = 9,
                        goalsHomeTeam = 3,
                        goalsAwayTeam = 5,
                        status = FixtureStatus.TIMED
                ),
                PersistenceUtils.instantiateFixture().copy(
                        matchday = 2,
                        goalsHomeTeam = 3,
                        goalsAwayTeam = 5,
                        status = FixtureStatus.FINISHED
                ),
                PersistenceUtils.instantiateFixture().copy(
                        matchday = 23,
                        goalsHomeTeam = 1,
                        goalsAwayTeam = 3,
                        status = FixtureStatus.FINISHED
                )
        )

        Assert.assertEquals(
                fixtures[2].awayTeam,
                championBetService.getCompetitionChampionFromFixtures(fixtures, 23))
    }
}
