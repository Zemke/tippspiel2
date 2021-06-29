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
    @JsonProperty("stage") var stage: String?,
    @JsonProperty("group") var group: String?,
    @JsonProperty("status") var status: FixtureStatus,
    @JsonProperty("utcDate") var utcDate: Instant,
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
            homeTeam = fixture.homeTeam?.let { FootballDataFixtureTeamDto(fixture.homeTeam?.id!!, fixture.homeTeam?.name) },
            awayTeam = fixture.awayTeam?.let { FootballDataFixtureTeamDto(fixture.awayTeam?.id!!, fixture.awayTeam?.name) },
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
    @JsonProperty("fullTime") var fullTime: FootballDataFixtureScoreDto?,
    @JsonProperty("extraTime") var extraTime: FootballDataFixtureScoreDto?,
    @JsonProperty("halfTime") var halfTime: FootballDataFixtureScoreDto?,
    @JsonProperty("penalties") var penalties: FootballDataFixtureScoreDto?,
) {
    lateinit var regularTime: FootballDataFixtureScoreDto

    init {
        regularTime = if (penalties == null && extraTime == null) {
            FootballDataFixtureScoreDto(homeTeam = fullTime?.homeTeam, awayTeam = fullTime?.awayTeam)
        } else if (fullTime == null && extraTime == null && halfTime == null && penalties == null) {
            FootballDataFixtureScoreDto()
        } else {
            FootballDataFixtureScoreDto(
                homeTeam = (fullTime?.homeTeam ?: 0) - (penalties?.homeTeam ?: 0) - (extraTime?.homeTeam ?: 0),
                awayTeam = (fullTime?.awayTeam ?: 0) - (penalties?.awayTeam ?: 0) - (extraTime?.awayTeam ?: 0),
            )
        }
    }

    constructor(regularTime: FootballDataFixtureScoreDto) : this(null, null, null, null) {
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
    @JsonProperty("homeTeam") var homeTeam: Int?,
    @JsonProperty("awayTeam") var awayTeam: Int?,
) {

    constructor() : this(null, null)
}

@DataTransferObject
data class FootballDataFixtureTeamDto(
    @JsonProperty("id") var id: Long?,
    @JsonProperty("name") var name: String?,
)
