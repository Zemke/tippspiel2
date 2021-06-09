package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import java.time.Instant

@DataTransferObject
data class FixtureDto(
        val id: Long,
        val date: Instant,
        val status: FixtureStatus,
        val matchday: Int,
        val goalsHomeTeam: Int?,
        val goalsAwayTeam: Int?,
        val homeTeam: TeamDto?,
        val awayTeam: TeamDto?,
        val competition: CompetitionDto
) {

    companion object {

        fun toDto(fixture: Fixture): FixtureDto = FixtureDto(
                fixture.id!!,
                fixture.date,
                fixture.status,
                fixture.matchday,
                fixture.goalsHomeTeam,
                fixture.goalsAwayTeam,
                fixture.homeTeam?.let { TeamDto.toDto(it) },
                fixture.awayTeam?.let { TeamDto.toDto(it) },
                CompetitionDto.toDto(fixture.competition)
        )
    }
}
