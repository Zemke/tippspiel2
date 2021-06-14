package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.ChampionBet
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import java.time.Instant

@DataTransferObject
data class ChampionBetDto(
        val id: Long,
        val user: UserDto,
        val team: TeamDto,
        val bettingGame: BettingGameDto,
        val modified: Instant,
) {

    companion object {

        fun toDto(championBet: ChampionBet): ChampionBetDto =
                ChampionBetDto(
                        id = championBet.id!!,
                        user = UserDto.toDto(championBet.user),
                        team = TeamDto.toDto(championBet.team),
                        bettingGame = BettingGameDto.toDto(championBet.bettingGame),
                        modified = championBet.modified,
                )
    }
}
