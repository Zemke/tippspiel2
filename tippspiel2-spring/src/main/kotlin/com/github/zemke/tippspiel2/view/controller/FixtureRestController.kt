package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.service.BetService
import com.github.zemke.tippspiel2.service.BettingGameService
import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.service.JsonWebTokenService
import com.github.zemke.tippspiel2.service.UserService
import com.github.zemke.tippspiel2.view.model.FixtureDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/fixtures")
class FixtureRestController(
        @Autowired private val fixtureService: FixtureService,
        @Autowired private val jsonWebTokenService: JsonWebTokenService,
        @Autowired private val userService: UserService,
        @Autowired private val bettingGameService: BettingGameService,
        @Autowired private val betService: BetService
) {

    @GetMapping("")
    fun getFixtures(@RequestParam("competition") competitionId: Long?): ResponseEntity<List<FixtureDto>> =
            ResponseEntity.ok(fixtureService.findAll()
                    .filter { competitionId == null || it.competition.id == competitionId }
                    .map { FixtureDto.toDto(it) })
}
