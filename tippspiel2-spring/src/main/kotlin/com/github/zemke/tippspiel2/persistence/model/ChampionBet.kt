package com.github.zemke.tippspiel2.persistence.model

import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.validation.constraints.NotNull

@Entity
@Table(
    uniqueConstraints = [
        (UniqueConstraint(name = "champion_bet_multiple_bets", columnNames = ["user_id", "betting_game_id"]))
    ]
)
data class ChampionBet(

    @Id @GeneratedValue val id: Long?,
    @ManyToOne(optional = false) val user: User,
    @ManyToOne(optional = false) var team: Team,
    @ManyToOne(optional = false) val bettingGame: BettingGame,
    @UpdateTimestamp @NotNull var modified: Instant
)
