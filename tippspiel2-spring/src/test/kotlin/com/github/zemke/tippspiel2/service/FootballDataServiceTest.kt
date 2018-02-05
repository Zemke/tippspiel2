package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.test.util.JacksonUtils
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
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
import org.springframework.web.client.RestTemplate

@RunWith(MockitoJUnitRunner::class)
class FootballDataServiceTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    @InjectMocks
    @Spy
    private lateinit var footballDataService: FootballDataService

    @Before
    fun before() = MockitoAnnotations.initMocks(this)

    @Test
    fun testRequestCompetition() {
        Mockito
                .`when`<FootballDataCompetitionDto>(restTemplate.getForObject(Mockito.anyString(), Mockito.any()))
                .thenReturn(JacksonUtils.fromJson(javaClass.classLoader.getResourceAsStream("competition.json")))

        Assert.assertEquals(
                footballDataService.requestCompetition(1),
                FootballDataCompetitionDto(467, "World Cup 2018 Russia", "WC", "2018", 1, 8, 32, 64, "2018-01-10T14:10:08Z"))
    }
}
