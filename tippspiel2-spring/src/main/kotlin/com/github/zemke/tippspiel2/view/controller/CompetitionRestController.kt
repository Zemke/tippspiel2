package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.service.FootballDataService
import com.github.zemke.tippspiel2.service.NULL_TEAM_ID
import com.github.zemke.tippspiel2.view.model.CompetitionCreationDto
import com.github.zemke.tippspiel2.view.model.CompetitionDto
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/competitions")
class CompetitionRestController(
        @Autowired private val footballDataService: FootballDataService,
        @Autowired private val fixtureService: FixtureService
) {

    @PostMapping("")
    fun createFixtures(@RequestBody competitionCreationDto: CompetitionCreationDto): ResponseEntity<CompetitionDto>? {
        val competitionDto = footballDataService.requestCompetition(competitionCreationDto.id)
        val fixtureWrappedListDto = footballDataService.requestFixtures(competitionCreationDto.id)
        val teamWrappedListDto = footballDataService.requestTeams(competitionCreationDto.id)

        val competition = FootballDataCompetitionDto.fromDto(competitionDto)
        val teams = teamWrappedListDto.teams.map { FootballDataTeamDto.fromDto(it, competition) }
        val fixtures = fixtureWrappedListDto.fixtures
                .filter { listOf(FixtureStatus.TIMED, FixtureStatus.IN_PLAY, FixtureStatus.FINISHED).contains(it.status) }
                .filter { it.homeTeamId != NULL_TEAM_ID && it.awayTeamId != NULL_TEAM_ID }
                .map { FootballDataFixtureDto.fromDto(it, teams, competition) }

        fixtureService.saveMany(fixtures)

        return ResponseEntity.status(HttpStatus.CREATED).body(CompetitionDto.toDto(competition))
    }
}
