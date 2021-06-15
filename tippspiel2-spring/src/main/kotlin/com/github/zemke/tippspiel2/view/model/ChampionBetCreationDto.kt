package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.ChampionBet
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.model.User
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import java.time.Instant

@DataTransferObject
data class ChampionBetCreationDto(
    val user: Long,
    val team: Long,
    val bettingGame: Long
) {

    companion object {

        fun fromDto(bettingGame: BettingGame, team: Team, user: User): ChampionBet =
            ChampionBet(
                id = null,
                modified = Instant.now(),
                bettingGame = bettingGame,
                team = team,
                user = user
            )
    }
}
