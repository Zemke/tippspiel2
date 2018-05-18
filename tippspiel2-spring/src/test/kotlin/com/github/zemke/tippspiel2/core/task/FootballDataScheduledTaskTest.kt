package com.github.zemke.tippspiel2.core.task

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

    // Add test with champion bet points. GH-30

    @Test
    fun testExec() {
        val currentCompetition = PersistenceUtils.instantiateCompetition()

        Mockito
                .doReturn(currentCompetition)
                .`when`(competitionService).findByCurrentTrue()

        var fixturesToAnswer = mutableListOf<Fixture>()
        Mockito
                .doAnswer {
                    fixturesToAnswer = mutableListOf(
                            PersistenceUtils.instantiateFixture(currentCompetition).copy(id = 1),
                            PersistenceUtils.instantiateFixture(currentCompetition).copy(id = 2),
                            PersistenceUtils.instantiateFixture(currentCompetition).copy(id = 3)
                    )
                    fixturesToAnswer.add(fixturesToAnswer[0].copy(id = 4, homeTeam = fixturesToAnswer[2].awayTeam))
                    fixturesToAnswer
                }
                .`when`(fixtureService).findFixturesByCompetitionAndManualFalse(currentCompetition)

        var fixturesToAnswerFromApi = mutableListOf<Fixture>()
        Mockito
                .`when`(footballDataService.requestFixtures(Mockito.anyLong()))
                .thenAnswer {
                    fixturesToAnswerFromApi = mutableListOf(*fixturesToAnswer.toTypedArray())
                    fixturesToAnswerFromApi[0] = fixturesToAnswerFromApi[0]
                            .copy(goalsHomeTeam = 3)
                    fixturesToAnswerFromApi[1].status = FixtureStatus.CANCELED

                    FootballDataFixtureWrappedListDto(
                            fixturesToAnswerFromApi.count(),
                            fixturesToAnswerFromApi.map { FootballDataFixtureDto.toDto(it) })
                }

        Mockito
                .doAnswer {
                    val fixturesPassedToMethod = it.getArgument<List<Fixture>>(0)
                    Assert.assertEquals(fixturesToAnswerFromApi, fixturesPassedToMethod)
                    fixturesPassedToMethod
                }
                .`when`(fixtureService).saveMany(Mockito.anyList<Fixture>())

        Mockito
                .doAnswer({ emptyList<Standing>() })
                .`when`(standingService).updateStandings(currentCompetition)

        footballDataScheduledTask.exec()
    }
}
