package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.Standing
import com.github.zemke.tippspiel2.view.util.DataTransferObject

@DataTransferObject
data class StandingDto(
    val id: Long,
    val points: Int,
    val exactBets: Int,
    val goalDifferenceBets: Int,
    val winnerBets: Int,
    val wrongBets: Int,
    val missedBets: Int,
    val user: UserDto,
    val bettingGame: BettingGameDto
) {

    companion object {

        fun toDto(standing: Standing) = StandingDto(
            id = standing.id!!,
            points = standing.points,
            exactBets = standing.exactBets,
            goalDifferenceBets = standing.goalDifferenceBets,
            winnerBets = standing.winnerBets,
            wrongBets = standing.wrongBets,
            missedBets = standing.missedBets,
            user = UserDto.toDto(standing.user),
            bettingGame = BettingGameDto.toDto(standing.bettingGame)
        )
    }
}
