package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.view.util.DataTransferObject

@DataTransferObject
data class FootballDataTeamWrappedListDto(
    @JsonProperty("count") val count: Int,
    @JsonProperty("teams") val teams: List<FootballDataTeamDto>
)

@DataTransferObject
data class FootballDataTeamDto(
    @JsonProperty("id") val id: Long,
    @JsonProperty("name") val name: String,
    @JsonProperty("shortName") val shortName: String?,
    @JsonProperty("crestUrl") val crestUrl: String?
) {

    companion object {

        fun fromDto(dto: FootballDataTeamDto, competition: Competition): Team = Team(
            id = dto.id,
            name = dto.name,
            competition = competition
        )

        fun toDto(team: Team): FootballDataTeamDto = FootballDataTeamDto(
            id = team.id,
            name = team.name,
            crestUrl = null,
            shortName = null,
        )
    }
}
