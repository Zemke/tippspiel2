package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import java.util.*

@DataTransferObject
data class FixtureDto(
        val id: Long,
        val date: Date,
        val status: FixtureStatus,
        val matchday: Int,
        val odds: Double,
        val goalsHomeTeam: Int,
        val goalsAwayTeam: Int,
        val homeTeam: TeamDto,
        val awayTeam: TeamDto,
        val competition: CompetitionDto
) {

    companion object {

        fun map(fixture: Fixture): FixtureDto = FixtureDto(
                fixture.id!!,
                fixture.date,
                fixture.status,
                fixture.matchday,
                fixture.odds!!,
                fixture.goalsHomeTeam!!,
                fixture.goalsAwayTeam!!,
                TeamDto.map(fixture.homeTeam),
                TeamDto.map(fixture.awayTeam),
                CompetitionDto.map(fixture.competition)
        )
    }
}
