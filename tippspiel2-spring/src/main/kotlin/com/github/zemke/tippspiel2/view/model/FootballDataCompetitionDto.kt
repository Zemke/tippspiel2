package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zemke.tippspiel2.view.util.DataTransferObject

@DataTransferObject
data class FootballDataCompetitionDto(
        @JsonProperty("id") private var id: Long,
        @JsonProperty("caption") private var caption: String,
        @JsonProperty("league") private var league: String,
        @JsonProperty("year") private var year: String,
        @JsonProperty("currentMatchday") private var currentMatchday: Int,
        @JsonProperty("numberOfMatchdays") private var numberOfMatchdays: Int,
        @JsonProperty("numberOfTeams") private var numberOfTeams: Int,
        @JsonProperty("numberOfGames") private var numberOfGames: Int,
        @JsonProperty("lastUpdated") private var lastUpdated: String
)
