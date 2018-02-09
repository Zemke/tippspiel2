package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import com.github.zemke.tippspiel2.view.util.FootballDataUtils
import org.springframework.hateoas.Resource

@DataTransferObject
data class FootballDataTeamWrappedListDto(
        @JsonProperty("count") var count: Int,
        @JsonProperty("teams") var teams: List<Resource<FootballDataTeamDto>>
)

@DataTransferObject
data class FootballDataTeamDto(
        @JsonProperty("name") var name: String?,
        @JsonProperty("code") var code: String?,
        @JsonProperty("shortName") var shortName: String?,
        @JsonProperty("squadMarketValue") var squadMarketValue: String?,
        @JsonProperty("crestUrl") var crestUrl: String?
) {

    companion object {

        fun map(teamResourceDto: Resource<FootballDataTeamDto>, competition: Competition): Team = Team(
                FootballDataUtils.extractId(teamResourceDto.id),
                teamResourceDto.content.name!!,
                teamResourceDto.content.squadMarketValue!!,
                competition
        )
    }
}
