package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Bet
import com.github.zemke.tippspiel2.persistence.repository.BetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class BetService(
        @Autowired private var betRepository: BetRepository
) {

    fun save(bet: Bet): Bet? = betRepository.save(bet)
}
