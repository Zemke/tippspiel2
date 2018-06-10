package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.Bet
import com.github.zemke.tippspiel2.persistence.model.BettingGame
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BetRepository : JpaRepository<Bet, Long> {

    fun findByBettingGame(bettingGame: BettingGame): List<Bet>
}
