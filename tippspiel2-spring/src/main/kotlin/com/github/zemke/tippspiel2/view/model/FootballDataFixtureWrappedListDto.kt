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
    @JsonProperty("count") val count: Int,
    @JsonProperty("matches") val matches: List<FootballDataFixtureDto>
)

@DataTransferObject
data class FootballDataFixtureDto(
    @JsonProperty("id") val id: Long,
    @JsonProperty("homeTeam") val homeTeam: FootballDataFixtureTeamDto?,
    @JsonProperty("awayTeam") val awayTeam: FootballDataFixtureTeamDto?,
    @JsonProperty("score") val score: FootballDataFixtureResultDto?,
    @JsonProperty("matchday") val matchday: Int?,
    @JsonProperty("stage") val stage: String?,
    @JsonProperty("group") val group: String?,
    @JsonProperty("status") val status: FixtureStatus,
    @JsonProperty("utcDate") val utcDate: Instant,
) {

    companion object {

        fun fromDto(dto: FootballDataFixtureDto, teams: List<Team>, competition: Competition) = Fixture(
            id = dto.id,
            date = dto.utcDate,
            status = dto.status,
            matchday = dto.matchday,
            goalsHomeTeam = dto.score?.regularTime?.homeTeam,
            goalsAwayTeam = dto.score?.regularTime?.awayTeam,
            homeTeam = dto.homeTeam?.let { team -> teams.find { it.id == team.id } },
            awayTeam = dto.awayTeam?.let { team -> teams.find { it.id == team.id } },
            stage = dto.stage,
            group = dto.group,
            competition = competition,
        )

        fun toDto(fixture: Fixture) = FootballDataFixtureDto(
            id = fixture.id!!,
            homeTeam = fixture.homeTeam?.let { FootballDataFixtureTeamDto(it.id, it.name) },
            awayTeam = fixture.awayTeam?.let { FootballDataFixtureTeamDto(it.id, it.name) },
            score = FootballDataFixtureResultDto.ofRegularTime(
                homeTeam = fixture.goalsHomeTeam,
                awayTeam = fixture.goalsAwayTeam
            ),
            matchday = fixture.matchday,
            status = fixture.status,
            utcDate = fixture.date,
            stage = fixture.stage,
            group = fixture.group,
        )
    }
}

@DataTransferObject
data class FootballDataFixtureResultDto(
    @JsonProperty("fullTime") val fullTime: FootballDataFixtureScoreDto,
    @JsonProperty("extraTime") val extraTime: FootballDataFixtureScoreDto,
    @JsonProperty("halfTime") val halfTime: FootballDataFixtureScoreDto,
    @JsonProperty("penalties") val penalties: FootballDataFixtureScoreDto,
) {
    var regularTime: FootballDataFixtureScoreDto

    init {
        regularTime = if (penalties.nothing() && extraTime.nothing()) {
            FootballDataFixtureScoreDto(homeTeam = fullTime.homeTeam, awayTeam = fullTime.awayTeam)
        } else if (fullTime.nothing() && extraTime.nothing() && halfTime.nothing() && penalties.nothing()) {
            FootballDataFixtureScoreDto()
        } else {
            FootballDataFixtureScoreDto(
                homeTeam = (fullTime.homeTeam ?: 0) - (penalties.homeTeam ?: 0) - (extraTime.homeTeam ?: 0),
                awayTeam = (fullTime.awayTeam ?: 0) - (penalties.awayTeam ?: 0) - (extraTime.awayTeam ?: 0),
            )
        }
    }

    private constructor(regularTime: FootballDataFixtureScoreDto) : this(FootballDataFixtureScoreDto(), FootballDataFixtureScoreDto(), FootballDataFixtureScoreDto(), FootballDataFixtureScoreDto()) {
        this.regularTime = regularTime
    }

    companion object {

        fun ofRegularTime(homeTeam: Int?, awayTeam: Int?) =
            FootballDataFixtureResultDto(
                regularTime = FootballDataFixtureScoreDto(homeTeam = homeTeam, awayTeam = awayTeam)
            )
    }
}

@DataTransferObject
data class FootballDataFixtureScoreDto(
    @JsonProperty("homeTeam") val homeTeam: Int?,
    @JsonProperty("awayTeam") val awayTeam: Int?,
) {

    constructor() : this(null, null)

    fun nothing() = homeTeam == null || awayTeam == null
}

@DataTransferObject
data class FootballDataFixtureTeamDto(
    @JsonProperty("id") val id: Long?,
    @JsonProperty("name") val name: String?,
)
