package com.github.zemke.tippspiel2.persistence.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
data class Standing(

    @Id @GeneratedValue val id: Long?,
    @Min(0) @NotNull var points: Int = 0,
    @Min(0) @NotNull var exactBets: Int = 0,
    @Min(0) @NotNull var goalDifferenceBets: Int = 0,
    @Min(0) @NotNull var winnerBets: Int = 0,
    @Min(0) @NotNull var wrongBets: Int = 0,
    @Min(0) @NotNull var missedBets: Int = 0,
    @ManyToOne(optional = false) val user: User,
    @ManyToOne(optional = false) val bettingGame: BettingGame
) {

    fun reset(): Standing {
        this.points = 0
        this.exactBets = 0
        this.goalDifferenceBets = 0
        this.winnerBets = 0
        this.wrongBets = 0
        this.missedBets = 0
        return this
    }
}
