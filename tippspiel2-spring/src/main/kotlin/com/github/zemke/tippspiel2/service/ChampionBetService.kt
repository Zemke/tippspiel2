package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.ChampionBet
import com.github.zemke.tippspiel2.persistence.repository.ChampionBetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChampionBetService(
        @Autowired private var championBetRepository: ChampionBetRepository
) {

    fun find(championBetId: Long): ChampionBet =
            championBetRepository.getOne(championBetId)

    fun saveChampionBet(championBet: ChampionBet): ChampionBet =
            championBetRepository.save(championBet)

    fun findAll(): List<ChampionBet> =
            championBetRepository.findAll()

    fun findByBettingGame(bettingGame: BettingGame): List<ChampionBet> =
            championBetRepository.findByBettingGame(bettingGame)
}
