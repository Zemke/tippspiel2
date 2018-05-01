package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.service.BettingGameService
import com.github.zemke.tippspiel2.service.CommunityService
import com.github.zemke.tippspiel2.service.CompetitionService
import com.github.zemke.tippspiel2.view.model.BettingGameCreationDto
import com.github.zemke.tippspiel2.view.model.BettingGameDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/betting-games")
class BettingGameRestController {

    @Autowired
    private lateinit var communityService: CommunityService

    @Autowired
    private lateinit var competitionService: CompetitionService

    @Autowired
    private lateinit var bettingGameService: BettingGameService

    @PostMapping("")
    fun createBettingGame(@RequestBody communityCreationDto: BettingGameCreationDto): ResponseEntity<BettingGameDto> {
        val community = communityService.find(communityCreationDto.community)
        val competition = competitionService.find(communityCreationDto.competition)

        val bettingGame = BettingGameCreationDto.fromDto(
                dto = communityCreationDto,
                community = community,
                competition = competition
        )

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BettingGameDto.toDto(bettingGameService.createBettingGame(bettingGame)))
    }
}
