package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.repository.BettingGameRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BettingGameService(
        @Autowired private var bettingGameRepository: BettingGameRepository
) {
    fun find(bettingGameId: Long): BettingGame = bettingGameRepository.findOne(bettingGameId)
    fun save(bettingGame: BettingGame): BettingGame = bettingGameRepository.save(bettingGame)
}
