package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.core.properties.FootballDataProperties
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.test.util.JacksonUtils
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureResultDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureWrappedListDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamWrappedListDto
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.hateoas.Resource
import org.springframework.web.client.RestTemplate

@RunWith(MockitoJUnitRunner::class)
class FootballDataServiceImplTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    @Mock
    private lateinit var footballDataProperties: FootballDataProperties

    @InjectMocks
    @Spy
    private lateinit var footballDataServiceImpl: FootballDataServiceImpl

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)

        Mockito
                .`when`(footballDataProperties.apiToken)
                .thenReturn("mockApiToken")

        Mockito
                .`when`(footballDataProperties.apiTokenHeader)
                .thenReturn("mockApiTokenHeader")
    }

    @Test
    fun testRequestCompetition() {
        mockRequestResponse<FootballDataCompetitionDto>("competition.json")

        Assert.assertEquals(
                footballDataServiceImpl.requestCompetition(1),
                FootballDataCompetitionDto(467, "World Cup 2018 Russia", "WC", "2018", 1, 8, 32, 64, JacksonUtils.toDate("2018-01-10T14:10:08Z")))
    }

    @Test
    fun testRequestFixtures() {
        mockRequestResponse<FootballDataFixtureWrappedListDto>("fixtures.json")

        Assert.assertEquals(
                footballDataServiceImpl.requestFixtures(1),
                FootballDataFixtureWrappedListDto(2, listOf(Resource(FootballDataFixtureDto(
                        date = JacksonUtils.toDate("2018-06-14T15:00:00Z"),
                        status = FixtureStatus.TIMED,
                        matchday = 1,
                        homeTeamName = "Russia",
                        awayTeamName = "Saudi Arabia",
                        result = FootballDataFixtureResultDto(null, null),
                        odds = null
                )), Resource(FootballDataFixtureDto(
                        date = JacksonUtils.toDate("2018-06-15T12:00:00Z"),
                        status = FixtureStatus.TIMED,
                        matchday = 1,
                        homeTeamName = "Egypt",
                        awayTeamName = "Uruguay",
                        result = FootballDataFixtureResultDto(null, null),
                        odds = null
                )))))
    }

    @Test
    fun testRequestTeams() {
        mockRequestResponse<FootballDataTeamWrappedListDto>("teams.json")

        footballDataServiceImpl.requestTeams(1);

        Assert.assertEquals(
                footballDataServiceImpl.requestTeams(1),
                FootballDataTeamWrappedListDto(2, listOf(Resource(FootballDataTeamDto(
                        name = "Russia",
                        code = "RUS",
                        shortName = null,
                        squadMarketValue = null,
                        crestUrl = "https://upload.wikimedia.org/wikipedia/en/f/f3/Flag_of_Russia.svg"
                )), Resource(FootballDataTeamDto(
                        name = "Saudi Arabia",
                        code = null,
                        shortName = null,
                        squadMarketValue = null,
                        crestUrl = null
                )))))
    }

    private inline fun <reified T : Any> mockRequestResponse(responseFile: String) {
        Mockito
                .`when`<T>(restTemplate.getForObject(Mockito.anyString(), Mockito.any()))
                .thenReturn(JacksonUtils.fromJson(javaClass.classLoader.getResourceAsStream(responseFile)))
    }
}
