package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.repository.BettingGameRepository
import com.github.zemke.tippspiel2.persistence.repository.StandingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class BettingGameService(
    @Autowired private var bettingGameRepository: BettingGameRepository,
    @Autowired private var standingRepository: StandingRepository
) {

    fun find(bettingGameId: Long): Optional<BettingGame> = bettingGameRepository.findById(bettingGameId)

    fun saveBettingGame(bettingGame: BettingGame): BettingGame = bettingGameRepository.save(bettingGame)

    fun findAll(): List<BettingGame> = bettingGameRepository.findAll()

    fun findMany(bettingGames: List<Long>): List<BettingGame> = bettingGameRepository.findAllById(bettingGames)
}
