package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.view.util.DataTransferObject

@DataTransferObject
data class BettingGameCreationDto(
        val name: String,
        val competition: Long
) {

    companion object {

        fun fromDto(dto: BettingGameCreationDto, competition: Competition): BettingGame = BettingGame(
                id = null,
                name = dto.name,
                competition = competition
        )
    }
}
