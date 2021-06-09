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
        @JsonProperty("emblemUrl") var emblemUrl: String?,
        @JsonProperty("plan") var plan: String,
        @JsonProperty("numberOfAvailableSeasons") var numberOfAvailableSeasons: Int,
        @JsonProperty("lastUpdated") var lastUpdated: Instant,
        @JsonProperty("currentSeason") var currentSeason: FootbalDataCompetitionCurrentSeasonDto,
        @JsonProperty("area") var area: FootbalDataCompetitionAreaDto,
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
    }
}

@DataTransferObject
data class FootbalDataCompetitionCurrentSeasonDto(
        @JsonProperty("id") var id: Long,
        @JsonProperty("startDate") var startDate: LocalDate,
        @JsonProperty("endDate") var endDate: LocalDate,
        @JsonProperty("currentMatchday") var currentMatchday: Int,
        @JsonProperty("winner") var winner: FootballDataTeamDto?,
)

@DataTransferObject
data class FootbalDataCompetitionAreaDto(
        @JsonProperty("id") var id: Long,
        @JsonProperty("name") var name: String,
        @JsonProperty("countryCode") var countryCode: String?,
        @JsonProperty("ensignUrl") var ensignUrl: String?,
)

