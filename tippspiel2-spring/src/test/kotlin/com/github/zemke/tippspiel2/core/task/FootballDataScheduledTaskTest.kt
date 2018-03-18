package com.github.zemke.tippspiel2.core.task

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.Standing
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.service.CompetitionService
import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.service.FootballDataService
import com.github.zemke.tippspiel2.service.StandingService
import com.github.zemke.tippspiel2.test.util.PersistenceUtils
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureWrappedListDto
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Strict::class)
class FootballDataScheduledTaskTest {

    @Spy
    @InjectMocks
    private lateinit var footballDataScheduledTask: FootballDataScheduledTask

    @Mock
    private lateinit var footballDataService: FootballDataService

    @Mock
    private lateinit var competitionService: CompetitionService

    @Mock
    private lateinit var fixtureService: FixtureService

    @Mock
    private lateinit var standingService: StandingService

    @Test
    fun testExec() {
        val currentCompetition = PersistenceUtils.instantiateCompetition()

        Mockito
                .`when`(competitionService.findByCurrentTrue())
                .thenReturn(currentCompetition)

        var fixturesToAnswer = mutableListOf<Fixture>()
        Mockito
                .`when`(fixtureService.findFixturesByCompetitionAndManualFalse(Mockito.any(Competition::class.java)))
                .thenAnswer {
                    fixturesToAnswer = mutableListOf(
                            PersistenceUtils.instantiateFixture().copy(id = 1, competition = currentCompetition),
                            PersistenceUtils.instantiateFixture().copy(id = 2, competition = currentCompetition),
                            PersistenceUtils.instantiateFixture().copy(id = 3, competition = currentCompetition)
                    )
                    fixturesToAnswer.add(fixturesToAnswer[0].copy(id = 4, homeTeam = fixturesToAnswer[2].awayTeam))
                    fixturesToAnswer
                }

        var fixturesToAnswerFromApi = mutableListOf<Fixture>()
        Mockito
                .`when`(footballDataService.requestFixtures(Mockito.anyLong()))
                .thenAnswer {
                    fixturesToAnswerFromApi = mutableListOf(*fixturesToAnswer.toTypedArray())
                    fixturesToAnswerFromApi[0].goalsHomeTeam = fixturesToAnswerFromApi[0].goalsHomeTeam!! + 2
                    fixturesToAnswerFromApi[1].status = FixtureStatus.CANCELED

                    FootballDataFixtureWrappedListDto(
                            fixturesToAnswerFromApi.count(),
                            fixturesToAnswerFromApi.map { FootballDataFixtureDto.toDto(it) })
                }

        Mockito
                .`when`(fixtureService.saveMany(Mockito.anyList()))
                .thenAnswer {
                    Assert.assertArrayEquals(
                            fixturesToAnswerFromApi.toTypedArray(),
                            (it.getArgument(0) as List<Fixture>).toTypedArray())
                }

        Mockito
                .`when`(standingService.updateStandings(Mockito.any(Competition::class.java)))
                .thenAnswer({ Mockito.anyList<Standing>() })

        footballDataScheduledTask.exec()
    }
}
