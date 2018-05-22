package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.view.util.DataTransferObject
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
    }
}
