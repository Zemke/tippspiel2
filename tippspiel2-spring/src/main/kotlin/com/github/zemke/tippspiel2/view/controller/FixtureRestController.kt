package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.view.exception.NotFoundException
import com.github.zemke.tippspiel2.view.model.FixtureDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/fixtures")
class FixtureRestController(
    @Autowired private val fixtureService: FixtureService
) {

    @GetMapping("")
    fun getFixtures(
        @RequestParam() competitionId: Long?,
        @RequestParam(defaultValue = "false") complete: Boolean,
        @RequestParam("status") statuses: List<FixtureStatus>?
    ): ResponseEntity<List<FixtureDto>> =
        ResponseEntity.ok(
            fixtureService.findAll()
                .filter { !complete || it.complete() }
                .filter { competitionId == null || it.competition.id == competitionId }
                .filter { statuses == null || statuses.contains(it.status) }
                .map { FixtureDto.toDto(it) }
        )

    @GetMapping("/{fixtureId}")
    fun getFixture(@PathVariable("fixtureId") fixtureId: Long): ResponseEntity<FixtureDto> =
        ResponseEntity.ok(
            FixtureDto.toDto(
                fixtureService.getById(fixtureId)
                    .orElseThrow { throw NotFoundException("There is no such fixture.", "err.fixtureNotFound") }
            )
        )
}
