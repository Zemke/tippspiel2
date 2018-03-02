package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.persistence.model.Bet
import com.github.zemke.tippspiel2.service.BetService
import com.github.zemke.tippspiel2.service.BettingGameService
import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.service.FootballDataService
import com.github.zemke.tippspiel2.service.JsonWebTokenService
import com.github.zemke.tippspiel2.service.UserService
import com.github.zemke.tippspiel2.view.exception.NotFoundException
import com.github.zemke.tippspiel2.view.model.BetCreationDto
import com.github.zemke.tippspiel2.view.model.FixtureDto
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.websocket.server.PathParam

@RestController
@RequestMapping("/api/fixtures")
class FixtureRestController(
        @Autowired private val footballDataService: FootballDataService,
        @Autowired private val fixtureService: FixtureService,
        @Autowired private val jsonWebTokenService: JsonWebTokenService,
        @Autowired private val userService: UserService,
        @Autowired private val bettingGameService: BettingGameService,
        @Autowired private val betService: BetService
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

    @PostMapping("/{fixtureId}/bets")
    fun createBetOnFixture(
            @PathParam("fixtureId") fixtureId: Long, @RequestBody betCreationDto: BetCreationDto,
            request: HttpServletRequest): ResponseEntity<Bet> {
        val fixture = fixtureService.getById(fixtureId) ?: throw NotFoundException("Fixture with id $fixtureId not found.")
        val user = userService.findUserByEmail(
                jsonWebTokenService.getUsernameFromToken(jsonWebTokenService.extractToken(request)))
        val bettingGame = bettingGameService.find(betCreationDto.bettingGame)
        val bet = BetCreationDto.fromDto(betCreationDto, fixture, user!!, bettingGame, Timestamp(Date().time))
        return ResponseEntity.status(HttpStatus.CREATED).body(betService.save(bet))
    }
}
