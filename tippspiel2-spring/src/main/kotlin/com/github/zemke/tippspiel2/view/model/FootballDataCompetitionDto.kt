package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.view.util.DataTransferObject

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@DataTransferObject
data class FootballDataCompetitionDto(
        @JsonProperty("id") var id: Long,
        @JsonProperty("name") var name: String,
        @JsonProperty("code") var code: String,
        @JsonProperty("numberOfAvailableSeasons") var numberOfAvailableSeasons: Int,
        @JsonProperty("lastUpdated") var lastUpdated: Instant,
        @JsonProperty("currentSeason") var currentSeason: FootbalDataCompetitionCurrentSeasonDto,
) {

    companion object {

        fun fromDto(dto: FootballDataCompetitionDto,
                    current: Boolean,
                    championBetAllowed: Boolean,
                    champion: Team?): Competition = Competition(
                id = dto.id,
                caption = dto.name,
                league = dto.code,
                year = dto.currentSeason.startDate.year,
                currentMatchday = dto.currentSeason.currentMatchday,
                numberOfAvailableSeasons = dto.numberOfAvailableSeasons,
                lastUpdated = dto.lastUpdated,
                current = current,
                championBetAllowed = championBetAllowed,
                champion = champion,
        )

        fun toDto(competition: Competition) = FootballDataCompetitionDto(
                id = competition.id,
                name = competition.caption,
                code = competition.league,
                numberOfAvailableSeasons = competition.numberOfAvailableSeasons,
                lastUpdated = competition.lastUpdated,
                currentSeason = FootbalDataCompetitionCurrentSeasonDto(
                    currentMatchday = competition.currentMatchday,
                    // TODO Persist whole startDate to map back completely.
                    startDate = LocalDate.of(competition.year, 1, 1)),
        )
    }
}

@DataTransferObject
data class FootbalDataCompetitionCurrentSeasonDto(
        @JsonProperty("currentMatchday") var currentMatchday: Int,
        @JsonProperty("startDate") var startDate: LocalDate,

        // TODO Should probably also be persisted.
        // @JsonProperty("id") var id: Long,

        // TODO GH-84 DTO has winner within season and busines model has champion.
        // @JsonProperty("winner") var winner: FootballDataTeamDto?,
)

