package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.service.BetService
import com.github.zemke.tippspiel2.service.BettingGameService
import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.service.JsonWebTokenService
import com.github.zemke.tippspiel2.service.UserService
import com.github.zemke.tippspiel2.view.exception.BadRequestException
import com.github.zemke.tippspiel2.view.exception.ForbiddenException
import com.github.zemke.tippspiel2.view.exception.NotFoundException
import com.github.zemke.tippspiel2.view.exception.UnauthorizedException
import com.github.zemke.tippspiel2.view.model.BetCreationDto
import com.github.zemke.tippspiel2.view.model.BetDto
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
import java.sql.Timestamp
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/bets")
class BetRestController {

    @Autowired
    private lateinit var betService: BetService

    @Autowired
    private lateinit var fixtureService: FixtureService

    @Autowired
    private lateinit var bettingGameService: BettingGameService

    @Autowired
    private lateinit var jsonWebTokenService: JsonWebTokenService

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("")
    fun getBets(@RequestParam user: Long?, @RequestParam bettingGame: Long?): ResponseEntity<List<BetDto>> =
            ResponseEntity.ok(betService.findAll()
                    .filter { user == null || user == it.user.id }
                    .filter { bettingGame == null || bettingGame == it.bettingGame.id }
                    .map { BetDto.toDto(it) })

    @PutMapping("/{betId}")
    fun updateBet(@PathVariable("betId") betId: Long, @RequestBody betCreationDto: BetCreationDto,
                  request: HttpServletRequest): ResponseEntity<BetDto> {
        val bet = betService.find(betId).orElseThrow({ throw NotFoundException() })
        if (!isBettingStillAllowed(bet.fixture.date)) throw BadRequestException("Too late to bet.")
        val token = jsonWebTokenService.assertToken(request) ?: throw UnauthorizedException()
        if (jsonWebTokenService.getIdFromToken(token) != bet.user.id) throw ForbiddenException()
        bet.goalsHomeTeamBet = betCreationDto.goalsHomeTeamBet
        bet.goalsAwayTeamBet = betCreationDto.goalsAwayTeamBet
        return ResponseEntity.ok(BetDto.toDto(betService.save(bet)))
    }

    @PostMapping("")
    fun saveBet(@RequestBody betCreationDto: BetCreationDto, request: HttpServletRequest): ResponseEntity<BetDto> {
        val token = jsonWebTokenService.assertToken(request) ?: throw UnauthorizedException()
        if (jsonWebTokenService.getIdFromToken(token) != betCreationDto.user) {
            throw ForbiddenException()
        }

        val fixture = fixtureService.getById(betCreationDto.fixture)
        if (!isBettingStillAllowed(fixture.date)) throw BadRequestException("Too late to bet.")

        val bet = BetCreationDto.fromDto(
                dto = betCreationDto,
                fixture = fixture,
                bettingGame = bettingGameService.find(betCreationDto.bettingGame),
                user = userService.getUser(betCreationDto.user)!!)
        return ResponseEntity.status(HttpStatus.CREATED).body(BetDto.toDto(betService.save(bet)))
    }

    private fun isBettingStillAllowed(fixtureStart: Timestamp) = fixtureStart > Date()
}
