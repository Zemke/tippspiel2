package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.Bet
import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.User
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import java.sql.Timestamp

@DataTransferObject
data class BetCreationDto(

        val goalsHomeTeamBet: Int,
        val goalsAwayTeamBet: Int,
        val fixture: Long,
        val user: Long,
        val bettingGame: Long
) {

    companion object {

        fun fromDto(dto: BetCreationDto, fixture: Fixture, user: User, bettingGame: BettingGame, modified: Timestamp): Bet = Bet(
                id = null,
                goalsHomeTeamBet = dto.goalsHomeTeamBet,
                goalsAwayTeamBet = dto.goalsAwayTeamBet,
                fixture = fixture,
                user = user,
                bettingGame = bettingGame,
                modified = modified
        )
    }
}
