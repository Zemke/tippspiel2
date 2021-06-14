package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import java.time.Instant
import java.util.*

@DataTransferObject
data class CompetitionDto(
        val id: Long,
        val caption: String,
        val league: String,
        val year: Int,
        val currentMatchday: Int,
        val lastUpdated: Instant,
        val numberOfAvailableSeasons: Int,
        val current: Boolean,
        val championBetAllowed: Boolean,
        val champion: ChampionTeamDto?
) {

    companion object {

        fun toDto(competition: Competition): CompetitionDto = CompetitionDto(
                id = competition.id,
                caption = competition.caption,
                league = competition.league,
                year = competition.year,
                currentMatchday = competition.currentMatchday,
                lastUpdated =  competition.lastUpdated,
                numberOfAvailableSeasons = competition.numberOfAvailableSeasons,
                current = competition.current,
                championBetAllowed = competition.championBetAllowed,
                champion = competition.champion?.let { ChampionTeamDto.toDto(it) },
        )

        fun fromDto(dto: CompetitionDto): Competition = Competition(
                id = dto.id,
                caption = dto.caption,
                league = dto.league,
                year = dto.year,
                currentMatchday = dto.currentMatchday,
                numberOfAvailableSeasons = dto.numberOfAvailableSeasons,
                lastUpdated = dto.lastUpdated,
                current = dto.current,
                champion = dto.champion?.let { ChampionTeamDto.fromDto(it, dto) },
                championBetAllowed = dto.championBetAllowed,
        )
    }
}
