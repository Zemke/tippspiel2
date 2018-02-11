package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.Community
import com.github.zemke.tippspiel2.persistence.model.Competition

data class BettingGameCreationDto(
        val name: String,
        val communityId: Long,
        val competitionId: Long
) {

    companion object {

        fun fromDto(dto: BettingGameCreationDto, community: Community, competition: Competition): BettingGame = BettingGame(
                id = null,
                name = dto.name,
                community = community,
                competition = competition,
                created = null
        )
    }
}
