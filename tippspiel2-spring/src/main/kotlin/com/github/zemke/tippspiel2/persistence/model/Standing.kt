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
        @Min(0) @NotNull val points: Int = 0,
        @Min(0) @NotNull val exactBets: Int = 0,
        @Min(0) @NotNull val goalDifferenceBets: Int = 0,
        @Min(0) @NotNull val winnerBets: Int = 0,
        @Min(0) @NotNull val wrongBets: Int = 0,
        @Min(0) @NotNull val missedBets: Int = 0,
        @ManyToOne(optional = false) val user: User,
        @ManyToOne(optional = false) val bettingGame: BettingGame
)
