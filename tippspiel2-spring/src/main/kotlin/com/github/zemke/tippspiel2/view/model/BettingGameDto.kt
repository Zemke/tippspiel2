package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import java.util.*

data class BettingGameDto(
        val id: Long,
        val name: String,
        val community: CommunityDto,
        val competition: CompetitionDto,
        val created: Date
) {

    companion object {

        fun toDto(bettingGame: BettingGame): BettingGameDto = BettingGameDto(
                id = bettingGame.id!!,
                name = bettingGame.name,
                community = CommunityDto.toDto(bettingGame.community),
                competition = CompetitionDto.toDto(bettingGame.competition),
                created = bettingGame.created
        )

        fun fromDto(bettingGame: BettingGame): BettingGameDto = BettingGameDto(
                id = bettingGame.id!!,
                competition = CompetitionDto.toDto(bettingGame.competition),
                community = CommunityDto.toDto(bettingGame.community),
                name = bettingGame.name,
                created = bettingGame.created
        )
    }
}
