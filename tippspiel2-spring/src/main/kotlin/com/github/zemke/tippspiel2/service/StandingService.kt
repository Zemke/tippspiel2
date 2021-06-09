package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.Standing
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.persistence.repository.BetRepository
import com.github.zemke.tippspiel2.persistence.repository.CompetitionRepository
import com.github.zemke.tippspiel2.persistence.repository.FixtureRepository
import com.github.zemke.tippspiel2.persistence.repository.StandingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class StandingService(
        @Autowired private val standingRepository: StandingRepository,
        @Autowired private val betRepository: BetRepository,
        @Autowired private val fixtureRepository: FixtureRepository,
        @Autowired private val championBetService: ChampionBetService,
        @Autowired private val competitionRepository: CompetitionRepository
) {

    /**
     * Full calc of standings based on bets for given competition and [FixtureStatus.FINISHED] fixtures.
     *
     * There've been a few considerations regarding the reliability of the fixtures
     * which led to the following requirements:
     *
     * - Make sure fixtures are processed only once.
     * - Make sure this method is only called for fixtures which have ended.
     * - What to do when there are changes to fixtures after they were believed to have ended?
     *
     * So this made me end up to just do a full re-calc every time.
     * There's probably no way to make it more reliably.
     */
    @Transactional
    fun updateStandings(bettingGame: BettingGame): List<Standing> {
        val bets = betRepository.findByBettingGame(bettingGame)
                .filter { it.fixture.status == FixtureStatus.FINISHED }
        val allStandings = standingRepository.findByBettingGame(bettingGame)
        val standingsWithBets = allStandings
                .filter { bets.map { it.user }.distinct().contains(it.user) }
                .map { it.reset() }

        bets.forEach {
            val standingOfUser = standingsWithBets.find { standing -> standing.user == it.user }
                    ?: return@forEach

            val pointsForBet = calcPoints(
                    it.goalsHomeTeamBet, it.goalsAwayTeamBet,
                    it.fixture.goalsHomeTeam!!, it.fixture.goalsAwayTeam!!)
            standingOfUser.points += pointsForBet
            changeStatsByNewPoints(standingOfUser, pointsForBet)
        }

        val usersWithRightChampionBet =
                bettingGame.competition.champion?.let { champion ->
                    // TODO Champion could not be up-to-date in competition at this time. Champion of the competition could possibibly be derived from the fixture as well.
                    championBetService.findByTeam(champion)
                            .filter { it.bettingGame.competition == bettingGame.competition }
                            .map { it.user.id }
                } ?: emptyList()

        allStandings.forEach { standing ->
            val numberOfFinishedFixturesInCompetition = fixtureRepository.findFixturesByCompetition(bettingGame.competition)
                    .filter { fixture -> fixture.competition === bettingGame.competition }
                    .filter { fixture -> fixture.status == FixtureStatus.FINISHED }
                    .count()

            standing.missedBets = calcMissedBets(standing, numberOfFinishedFixturesInCompetition)
            if (usersWithRightChampionBet.contains(standing.user.id)) standing.points += 10
        }

        return standingRepository.saveAll(standingsWithBets)
    }

    private fun calcMissedBets(standing: Standing, numberOfFixturesInCompetition: Int): Int {
        val numberOfBets = standing.exactBets + standing.goalDifferenceBets + standing.winnerBets + standing.wrongBets
        return numberOfFixturesInCompetition - numberOfBets
    }

    private fun changeStatsByNewPoints(standing: Standing, pointsForBet: Int) {
        when (pointsForBet) {
            5 -> standing.exactBets++
            3 -> standing.goalDifferenceBets++
            1 -> standing.winnerBets++
            0 -> standing.wrongBets++
        }
    }

    private fun calcPoints(homeBet: Int, awayBet: Int, homeActual: Int, awayActual: Int): Int {
        return if (homeBet == homeActual && awayBet == awayActual) {
            5
        } else if (homeBet - awayBet == homeActual - awayActual) {
            3
        } else if ((homeActual > awayActual && homeBet > awayBet)
                || (homeActual < awayActual && homeBet < awayBet)) {
            1
        } else {
            0
        }
    }

    fun findByBettingGame(bettingGame: BettingGame) =
            standingRepository.findByBettingGame(bettingGame)

    fun findAll(): List<Standing> =
            standingRepository.findAll()
}
