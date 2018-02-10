package com.github.zemke.tippspiel2.persistence.model

import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import java.sql.Timestamp
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Fixture(

        @Id val id: Long?,
        val date: Timestamp,
        @Enumerated(EnumType.STRING) val status: FixtureStatus,
        val matchday: Int,
        val odds: Double?,
        val goalsHomeTeam: Int?,
        val goalsAwayTeam: Int?,
        @ManyToOne(cascade = [CascadeType.MERGE]) val homeTeam: Team,
        @ManyToOne(cascade = [CascadeType.MERGE]) val awayTeam: Team,
        @ManyToOne(cascade = [CascadeType.MERGE]) val competition: Competition
)
