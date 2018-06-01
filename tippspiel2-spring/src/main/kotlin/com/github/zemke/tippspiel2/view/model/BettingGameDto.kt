package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import java.sql.Timestamp
import java.util.*

data class BettingGameDto(
        val id: Long,
        val name: String,
        val competition: CompetitionDto,
        val invitationToken: String,
        val created: Date
) {

    companion object {

        fun toDto(bettingGame: BettingGame): BettingGameDto = BettingGameDto(
                id = bettingGame.id!!,
                name = bettingGame.name,
                competition = CompetitionDto.toDto(bettingGame.competition),
                created = bettingGame.created,
                invitationToken = bettingGame.invitationToken
        )

        fun fromDto(dto: BettingGameDto): BettingGame = BettingGame(
                id = dto.id,
                competition = CompetitionDto.fromDto(dto.competition),
                invitationToken = dto.invitationToken,
                created = Timestamp(dto.created.time),
                name = dto.name
        )
    }
}
