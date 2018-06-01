package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import java.sql.Timestamp
import java.util.*

@DataTransferObject
data class CompetitionDto(
        val id: Long,
        val caption: String,
        val league: String,
        val year: String,
        val currentMatchday: Int,
        val numberOfMatchdays: Int,
        val numberOfTeams: Int,
        val numberOfGames: Int,
        val lastUpdated: Date,
        val current: Boolean,
        val championBetAllowed: Boolean,
        val champion: TeamDto?
) {

    companion object {

        fun toDto(competition: Competition): CompetitionDto = CompetitionDto(
                competition.id,
                competition.caption,
                competition.league,
                competition.year,
                competition.currentMatchday,
                competition.numberOfMatchdays,
                competition.numberOfTeams,
                competition.numberOfGames,
                Date(competition.lastUpdated.time),
                competition.current,
                competition.championBetAllowed,
                if (competition.champion != null) TeamDto.toDto(competition.champion!!) else null
        )

        fun fromDto(dto: CompetitionDto): Competition = Competition(
                id = dto.id,
                currentMatchday = dto.currentMatchday,
                year = dto.year,
                numberOfTeams = dto.numberOfTeams,
                numberOfGames = dto.numberOfGames,
                league = dto.league,
                lastUpdated = Timestamp(dto.lastUpdated.time),
                caption = dto.caption,
                numberOfMatchdays = dto.numberOfMatchdays,
                champion = if (dto.champion != null) TeamDto.fromDto(dto.champion) else null,
                championBetAllowed = dto.championBetAllowed,
                current = dto.current
        )
    }
}
