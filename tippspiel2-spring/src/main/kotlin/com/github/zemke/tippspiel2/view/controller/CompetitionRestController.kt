package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.service.CompetitionService
import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.service.FootballDataScheduledTasksService
import com.github.zemke.tippspiel2.service.FootballDataService
import com.github.zemke.tippspiel2.service.NULL_TEAM_ID
import com.github.zemke.tippspiel2.service.TeamService
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
import org.springframework.transaction.annotation.Transactional
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
    @Autowired private val teamService: TeamService,
    @Autowired private val footballDataScheduledTasksService: FootballDataScheduledTasksService
) {

    @GetMapping("")
    fun queryCompetitions(@RequestParam("current", defaultValue = "false") current: Boolean): ResponseEntity<List<CompetitionDto>> {
        val competitions = if (current) {
            listOf(
                competitionService.findByCurrentTrue()
                    ?: throw NotFoundException("There is currently no competition.", "err.noCurrentCompetition")
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

        val competition = FootballDataCompetitionDto.fromDto(competitionDto, false, true)
        val teams = teamWrappedListDto.teams.map { FootballDataTeamDto.fromDto(it, competition) }
        val fixtures = fixtureWrappedListDto.matches
            .filter { it.homeTeam != null && it.awayTeam != null }
            .filter { it.homeTeam?.id != NULL_TEAM_ID && it.awayTeam?.id != NULL_TEAM_ID }
            .map { FootballDataFixtureDto.fromDto(it, teams, competition) }

        fixtureService.saveMany(fixtures)

        return ResponseEntity.status(HttpStatus.CREATED).body(CompetitionDto.toDto(competition))
    }

    @PutMapping("/{competitionId}")
    @Transactional
    fun updateCompetition(
        @PathVariable competitionId: Long,
        @RequestBody competitionCreationDto: CompetitionCreationDto
    ): ResponseEntity<CompetitionDto> {
        val competitionToUpdate = competitionService.find(competitionId)
            .orElseThrow { throw NotFoundException("No such competition.", "err.competitionNotFound") }

        if (competitionCreationDto.current != competitionToUpdate.current)
            competitionService.setCurrentCompetition(competitionToUpdate)

        if (competitionCreationDto.champion != null && competitionCreationDto.champion != competitionToUpdate.champion?.id)
            competitionService.saveChampion(
                competitionToUpdate,
                teamService.find(competitionCreationDto.champion)
                    .orElseThrow { throw BadRequestException("No such team.", "err.teamNotFound") },
                true
            )

        val currentCompetition = competitionService.findByCurrentTrue()

        if (currentCompetition != null && currentCompetition == competitionToUpdate) {
            footballDataScheduledTasksService.updateCurrentCompetitionWithItsTeams()
            return ResponseEntity.ok(CompetitionDto.toDto(currentCompetition))
        }

        return ResponseEntity.ok(CompetitionDto.toDto(competitionToUpdate))
    }
}
