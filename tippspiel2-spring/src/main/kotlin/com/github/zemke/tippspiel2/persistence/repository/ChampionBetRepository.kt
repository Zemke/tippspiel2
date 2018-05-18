package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.ChampionBet
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Team
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChampionBetRepository : JpaRepository<ChampionBet, Long> {

    fun findByBettingGame(bettingGame: BettingGame): List<ChampionBet>

    fun findByCompetitionAndTeam(competition: Competition, competitionChampion: Team): List<ChampionBet>
}
