package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import org.springframework.hateoas.Resource

@DataTransferObject
data class FootballDataTeamWrappedListDto(
        @JsonProperty("count") private var count: Int,
        @JsonProperty("teams") private var teams: List<Resource<FootballDataTeamDto>>
)

@DataTransferObject
data class FootballDataTeamDto(
        @JsonProperty("name") private var name: String?,
        @JsonProperty("code") private var code: String?,
        @JsonProperty("shortName") private var shortName: String?,
        @JsonProperty("squadMarketValue") private var squadMarketValue: String?,
        @JsonProperty("crestUrl") private var crestUrl: String?
)
