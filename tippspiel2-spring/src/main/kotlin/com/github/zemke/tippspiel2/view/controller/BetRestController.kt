package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.service.BetService
import com.github.zemke.tippspiel2.view.model.BetDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/bets")
class BetRestController {

    @Autowired
    private lateinit var betService: BetService

    @GetMapping("")
    fun getBets(@RequestParam user: Long?, @RequestParam bettingGame: Long?): ResponseEntity<List<BetDto>> =
            ResponseEntity.ok(betService.findAll()
                    .filter { user == null || user == it.user.id }
                    .filter { bettingGame == null || bettingGame == it.bettingGame.id }
                    .map { BetDto.toDto(it) })
}
