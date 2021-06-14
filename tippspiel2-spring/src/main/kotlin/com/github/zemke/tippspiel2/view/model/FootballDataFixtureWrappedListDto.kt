package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import java.time.Instant

@DataTransferObject
data class FootballDataFixtureWrappedListDto(
        @JsonProperty("count") var count: Int,
        @JsonProperty("matches") var matches: List<FootballDataFixtureDto>
)

@DataTransferObject
data class FootballDataFixtureDto(
        @JsonProperty("id") var id: Long,
        @JsonProperty("homeTeam") var homeTeam: FootballDataFixtureTeamDto?,
        @JsonProperty("awayTeam") var awayTeam: FootballDataFixtureTeamDto?,
        @JsonProperty("score") var score: FootballDataFixtureResultDto?,
        @JsonProperty("matchday") var matchday: Int,
        @JsonProperty("status") var status: FixtureStatus,
        @JsonProperty("utcDate") var utcDate: Instant,
) {

    companion object {

        fun fromDto(dto: FootballDataFixtureDto, teams: List<Team>, competition: Competition): Fixture = Fixture(
                id = dto.id,
                date = dto.utcDate,
                status = dto.status,
                matchday = dto.matchday,
                goalsHomeTeam = dto.score?.fullTime?.homeTeam,
                goalsAwayTeam = dto.score?.fullTime?.awayTeam,
                homeTeam = dto.homeTeam?.let { team -> teams.find { it.id == team.id } },
                awayTeam = dto.awayTeam?.let { team -> teams.find { it.id == team.id } },
                competition = competition
        )

        fun toDto(fixture: Fixture) = FootballDataFixtureDto(
                id = fixture.id!!,
                homeTeam = fixture.homeTeam?.let { FootballDataFixtureTeamDto(fixture.homeTeam?.id!!, fixture.homeTeam?.name) },
                awayTeam = fixture.awayTeam?.let { FootballDataFixtureTeamDto(fixture.awayTeam?.id!!, fixture.awayTeam?.name) },
                score = FootballDataFixtureResultDto.toDto(
                    goalsHomeTeam = fixture.goalsHomeTeam,
                    goalsAwayTeam = fixture.goalsAwayTeam),
                matchday = fixture.matchday,
                status = fixture.status,
                utcDate = fixture.date,
        )
    }
}

@DataTransferObject
data class FootballDataFixtureResultDto(
        @JsonProperty("fullTime") var fullTime: FootballDataFixtureFullTimeResultDto,
) {

    companion object {

        fun toDto(goalsHomeTeam: Int?, goalsAwayTeam: Int?) =
            FootballDataFixtureResultDto(FootballDataFixtureFullTimeResultDto(homeTeam = goalsHomeTeam, awayTeam = goalsAwayTeam))
    }
}

@DataTransferObject
data class FootballDataFixtureFullTimeResultDto(
        @JsonProperty("homeTeam") var homeTeam: Int?,
        @JsonProperty("awayTeam") var awayTeam: Int?,
) {

    constructor(): this(null, null)
}

@DataTransferObject
data class FootballDataFixtureTeamDto(
    @JsonProperty("id") var id: Long?,
    @JsonProperty("name") var name: String?,
)

