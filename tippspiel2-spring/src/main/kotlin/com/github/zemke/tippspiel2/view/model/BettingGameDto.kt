package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import java.time.Instant

data class BettingGameDto(
    val id: Long,
    val name: String,
    val competition: CompetitionDto,
    val invitationToken: String,
    val created: Instant,
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
            created = dto.created,
            name = dto.name
        )
    }
}
