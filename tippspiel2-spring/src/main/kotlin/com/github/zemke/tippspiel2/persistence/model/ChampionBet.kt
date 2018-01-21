package com.github.zemke.tippspiel2.persistence.model

import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(uniqueConstraints = [
    (UniqueConstraint(name = "champion_bet_multiple_bets", columnNames = ["user_id", "betting_game_id"]))])
data class ChampionBet(

        @Id val id: Long,
        @ManyToOne(optional = false) val user: User,
        @ManyToOne(optional = false) val team: Team,
        @ManyToOne(optional = false) val bettingGame: BettingGame,
        @UpdateTimestamp @NotNull val modified: Timestamp
)
