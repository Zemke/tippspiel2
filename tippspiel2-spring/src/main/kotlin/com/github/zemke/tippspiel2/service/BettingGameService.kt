package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.Standing
import com.github.zemke.tippspiel2.persistence.repository.BettingGameRepository
import com.github.zemke.tippspiel2.persistence.repository.StandingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class BettingGameService(
        @Autowired private var bettingGameRepository: BettingGameRepository,
        @Autowired private var standingRepository: StandingRepository
) {

    fun find(bettingGameId: Long): BettingGame = bettingGameRepository.getOne(bettingGameId)

    @Transactional
    fun createBettingGame(bettingGame: BettingGame): BettingGame {
        bettingGameRepository.save(bettingGame)
        standingRepository.saveAll(bettingGame.community.users.map {
            Standing(
                    id = null,
                    points = 0,
                    exactBets = 0,
                    goalDifferenceBets = 0,
                    winnerBets = 0,
                    wrongBets = 0,
                    missedBets = 0,
                    user = it,
                    bettingGame = bettingGame
            )
        })

        return bettingGame
    }
}
