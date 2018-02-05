package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.service.FootballDataService
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/competitions")
class CompetitionRestController(
        @Autowired private val footballDataService: FootballDataService
) {

    //    @PostMapping("")
    @GetMapping("")
    fun createCompetition(/*@RequestBody competitionId: Long*/): ResponseEntity<FootballDataCompetitionDto>? {
//        Competition(competitionId)

        val requestCompetition = footballDataService.requestCompetition(1)
        return ResponseEntity.ok(requestCompetition)
    }
}
