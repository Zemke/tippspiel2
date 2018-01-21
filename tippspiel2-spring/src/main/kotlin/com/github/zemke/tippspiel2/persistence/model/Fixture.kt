package com.github.zemke.tippspiel2.persistence.model

import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import java.sql.Timestamp
import javax.persistence.*

@Entity
data class Fixture(

        @Id val id: Long,
        val date: Timestamp,
        @Enumerated(EnumType.STRING) val status: FixtureStatus,
        val matchday: Int,
        val odds: Double,
        val goalsHomeTeam: Int,
        val goalsAwayTeam: Int,
        @ManyToOne val homeTeam: Team,
        @ManyToOne val awayTeam: Team,
        @ManyToOne val competition: Competition
)
