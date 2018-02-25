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
        @JsonProperty("squadMarketValue") var squadMarketValue: String?,
        @JsonProperty("crestUrl") var crestUrl: String?
) {

    companion object {

        fun map(dto: FootballDataTeamDto, competition: Competition): Team = Team(
                id = dto.id,
                name = dto.name,
                squadMarketValue = dto.squadMarketValue,
                competition = competition
        )
    }
}
