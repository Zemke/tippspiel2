package com.github.zemke.tippspiel2.core.task

import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.service.CompetitionService
import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.service.FootballDataService
import com.github.zemke.tippspiel2.service.StandingService
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class FootballDataScheduledTask {

    @Autowired
    private lateinit var footballDataService: FootballDataService

    @Autowired
    private lateinit var competitionService: CompetitionService

    @Autowired
    private lateinit var fixtureService: FixtureService

    @Autowired
    private lateinit var standingService: StandingService

    @Scheduled(fixedDelayString = "\${tippspiel2.football-data.fixed-delay}")
    @Transactional
    fun exec() {
        val competition = competitionService.findByCurrentTrue() ?: return
        val currentFixtures = fixtureService.findFixturesByCompetitionAndManualFalse(competition)
        val teamsToBeAffectedByUpdate = currentFixtures.fold(arrayListOf()) { acc: ArrayList<Team>, fixture: Fixture ->
            with(acc) { addAll(listOf(fixture.homeTeam, fixture.awayTeam)); this }
        }
        val footballDataFixturesOfCompetition = footballDataService.requestFixtures(competition.id).fixtures
        val fixturesNewOrChanged = footballDataFixturesOfCompetition
                .map { FootballDataFixtureDto.fromDto(it, teamsToBeAffectedByUpdate, competition) }
                .filter { !currentFixtures.contains(it) }

        if (fixturesNewOrChanged.isNotEmpty()) {
            fixtureService.saveMany(fixturesNewOrChanged)
            standingService.updateStandings(competition)
        }
    }
}
