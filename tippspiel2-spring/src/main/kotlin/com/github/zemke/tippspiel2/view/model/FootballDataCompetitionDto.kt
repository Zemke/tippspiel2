package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import java.time.Instant
import java.time.LocalDate

@DataTransferObject
data class FootballDataCompetitionDto(
    @JsonProperty("id") var id: Long,
    @JsonProperty("name") var name: String,
    @JsonProperty("code") var code: String,
    @JsonProperty("lastUpdated") var lastUpdated: Instant,
    @JsonProperty("currentSeason") var currentSeason: FootbalDataCompetitionCurrentSeasonDto,
) {

    companion object {

        fun fromDto(
            dto: FootballDataCompetitionDto,
            current: Boolean,
            championBetAllowed: Boolean,
        ): Competition = Competition(
            id = dto.id,
            caption = dto.name,
            league = dto.code,
            year = dto.currentSeason.startDate.year,
            currentMatchday = dto.currentSeason.currentMatchday,
            lastUpdated = dto.lastUpdated,
            current = current,
            championBetAllowed = championBetAllowed,
        ).apply {
            champion = dto.currentSeason.winner?.let { FootballDataTeamDto.fromDto(it, this) }
        }

        fun toDto(competition: Competition) = FootballDataCompetitionDto(
            id = competition.id,
            name = competition.caption,
            code = competition.league,
            lastUpdated = competition.lastUpdated,
            currentSeason = FootbalDataCompetitionCurrentSeasonDto(
                currentMatchday = competition.currentMatchday,
                // TODO Persist whole startDate to map back completely.
                startDate = LocalDate.of(competition.year, 1, 1),
                winner = competition.champion?.let { FootballDataTeamDto.toDto(it) },
            ),
        )
    }
}

@DataTransferObject
data class FootbalDataCompetitionCurrentSeasonDto(
    @JsonProperty("currentMatchday") var currentMatchday: Int,
    @JsonProperty("startDate") var startDate: LocalDate,
    @JsonProperty("winner") var winner: FootballDataTeamDto?,

    // TODO Should probably also be persisted.
    // @JsonProperty("id") var id: Long,
)
