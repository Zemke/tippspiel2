package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.view.util.DataTransferObject

@DataTransferObject
data class TeamDto(
    val id: Long,
    val name: String,
    val competition: CompetitionDto
) {

    companion object {

        fun toDto(team: Team): TeamDto = TeamDto(
            team.id,
            team.name,
            CompetitionDto.toDto(team.competition)
        )

        fun fromDto(dto: TeamDto): Team = Team(
            id = dto.id,
            name = dto.name,
            competition = CompetitionDto.fromDto(dto.competition),
        )
    }
}
