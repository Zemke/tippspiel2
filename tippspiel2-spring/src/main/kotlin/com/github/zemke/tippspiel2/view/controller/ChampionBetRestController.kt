package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.service.BettingGameService
import com.github.zemke.tippspiel2.service.ChampionBetService
import com.github.zemke.tippspiel2.service.JsonWebTokenService
import com.github.zemke.tippspiel2.service.TeamService
import com.github.zemke.tippspiel2.service.UserService
import com.github.zemke.tippspiel2.view.exception.BadRequestException
import com.github.zemke.tippspiel2.view.exception.ForbiddenException
import com.github.zemke.tippspiel2.view.exception.NotFoundException
import com.github.zemke.tippspiel2.view.exception.UnauthorizedException
import com.github.zemke.tippspiel2.view.model.ChampionBetCreationDto
import com.github.zemke.tippspiel2.view.model.ChampionBetDto
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
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/champion-bets")
class ChampionBetRestController(
        @Autowired private val jsonWebTokenService: JsonWebTokenService,
        @Autowired private val userService: UserService,
        @Autowired private val bettingGameService: BettingGameService,
        @Autowired private val teamService: TeamService,
        @Autowired private val championBetService: ChampionBetService
) {

    @PostMapping("")
    fun createChampionBet(
            @RequestBody championBetCreationDto: ChampionBetCreationDto,
            request: HttpServletRequest): ResponseEntity<ChampionBetDto> {
        val token = jsonWebTokenService.assertToken(request) ?: throw UnauthorizedException()
        val user = userService.findUserByEmail(
                jsonWebTokenService.getSubjectFromToken(token))

        val championBet = ChampionBetCreationDto.fromDto(
                bettingGameService.find(championBetCreationDto.bettingGame)
                        .orElseThrow { throw BadRequestException("There is no such betting game.") },
                teamService.find(championBetCreationDto.team)
                        .orElseThrow { throw BadRequestException("There is no such team.") },
                user!!
        )

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ChampionBetDto.toDto(championBetService.saveChampionBet(championBet)));
    }

    @PutMapping("/{championBetId}")
    fun editChampionBet(
            @PathVariable championBetId: Long,
            @RequestBody championBetCreationDto: ChampionBetCreationDto,
            request: HttpServletRequest): ResponseEntity<ChampionBetDto> {
        val token = jsonWebTokenService.assertToken(request) ?: throw UnauthorizedException()
        val authenticatedUserId = jsonWebTokenService.getIdFromToken(token)

        val championBet = championBetService.find(championBetId)
                .orElseThrow { throw NotFoundException("Champion bet not found.") }

        if (authenticatedUserId != championBet.user.id) throw ForbiddenException("This champion bet is not yours.")

        championBet.team = teamService.find(championBetCreationDto.team)
                .orElseThrow { throw BadRequestException("There is no such betting game.") }

        return ResponseEntity.ok(ChampionBetDto.toDto(championBetService.saveChampionBet(championBet)))
    }

    @GetMapping("")
    fun getChampionBets(@RequestParam bettingGame: Long?, @RequestParam user: Long?): ResponseEntity<List<ChampionBetDto>> {
        val championBets =
                (if (bettingGame != null)
                    championBetService.findByBettingGame(bettingGameService.find(bettingGame)
                            .orElseThrow { throw BadRequestException("Invalid betting game.") })
                else championBetService.findAll())
                        .filter { user == null || user == it.user.id }

        return ResponseEntity.ok(championBets.map { ChampionBetDto.toDto(it) })
    }
}
