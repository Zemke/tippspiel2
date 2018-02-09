package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import java.sql.Timestamp
import java.util.*

@DataTransferObject
data class FootballDataCompetitionDto(
        @JsonProperty("id") var id: Long,
        @JsonProperty("caption") var caption: String,
        @JsonProperty("league") var league: String,
        @JsonProperty("year") var year: String,
        @JsonProperty("currentMatchday") var currentMatchday: Int,
        @JsonProperty("numberOfMatchdays") var numberOfMatchdays: Int,
        @JsonProperty("numberOfTeams") var numberOfTeams: Int,
        @JsonProperty("numberOfGames") var numberOfGames: Int,
        @JsonProperty("lastUpdated") var lastUpdated: Date
) {

    companion object {

        fun map(dto: FootballDataCompetitionDto): Competition = Competition(
                dto.id,
                dto.caption,
                dto.league,
                dto.year,
                dto.currentMatchday,
                dto.numberOfMatchdays,
                dto.numberOfTeams,
                dto.numberOfGames,
                Timestamp(dto.lastUpdated.time)
        )

    }
}
