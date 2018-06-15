package com.github.zemke.tippspiel2.view.stream

import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.view.model.FixtureDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*
import java.util.stream.Stream


@RestController
@RequestMapping("/api/stream/fixtures")
class FixtureStream(
        @Autowired private val fixtureService: FixtureService
) {

    @GetMapping("/{fixtureId}", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getFixture(@PathVariable("fixtureId") fixtureId: Long): Flux<FixtureDto> {
        val interval = Flux.interval(Duration.ofSeconds(4))
        val meh = Flux.fromStream<FixtureDto> { (Stream.generate<FixtureDto> { FixtureDto.toDto(fixtureService.getById(fixtureId).get()) }) }
        return Flux.zip(interval, meh).map {
            it.t2.copy(goalsHomeTeam = (Random().nextInt(3 - 0) + 0), goalsAwayTeam = (Random().nextInt(3 - 0) + 0))
        }
    }
}
