package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import com.github.zemke.tippspiel2.view.util.FootballDataUtils
import org.springframework.hateoas.Resource
import java.sql.Timestamp
import java.util.*

@DataTransferObject
data class FootballDataFixtureWrappedListDto(
        @JsonProperty("count") var count: Int,
        @JsonProperty("fixtures") var fixtures: List<Resource<FootballDataFixtureDto>>
)

@DataTransferObject
data class FootballDataFixtureDto(
        @JsonProperty("date") var date: Date?,
        @JsonProperty("status") var status: String?,
        @JsonProperty("matchday") var matchday: Int?,
        @JsonProperty("homeTeamName") var homeTeamName: String?,
        @JsonProperty("awayTeamName") var awayTeamName: String?,
        @JsonProperty("result") var result: FootballDataFixtureResultDto?,
        @JsonProperty("odds") var odds: Double?
) {
    companion object {

        fun map(resource: Resource<FootballDataFixtureDto>, teams: List<Team>, competition: Competition): Fixture = Fixture(
                FootballDataUtils.extractId(resource.id),
                Timestamp(resource.content.date?.time!!),
                FixtureStatus.valueOf(resource.content.status!!),
                resource.content.matchday!!,
                resource.content.odds!!,
                resource.content.result!!.goalsHomeTeam!!,
                resource.content.result!!.goalsAwayTeam!!,
                teams.find { it.id == FootballDataUtils.extractId(resource.getLink("homeTeam")) }!!,
                teams.find { it.id == FootballDataUtils.extractId(resource.getLink("awayTeam")) }!!,
                competition
        )
    }
}

@DataTransferObject
data class FootballDataFixtureResultDto(
        @JsonProperty("goalsHomeTeam") var goalsHomeTeam: Int?,
        @JsonProperty("goalsAwayTeam") var goalsAwayTeam: Int?
)
