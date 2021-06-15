package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.view.util.DataTransferObject

@DataTransferObject
data class FootballDataTeamWrappedListDto(
    @JsonProperty("count") var count: Int,
    @JsonProperty("teams") var teams: List<FootballDataTeamDto>
)

@DataTransferObject
data class FootballDataTeamDto(
    @JsonProperty("id") var id: Long,
    @JsonProperty("name") var name: String,
    @JsonProperty("shortName") var shortName: String?,
    @JsonProperty("crestUrl") var crestUrl: String?
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
