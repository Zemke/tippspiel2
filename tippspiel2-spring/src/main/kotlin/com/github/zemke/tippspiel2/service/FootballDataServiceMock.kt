package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.core.profile.Dev
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureWrappedListDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamWrappedListDto
import com.github.zemke.tippspiel2.view.util.JacksonUtils
import org.springframework.stereotype.Service

@Dev
@Service
class FootballDataServiceMock : FootballDataService {

    override fun requestCompetition(competitionId: Long): FootballDataCompetitionDto {
        return JacksonUtils.fromJson(javaClass.classLoader.getResourceAsStream("competition.json"))
    }

    override fun requestFixtures(competitionId: Long): FootballDataFixtureWrappedListDto {
        return JacksonUtils.fromJson(javaClass.classLoader.getResourceAsStream("fixtures.json"))
    }

    override fun requestTeams(competitionId: Long): FootballDataTeamWrappedListDto {
        return JacksonUtils.fromJson(javaClass.classLoader.getResourceAsStream("teams.json"))
    }
}
