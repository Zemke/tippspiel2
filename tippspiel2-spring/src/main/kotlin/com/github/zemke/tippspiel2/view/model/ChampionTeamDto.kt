package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.view.util.DataTransferObject

@DataTransferObject
data class ChampionTeamDto(
        val id: Long,
        val name: String,
) {

    companion object {

        fun toDto(team: Team): ChampionTeamDto = ChampionTeamDto(
                team.id,
                team.name,
        )

        fun fromDto(dto: ChampionTeamDto, competition: CompetitionDto): Team = Team(
                id = dto.id,
                name = dto.name,
                competition = CompetitionDto.fromDto(competition),
        )
    }
}
