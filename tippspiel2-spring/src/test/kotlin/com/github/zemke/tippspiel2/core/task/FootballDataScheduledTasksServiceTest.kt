package com.github.zemke.tippspiel2.core.task

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.persistence.repository.BettingGameRepository
import com.github.zemke.tippspiel2.persistence.repository.CompetitionRepository
import com.github.zemke.tippspiel2.persistence.repository.TeamRepository
import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.service.FootballDataScheduledTasksService
import com.github.zemke.tippspiel2.service.FootballDataService
import com.github.zemke.tippspiel2.service.StandingService
import com.github.zemke.tippspiel2.test.util.PersistenceUtils
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureWrappedListDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamWrappedListDto
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class FootballDataScheduledTasksServiceTest {

    @InjectMocks
    private lateinit var footballDataScheduledTasksService: FootballDataScheduledTasksService

    @Mock
    private lateinit var footballDataService: FootballDataService

    @Mock
    private lateinit var competitionRepository: CompetitionRepository

    @Mock
    private lateinit var teamRepository: TeamRepository

    @Mock
    private lateinit var fixtureService: FixtureService

    @Mock
    private lateinit var standingService: StandingService

    @Mock
    private lateinit var bettingGameRepository: BettingGameRepository

    @Test
    fun testRequestFixturesAndUpdateStandings() {
        val currentCompetition = mockCurrentCompetition()

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
                            .copy(goalsHomeTeam = 4)
                    fixturesToAnswerFromApi[1] = fixturesToAnswerFromApi[1]
                            .copy(status = FixtureStatus.CANCELED)
                    fixturesToAnswerFromApi.add(PersistenceUtils.instantiateFixture(currentCompetition)
                            .copy(id = 5, homeTeam = fixturesToAnswerFromApi[0].homeTeam, awayTeam = fixturesToAnswerFromApi[1].awayTeam))

                    FootballDataFixtureWrappedListDto(
                            fixturesToAnswerFromApi.count(),
                            fixturesToAnswerFromApi.map { FootballDataFixtureDto.toDto(it) })
                }

        Mockito
                .doAnswer {
                    val fixturesPassedToMethod = it.getArgument<List<Fixture>>(0)
                    Assert.assertEquals(
                            listOf(fixturesToAnswerFromApi[0], fixturesToAnswerFromApi[1], fixturesToAnswerFromApi[4]),
                            fixturesPassedToMethod)
                    fixturesPassedToMethod
                }
                .`when`(fixtureService).saveMany(Mockito.anyList<Fixture>())

        footballDataScheduledTasksService.requestFixturesAndUpdateStandings()
    }

    @Test
    fun testUpdateCurrentCompetitionWithItsTeamsCompetitionUpdate() {
        val currentCompetition = mockCurrentCompetition()

        Mockito
                .doReturn(FootballDataCompetitionDto.toDto(currentCompetition.copy(currentMatchday = currentCompetition.currentMatchday + 1)))
                .`when`(footballDataService).requestCompetition(currentCompetition.id)

        Mockito
                .doReturn(FootballDataTeamWrappedListDto(0, emptyList()))
                .`when`(footballDataService).requestTeams(currentCompetition.id)

        Mockito
                .doAnswer {
                    Assert.assertEquals(emptyList<Team>(), it.getArgument(0))
                    it.getArgument(0)
                }
                .`when`(teamRepository).saveAll(Mockito.anyList())

        footballDataScheduledTasksService.updateCurrentCompetitionWithItsTeams()

        Mockito
                .verify(competitionRepository).save(Mockito.any(Competition::class.java))
    }

    @Test
    fun testUpdateCurrentCompetitionWithItsTeamsUnchangedCompetition() {
        val currentCompetition = mockCurrentCompetition()

        Mockito
                .doReturn(FootballDataCompetitionDto.toDto(currentCompetition))
                .`when`(footballDataService).requestCompetition(currentCompetition.id)

        Mockito
                .doReturn(FootballDataTeamWrappedListDto(0, emptyList()))
                .`when`(footballDataService).requestTeams(currentCompetition.id)

        Mockito
                .doAnswer {
                    Assert.assertEquals(emptyList<Team>(), it.getArgument(0))
                    it.getArgument(0)
                }
                .`when`(teamRepository).saveAll(Mockito.anyList())

        footballDataScheduledTasksService.updateCurrentCompetitionWithItsTeams()

        Mockito
                .verify(competitionRepository, Mockito.never()).save(Mockito.any(Competition::class.java))
    }

    @Test
    fun testUpdateCurrentCompetitionWithItsTeamsTeams() {
        val currentCompetition = mockCurrentCompetition()

        Mockito
                .doReturn(FootballDataCompetitionDto.toDto(currentCompetition))
                .`when`(footballDataService).requestCompetition(currentCompetition.id)

        val teamsOfCurrentCompetition = listOf(
                Team(1, "Schokoladenbärenland", currentCompetition),
                Team(2, "Team2", currentCompetition),
                Team(3, "Team3", currentCompetition))

        val footballDataTeams = listOf(
                Team(2, "Team2", currentCompetition),
                Team(1, "Schokoladenbärenland", currentCompetition),
                Team(3, "Team3", currentCompetition))

        Mockito
                .doReturn(teamsOfCurrentCompetition)
                .`when`(teamRepository).findByCompetition(currentCompetition)

        Mockito
                .doReturn(FootballDataTeamWrappedListDto(3, footballDataTeams.map { FootballDataTeamDto.toDto(it) }))
                .`when`(footballDataService).requestTeams(currentCompetition.id)

        Mockito
                .doAnswer {
                    Assert.assertEquals(listOf(footballDataTeams[2]), it.getArgument(0))
                    it.getArgument(0)
                }
                .`when`(teamRepository).saveAll(Mockito.anyList())

        footballDataScheduledTasksService.updateCurrentCompetitionWithItsTeams()

        Mockito
                .verify(competitionRepository, Mockito.never()).save(Mockito.any(Competition::class.java))
    }

    private fun mockCurrentCompetition(): Competition {
        val currentCompetition = PersistenceUtils.instantiateCompetition()

        Mockito
                .doReturn(currentCompetition)
                .`when`(competitionRepository).findByCurrentTrue()
        return currentCompetition
    }
}
