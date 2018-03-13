package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.Standing
import com.github.zemke.tippspiel2.persistence.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StandingRepository : JpaRepository<Standing, Long> {

    fun findByUserInAndBettingGameIn(users: List<User>, bettingGames: List<BettingGame>): List<Standing>
}
