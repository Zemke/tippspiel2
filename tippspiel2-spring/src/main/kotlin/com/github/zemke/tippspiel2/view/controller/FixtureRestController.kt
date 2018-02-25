package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.service.FootballDataService
import com.github.zemke.tippspiel2.view.model.FixtureDto
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/fixtures")
class FixtureRestController(
        @Autowired private val footballDataService: FootballDataService,
        @Autowired private val fixtureService: FixtureService
) {

    @PostMapping("")
    fun createFixtures(@RequestParam("competition") competitionId: Long): ResponseEntity<List<FixtureDto>> {
        val competitionDto = footballDataService.requestCompetition(competitionId)
        val fixtureWrappedListDto = footballDataService.requestFixtures(competitionId)
        val teamWrappedListDto = footballDataService.requestTeams(competitionId)

        val competition = FootballDataCompetitionDto.fromDto(competitionDto)
        val teams = teamWrappedListDto.teams.map { FootballDataTeamDto.fromDto(it, competition) }
        val fixtures = fixtureWrappedListDto.fixtures.map { FootballDataFixtureDto.fromDto(it, teams, competition) }

        return ResponseEntity.status(HttpStatus.CREATED).body((fixtureService.saveMany(fixtures).map { FixtureDto.toDto(it) }))
    }
}
