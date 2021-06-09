package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.repository.BettingGameRepository
import com.github.zemke.tippspiel2.persistence.repository.CompetitionRepository
import com.github.zemke.tippspiel2.persistence.repository.TeamRepository
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.transaction.Transactional
import com.github.zemke.tippspiel2.service.NULL_TEAM_ID

@Component
class FootballDataScheduledTasksService {

    @Autowired
    private lateinit var footballDataService: FootballDataService

    @Autowired
    private lateinit var competitionRepository: CompetitionRepository

    @Autowired
    private lateinit var bettingGameRepository: BettingGameRepository

    @Autowired
    private lateinit var fixtureService: FixtureService

    @Autowired
    private lateinit var standingService: StandingService

    @Autowired
    private lateinit var teamRepository: TeamRepository

    @Scheduled(fixedDelayString = "\${tippspiel2.football-data.fixed-delay-fixtures}")
    @Transactional
    fun requestFixturesAndUpdateStandings() {
        val competition = competitionRepository.findByCurrentTrue() ?: return
        val currentFixtures = fixtureService.findFixturesByCompetitionAndManualFalse(competition)
        val teamsToBeAffectedByUpdate = currentFixtures
            .fold(arrayListOf()) { acc: ArrayList<Team?>, fixture: Fixture ->
                with(acc) { addAll(listOf(fixture.homeTeam, fixture.awayTeam)); this }
            }
            .filterNotNull()
        val footballDataFixturesOfCompetition = footballDataService.requestFixtures(competition.id).matches
        val fixturesNewOrChanged = footballDataFixturesOfCompetition
                .filter { it.homeTeam != null && it.awayTeam != null }
                .filter { it.homeTeam?.id != NULL_TEAM_ID && it.awayTeam?.id != NULL_TEAM_ID }
                .map { FootballDataFixtureDto.fromDto(it, teamsToBeAffectedByUpdate, competition) }
                .filter { !currentFixtures.contains(it) }

        if (fixturesNewOrChanged.isNotEmpty()) {
            fixtureService.saveMany(fixturesNewOrChanged)
            bettingGameRepository.findByCompetition(competition).forEach {
                standingService.updateStandings(it)
            }
        }
    }

    @Scheduled(cron = "\${tippspiel2.football-data.cron-expression-current-competition}", zone = "CET")
    @Transactional
    fun updateCurrentCompetitionWithItsTeams() {
        val currentCompetition = competitionRepository.findByCurrentTrue() ?: return
        val footballDataCompetition = FootballDataCompetitionDto.fromDto(
                footballDataService.requestCompetition(currentCompetition.id),
                true, currentCompetition.championBetAllowed, currentCompetition.champion)

        if (currentCompetition != footballDataCompetition) competitionRepository.save(footballDataCompetition)

        val teamsOfCurrentCompetition = teamRepository.findByCompetition(currentCompetition)

        val footballDataTeams = footballDataService.requestTeams(currentCompetition.id).teams
                .map { FootballDataTeamDto.fromDto(it, footballDataCompetition) }

        teamRepository.saveAll(footballDataTeams.filter { !teamsOfCurrentCompetition.contains(it) })
    }
}
