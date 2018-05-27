package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.service.BettingGameService
import com.github.zemke.tippspiel2.service.StandingService
import com.github.zemke.tippspiel2.view.exception.BadRequestException
import com.github.zemke.tippspiel2.view.model.StandingDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/standings")
class StandingRestController {

    @Autowired
    private lateinit var standingService: StandingService

    @Autowired
    private lateinit var bettingGameService: BettingGameService

    @GetMapping("")
    fun getStandings(@RequestParam bettingGame: Long?, request: HttpServletRequest): ResponseEntity<List<StandingDto>> {
        return if (bettingGame != null)
            ResponseEntity.ok(standingService.findByBettingGame(bettingGameService.find(bettingGame)
                    .orElseThrow { throw BadRequestException("There is no such betting game.", "err.bettingGameNotFound") })
                    .map { StandingDto.toDto(it) })
        else
            ResponseEntity.ok(standingService.findAll().map { StandingDto.toDto(it) })
    }
}
