package com.github.zemke.tippspiel2.entity

import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.validator.constraints.Range
import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
data class Bet(

        @Id val id: Long,
        @Range(min = 0, max = 20) val goalsHomeTeamBet: Int,
        @Range(min = 0, max = 20) val goalsAwayTeamBet: Int,
        @ManyToOne(optional = false) val fixture: Fixture,
        @ManyToOne(optional = false) val user: User,
        @ManyToOne(optional = false) val bettingGame: BettingGame,
        @UpdateTimestamp @NotNull val modified: Timestamp
)
