package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.Competition
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BettingGameRepository : JpaRepository<BettingGame, Long> {

    fun findByCompetition(competition: Competition): List<BettingGame>
}
