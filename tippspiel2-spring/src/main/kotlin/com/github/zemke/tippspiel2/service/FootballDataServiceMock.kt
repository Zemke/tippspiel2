package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.core.profile.Dev
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import org.springframework.stereotype.Service

@Dev
@Service
class FootballDataServiceMock : FootballDataService {

    override fun requestCompetition(competitionId: Long): FootballDataCompetitionDto {
        return FootballDataCompetitionDto(
                id = 467,
                caption = "World Cup 2018 Russia",
                league = "WC",
                year = "2018",
                currentMatchday = 1,
                numberOfMatchdays = 8,
                numberOfTeams = 32,
                numberOfGames = 64,
                lastUpdated = "2018-01-10T14:10:08Z"
        )
    }
}
