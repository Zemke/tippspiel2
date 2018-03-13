package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Standing
import com.github.zemke.tippspiel2.persistence.repository.BetRepository
import com.github.zemke.tippspiel2.persistence.repository.FixtureRepository
import com.github.zemke.tippspiel2.persistence.repository.StandingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
open class StandingService(
        @Autowired private val standingRepository: StandingRepository,
        @Autowired private val betRepository: BetRepository,
        @Autowired private val fixtureRepository: FixtureRepository
) {

    /**
     * Full calc of standings.
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
     *
     * TODO Do it per competition.
     */
    @Transactional
    open fun updateStandings(): List<Standing> {
        val bets = betRepository.findAll()
        val standings = standingRepository.findAll()

        bets.forEach {
            val standingOfUser = standings.find { standing -> standing.user == it.user }

            if (standingOfUser == null) {
                // TODO Logging.
                return@forEach
            }

            val pointsForBet = calcPoints(
                    it.goalsHomeTeamBet, it.goalsAwayTeamBet,
                    it.fixture.goalsHomeTeam!!, it.fixture.goalsAwayTeam!!)
            standingOfUser.points += pointsForBet
            changeStatsByNewPoints(standingOfUser, pointsForBet)
        }

        val fixtures = fixtureRepository.findAll()

        standings.forEach {
            val numberOfFixturesInCompetition = fixtures
                    .filter { fixture -> fixture.competition === it.bettingGame.competition }
                    .count()

            it.missedBets = calcMissedBets(it, numberOfFixturesInCompetition)
        }

        return standingRepository.saveAll(standings)
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
}
