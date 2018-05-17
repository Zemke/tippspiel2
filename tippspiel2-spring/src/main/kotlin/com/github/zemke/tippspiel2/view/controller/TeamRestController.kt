package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.service.CompetitionService
import com.github.zemke.tippspiel2.service.TeamService
import com.github.zemke.tippspiel2.view.exception.BadRequestException
import com.github.zemke.tippspiel2.view.model.TeamDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/teams")
class TeamRestController(
        @Autowired private val competitionService: CompetitionService,
        @Autowired private val teamService: TeamService
) {

    @GetMapping("")
    fun getTeams(@RequestParam competition: Long?): ResponseEntity<List<TeamDto>> {
        val teams =
                if (competition != null)
                    teamService.findByCompetition(competitionService.find(competition)
                            .orElseThrow { throw BadRequestException("Invalid competition.") })
                else teamService.findAll()

        return ResponseEntity.ok(teams.map { TeamDto.toDto(it) })
    }
}
