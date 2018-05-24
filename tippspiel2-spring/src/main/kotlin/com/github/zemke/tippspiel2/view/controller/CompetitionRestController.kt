package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.service.CompetitionService
import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.service.FootballDataScheduledTasksService
import com.github.zemke.tippspiel2.service.FootballDataService
import com.github.zemke.tippspiel2.service.NULL_TEAM_ID
import com.github.zemke.tippspiel2.view.exception.BadRequestException
import com.github.zemke.tippspiel2.view.exception.NotFoundException
import com.github.zemke.tippspiel2.view.model.CompetitionCreationDto
import com.github.zemke.tippspiel2.view.model.CompetitionDto
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/competitions")
class CompetitionRestController(
        @Autowired private val footballDataService: FootballDataService,
        @Autowired private val competitionService: CompetitionService,
        @Autowired private val fixtureService: FixtureService,
        @Autowired private val footballDataScheduledTasksService: FootballDataScheduledTasksService
) {

    @GetMapping("")
    fun queryCompetitions(@RequestParam("current", defaultValue = "false") current: Boolean): ResponseEntity<List<CompetitionDto>> {
        val competitions = if (current) {
            listOf(competitionService.findByCurrentTrue()
                    ?: throw NotFoundException("There is currently no competition.")
            )
        } else {
            competitionService.findAll()
        }

        return ResponseEntity.ok(competitions.map { CompetitionDto.toDto(it) })
    }

    @PostMapping("")
    fun createCompetition(@RequestBody competitionCreationDto: CompetitionCreationDto): ResponseEntity<CompetitionDto> {
        val competitionDto = footballDataService.requestCompetition(competitionCreationDto.id)
        val fixtureWrappedListDto = footballDataService.requestFixtures(competitionCreationDto.id)
        val teamWrappedListDto = footballDataService.requestTeams(competitionCreationDto.id)

        val competition = FootballDataCompetitionDto.fromDto(competitionDto)
        val teams = teamWrappedListDto.teams.map { FootballDataTeamDto.fromDto(it, competition) }
        val fixtures = fixtureWrappedListDto.fixtures
                .filter { FixtureStatus.OF_INTEREST.contains(it.status) }
                .filter { it.homeTeamId != NULL_TEAM_ID && it.awayTeamId != NULL_TEAM_ID }
                .map { FootballDataFixtureDto.fromDto(it, teams, competition) }

        fixtureService.saveMany(fixtures)

        return ResponseEntity.status(HttpStatus.CREATED).body(CompetitionDto.toDto(competition))
    }

    @PutMapping("/{competitionId}")
    fun updateCompetition(@PathVariable competitionId: Long,
                          @RequestBody competitionCreationDto: CompetitionCreationDto): ResponseEntity<CompetitionDto> {
        if (competitionCreationDto.current == true) {
            val newCurrentCompetition = competitionService.find(competitionId)
                    .orElseThrow { throw NotFoundException("No such competition.") }

            return ResponseEntity.ok(CompetitionDto.toDto(competitionService.setCurrentCompetition(newCurrentCompetition)))
        } else {
            val currentCompetition = competitionService.findByCurrentTrue()
            val competitionToUpdate = competitionService.find(competitionId)
                    .orElseThrow { throw NotFoundException("No such competition.") }
            if (competitionToUpdate == currentCompetition) footballDataScheduledTasksService.updateCurrentCompetitionWithItsTeams()
            else throw BadRequestException("At the moment you can only update the current competition.")
            return ResponseEntity.ok(CompetitionDto.toDto(competitionService.find(competitionId).get()))
        }
    }
}
