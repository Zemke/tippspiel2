package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Bet
import com.github.zemke.tippspiel2.persistence.repository.BetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class BetService(
    @Autowired private val betRepository: BetRepository
) {

    fun save(bet: Bet): Bet = betRepository.save(bet)

    fun findAll(): List<Bet> = betRepository.findAll()

    fun find(id: Long): Optional<Bet> = betRepository.findById(id)
}
