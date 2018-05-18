package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.ChampionBet
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.persistence.repository.ChampionBetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChampionBetService(
        @Autowired private var championBetRepository: ChampionBetRepository
) {

    fun find(championBetId: Long): Optional<ChampionBet> =
            championBetRepository.findById(championBetId)

    fun saveChampionBet(championBet: ChampionBet): ChampionBet =
            championBetRepository.save(championBet)

    fun findAll(): List<ChampionBet> =
            championBetRepository.findAll()

    fun findByBettingGame(bettingGame: BettingGame): List<ChampionBet> =
            championBetRepository.findByBettingGame(bettingGame)

    fun findByCompetitionAndTeam(competition: Competition, competitionChampion: Team) =
            championBetRepository.findByCompetitionAndTeam(competition, competitionChampion)

    fun getCompetitionChampionFromFixtures(fixturesOfCompetition: List<Fixture>, finalMatchdayOfCompetition: Int): Team? {
        val finishedFinalGame = fixturesOfCompetition
                .find { it.matchday == finalMatchdayOfCompetition && it.status == FixtureStatus.FINISHED } ?: return null

        if (finishedFinalGame.goalsHomeTeam == null || finishedFinalGame.goalsAwayTeam == null) return null

        return when {
            finishedFinalGame.goalsHomeTeam > finishedFinalGame.goalsAwayTeam -> finishedFinalGame.homeTeam
            finishedFinalGame.goalsAwayTeam > finishedFinalGame.goalsHomeTeam -> finishedFinalGame.awayTeam
            else -> null
        }
    }
}
