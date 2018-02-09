package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.core.profile.Dev
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureWrappedListDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamWrappedListDto
import org.springframework.stereotype.Service
import java.util.*

@Dev
@Service
class FootballDataServiceMock : FootballDataService {

    // TODO
    // Generally speaking, it makes sense to have some test here. Test data that is as if we were amid a tournament.
    // Therefor it would also be important to have times---i.e. of fixtures---adapt to the current time.
    // It's just nice for development especially for the UI. Actually there's no other way of developing the UI
    // or doing any kind of manual tests.

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
                lastUpdated = GregorianCalendar(2018, 0, 10, 15, 10, 8).time
        )
    }

    override fun requestFixtures(competitionId: Long): FootballDataFixtureWrappedListDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestTeams(competitionId: Long): FootballDataTeamWrappedListDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
