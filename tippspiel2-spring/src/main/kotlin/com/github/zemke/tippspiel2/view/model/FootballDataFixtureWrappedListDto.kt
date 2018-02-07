package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import org.springframework.hateoas.Resource

@DataTransferObject
data class FootballDataFixtureWrappedListDto(
        @JsonProperty("count") private var count: Int,
        @JsonProperty("fixtures") private var fixtures: List<Resource<FootballDataFixtureDto>>
)

@DataTransferObject
data class FootballDataFixtureDto(
        @JsonProperty("date") private var date: String?,
        @JsonProperty("status") private var status: String?,
        @JsonProperty("matchday") private var matchday: Int?,
        @JsonProperty("homeTeamName") private var homeTeamName: String?,
        @JsonProperty("awayTeamName") private var awayTeamName: String?,
        @JsonProperty("result") private var result: FootballDataFixtureResultDto?,
        @JsonProperty("odds") private var odds: Double?
)

@DataTransferObject
data class FootballDataFixtureResultDto(
        @JsonProperty("goalsHomeTeam") private var goalsHomeTeam: Int?,
        @JsonProperty("goalsAwayTeam") private var goalsAwayTeam: Int?
)
