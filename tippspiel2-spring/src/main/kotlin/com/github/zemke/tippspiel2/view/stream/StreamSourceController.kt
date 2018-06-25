package com.github.zemke.tippspiel2.view.stream

import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.view.model.FixtureDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.stream.Stream


@RestController
@RequestMapping("/api/stream")
class StreamSourceController(
        @Autowired private val fixtureService: FixtureService
) {

    @GetMapping("/fixtures/{fixtureId}", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getFixture(@PathVariable("fixtureId") fixtureId: Long): Flux<FixtureDto> {
        val interval = Flux.interval(Duration.ofSeconds(5))
        val stream = Flux.fromStream<FixtureDto> { (Stream.generate<FixtureDto> { FixtureDto.toDto(fixtureService.getById(fixtureId).get()) }) }
        return Flux.zip(interval, stream).map { it.t2 }
    }

    @GetMapping("/fixtures", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getFixture(@RequestParam status: List<FixtureStatus>?): Flux<List<FixtureDto>> {
        val interval = Flux.interval(Duration.ofSeconds(5))

        val stream = Flux.fromStream<List<Fixture>> {
            (Stream.generate<List<Fixture>> {
                fixtureService.findByStatusIn(status ?: FixtureStatus.values().asList())
            })
        }

        return Flux.zip(interval, stream).map { it.t2.map { FixtureDto.toDto(it) } }
    }
}
