package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.view.util.DataTransferObject

@DataTransferObject
data class TeamDto(
        val id: Long,
        val name: String,
        val squadMarketValue: String?,
        val competition: CompetitionDto
) {

    companion object {

        fun toDto(team: Team): TeamDto = TeamDto(
                team.id,
                team.name,
                team.squadMarketValue,
                CompetitionDto.toDto(team.competition)
        )
    }
}
