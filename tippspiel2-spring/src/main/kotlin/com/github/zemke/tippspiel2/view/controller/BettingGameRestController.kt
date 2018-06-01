package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.service.BettingGameService
import com.github.zemke.tippspiel2.service.CompetitionService
import com.github.zemke.tippspiel2.view.exception.BadRequestException
import com.github.zemke.tippspiel2.view.exception.NotFoundException
import com.github.zemke.tippspiel2.view.model.BettingGameCreationDto
import com.github.zemke.tippspiel2.view.model.BettingGameDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/betting-games")
class BettingGameRestController {

    @Autowired
    private lateinit var competitionService: CompetitionService

    @Autowired
    private lateinit var bettingGameService: BettingGameService

    @PostMapping("")
    fun createBettingGame(@RequestBody bettingGameCreationDto: BettingGameCreationDto): ResponseEntity<BettingGameDto> {
        val competition = competitionService.find(bettingGameCreationDto.competition)
                .orElseThrow { throw BadRequestException("There is no such betting game", "err.bettingGameNotFound") }

        val bettingGame = BettingGameCreationDto.fromDto(bettingGameCreationDto, competition)

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BettingGameDto.toDto(bettingGameService.saveBettingGame(bettingGame)))
    }

    @GetMapping("{bettingGameId}")
    fun readBettingGame(@PathVariable bettingGameId: Long): ResponseEntity<BettingGameDto> {
        val bettingGame = bettingGameService.find(bettingGameId)
                .orElseThrow { throw NotFoundException("No such betting game.", "err.bettingGameNotFound") }
        return ResponseEntity.ok(BettingGameDto.toDto(bettingGame))
    }

    @GetMapping("")
    fun queryBettingGames(@RequestParam("invitation-token") invitationToken: String?): ResponseEntity<List<BettingGameDto>> {
        val bettingGames = bettingGameService.findAll()
                .filter { invitationToken == null || it.invitationToken == invitationToken }
        return ResponseEntity.ok(bettingGames.map { BettingGameDto.toDto(it) })
    }
}
