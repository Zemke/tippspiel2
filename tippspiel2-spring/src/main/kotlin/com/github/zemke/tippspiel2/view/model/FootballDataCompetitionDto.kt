package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Team
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

        fun fromDto(dto: FootballDataCompetitionDto,
                    current: Boolean,
                    championBetAllowed: Boolean,
                    champion: Team?): Competition = Competition(
                id = dto.id,
                caption = dto.caption,
                league = dto.league,
                year = dto.year,
                currentMatchday = dto.currentMatchday,
                numberOfMatchdays = dto.numberOfMatchdays,
                numberOfTeams = dto.numberOfTeams,
                numberOfGames = dto.numberOfGames,
                lastUpdated = Timestamp(dto.lastUpdated.time),
                current = current,
                championBetAllowed = championBetAllowed,
                champion = champion
        )

        fun toDto(competition: Competition): FootballDataCompetitionDto = FootballDataCompetitionDto(
                id = competition.id,
                numberOfMatchdays = competition.numberOfMatchdays,
                caption = competition.caption,
                currentMatchday = competition.currentMatchday,
                lastUpdated = competition.lastUpdated,
                league = competition.league,
                numberOfGames = competition.numberOfGames,
                numberOfTeams = competition.numberOfTeams,
                year = competition.year
        )
    }
}
