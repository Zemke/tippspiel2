package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import java.sql.Timestamp
import java.util.*

@DataTransferObject
data class FootballDataFixtureWrappedListDto(
        @JsonProperty("count") var count: Int,
        @JsonProperty("fixtures") var fixtures: List<FootballDataFixtureDto>
)

@DataTransferObject
data class FootballDataFixtureDto(
        @JsonProperty("id") var id: Long,
        @JsonProperty("homeTeamName") var homeTeamName: String,
        @JsonProperty("awayTeamName") var awayTeamName: String,
        @JsonProperty("result") var result: FootballDataFixtureResultDto,
        @JsonProperty("matchday") var matchday: Int,
        @JsonProperty("status") var status: FixtureStatus,
        @JsonProperty("date") var date: Date?,
        @JsonProperty("odds") var odds: Double?,
        @JsonProperty("homeTeamId") var homeTeamId: Long,
        @JsonProperty("awayTeamId") var awayTeamId: Long,
        @JsonProperty("competitionId") var competitionId: Long
) {
    companion object {

        fun fromDto(dto: FootballDataFixtureDto, teams: List<Team>, competition: Competition): Fixture = Fixture(
                id = dto.id,
                date = Timestamp(dto.date?.time!!),
                status = dto.status,
                matchday = dto.matchday,
                odds = dto.odds,
                goalsHomeTeam = dto.result.goalsHomeTeam,
                goalsAwayTeam = dto.result.goalsAwayTeam,
                homeTeam = teams.find { it.id == dto.homeTeamId }!!,
                awayTeam = teams.find { it.id == dto.awayTeamId }!!,
                competition = competition
        )
    }
}

@DataTransferObject
data class FootballDataFixtureResultDto(
        @JsonProperty("goalsHomeTeam") var goalsHomeTeam: Int?,
        @JsonProperty("goalsAwayTeam") var goalsAwayTeam: Int?
)
