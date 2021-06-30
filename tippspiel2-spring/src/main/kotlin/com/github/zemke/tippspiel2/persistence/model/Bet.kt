package com.github.zemke.tippspiel2.persistence.model

import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.validator.constraints.Range
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
data class Bet(

    @Id @GeneratedValue val id: Long?,
    @Range(min = 0, max = 20) var goalsHomeTeamBet: Int,
    @Range(min = 0, max = 20) var goalsAwayTeamBet: Int,
    @ManyToOne(optional = false) val fixture: Fixture,
    @ManyToOne(optional = false) val user: User,
    @ManyToOne(optional = false) val bettingGame: BettingGame,
    @UpdateTimestamp @NotNull var modified: Instant = Instant.now(),
)
