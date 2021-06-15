package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.Bet
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import java.time.Instant

@DataTransferObject
data class BetDto(
    val id: Long,
    val goalsHomeTeamBet: Int,
    val goalsAwayTeamBet: Int,
    val fixture: FixtureDto,
    val user: UserDto,
    val bettingGame: BettingGameDto,
    val modified: Instant,
) {

    companion object {

        fun toDto(bet: Bet) = BetDto(
            id = bet.id!!,
            user = UserDto.toDto(bet.user),
            goalsHomeTeamBet = bet.goalsHomeTeamBet,
            goalsAwayTeamBet = bet.goalsAwayTeamBet,
            fixture = FixtureDto.toDto(bet.fixture),
            bettingGame = BettingGameDto.toDto(bet.bettingGame),
            modified = bet.modified
        )
    }
}
