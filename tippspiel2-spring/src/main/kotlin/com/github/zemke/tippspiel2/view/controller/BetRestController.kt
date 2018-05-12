package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.persistence.model.Bet
import com.github.zemke.tippspiel2.service.BetService
import com.github.zemke.tippspiel2.service.BettingGameService
import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.service.JsonWebTokenService
import com.github.zemke.tippspiel2.service.UserService
import com.github.zemke.tippspiel2.view.exception.ForbiddenException
import com.github.zemke.tippspiel2.view.model.BetCreationDto
import com.github.zemke.tippspiel2.view.model.BetDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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

    @PostMapping("")
    fun saveBet(@RequestBody betCreationDto: BetCreationDto, request: HttpServletRequest): ResponseEntity<Bet> {
        if (jsonWebTokenService.getIdFromToken(jsonWebTokenService.assertToken(request)) != betCreationDto.user) {
            throw ForbiddenException()
        }

        val bet = BetCreationDto.fromDto(
                dto = betCreationDto,
                fixture = fixtureService.getById(betCreationDto.fixture),
                bettingGame = bettingGameService.find(betCreationDto.bettingGame),
                user = userService.getUser(betCreationDto.user)!!)
        return ResponseEntity.status(HttpStatus.CREATED).body(betService.save(bet))
    }
}