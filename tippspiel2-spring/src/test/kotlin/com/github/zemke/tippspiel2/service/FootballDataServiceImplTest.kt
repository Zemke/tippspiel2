package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.core.properties.FootballDataProperties
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.view.model.FootbalDataCompetitionCurrentSeasonDto
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureResultDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureTeamDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureWrappedListDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamWrappedListDto
import com.github.zemke.tippspiel2.view.util.JacksonUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.web.client.RestTemplate

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class FootballDataServiceImplTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    @Mock
    private lateinit var footballDataProperties: FootballDataProperties

    @InjectMocks
    @Spy
    private lateinit var footballDataServiceImpl: FootballDataServiceImpl

    @Test
    fun testRequestCompetition() {
        mockRequestResponse<FootballDataCompetitionDto>("competition.json")
        val dto = FootballDataCompetitionDto(
            id = 467,
            name = "World Cup 2018 Russia",
            code = "WC",
            numberOfAvailableSeasons = 8,
            currentSeason = FootbalDataCompetitionCurrentSeasonDto(
                currentMatchday = 1,
                startDate = LocalDate.of(2018, 1, 8)),
            lastUpdated = LocalDateTime.of(2018, 1, 10, 14, 10).toInstant(ZoneOffset.UTC),
        )
        Assert.assertEquals(footballDataServiceImpl.requestCompetition(1), dto)

    }

    @Test
    fun testRequestFixtures() {
        mockRequestResponse<FootballDataFixtureWrappedListDto>("fixtures.json")

        Assert.assertEquals(
                footballDataServiceImpl.requestFixtures(1),
                FootballDataFixtureWrappedListDto(2, listOf(FootballDataFixtureDto(
                        utcDate = LocalDateTime.of(2018, 6, 14, 15, 0).toInstant(ZoneOffset.UTC),
                        matchday = 1,
                        competitionId = 467,
                        status = FixtureStatus.SCHEDULED,
                        id = 165069,
                        homeTeam = FootballDataFixtureTeamDto(id = 801, name = "Russia"),
                        awayTeam = FootballDataFixtureTeamDto(id = 808, name = "Saudi Arabia"),
                        score = null,
                ), FootballDataFixtureDto(
                        utcDate = LocalDateTime.of(2018, 6, 15, 12, 0).toInstant(ZoneOffset.UTC),
                        matchday = 1,
                        competitionId = 467,
                        id = 165084,
                        status = FixtureStatus.SCHEDULED,
                        homeTeam = FootballDataFixtureTeamDto(id = 758, name = "Egypt"),
                        awayTeam = FootballDataFixtureTeamDto(id = 825, name = "Uruguay"),
                        score = null,
                ))))
    }

    @Test
    fun testRequestTeams() {
        mockRequestResponse<FootballDataTeamWrappedListDto>("teams.json")

        footballDataServiceImpl.requestTeams(1)

        Assert.assertEquals(
                footballDataServiceImpl.requestTeams(1),
                FootballDataTeamWrappedListDto(2, listOf(FootballDataTeamDto(
                        name = "Russia",
                        id = 808,
                        crestUrl = "https://upload.wikimedia.org/wikipedia/en/f/f3/Flag_of_Russia.svg",
                        shortName = null
                ), FootballDataTeamDto(
                        crestUrl = null,
                        shortName = null,
                        id = 801,
                        name = "Saudi Arabia"
                ), FootballDataTeamDto(
                        crestUrl = null,
                        shortName = null,
                        id = 825,
                        name = "Egypt"
                ), FootballDataTeamDto(
                        crestUrl = null,
                        shortName = null,
                        id = 758,
                        name = "Uruguay"
                ))))
    }

    private inline fun <reified T : Any> mockRequestResponse(responseFile: String) {
        Mockito
                .`when`<T>(restTemplate.getForObject(Mockito.anyString(), Mockito.any()))
                .thenReturn(JacksonUtils.fromJson(javaClass.classLoader.getResourceAsStream(responseFile)))
    }
}
