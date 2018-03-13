package com.github.zemke.tippspiel2.core.task

import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.service.CompetitionService
import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.service.FootballDataService
import com.github.zemke.tippspiel2.service.StandingService
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
open class FootballDataScheduledTask {

    @Autowired
    private lateinit var footballDataService: FootballDataService

    @Autowired
    private lateinit var competitionService: CompetitionService

    @Autowired
    private lateinit var fixtureService: FixtureService

    @Autowired
    private lateinit var standingService: StandingService

    private val logger = LoggerFactory.getLogger(FootballDataScheduledTask::class.java)

    @Scheduled(fixedDelayString = "\${tippspiel2.football-data.fixed-delay}")
    @Transactional
    open fun exec() {
        val competition = competitionService.findByCurrentTrue()
        val fixturesToUpdate = fixtureService.findFixturesByCompetitionAndManualFalse(competition!!)
        val teamsToBeAffectedByUpdate = fixturesToUpdate.fold(arrayListOf()) { acc: ArrayList<Team>, fixture: Fixture ->
            with(acc) { addAll(listOf(fixture.homeTeam, fixture.awayTeam)); this }
        }
        val footballDataFixturesOfCompetition = footballDataService.requestFixtures(competition.id).fixtures
        val fixturesToSave = footballDataFixturesOfCompetition
                .map { FootballDataFixtureDto.fromDto(it, teamsToBeAffectedByUpdate, competition) }
                .filter { fixturesToUpdate.contains(it) }

        if (fixturesToSave.isNotEmpty()) {
            fixtureService.saveMany(fixturesToSave)
            standingService.updateStandings()
        }
    }
}
