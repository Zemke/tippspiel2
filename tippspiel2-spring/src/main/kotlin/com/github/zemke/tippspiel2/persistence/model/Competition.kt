package com.github.zemke.tippspiel2.persistence.model

import org.hibernate.validator.constraints.Range
import java.time.Instant
import java.util.Objects
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(
    uniqueConstraints = [
        (UniqueConstraint(name = "competition_caption_league_year", columnNames = ["caption", "league", "year"]))
    ]
)
data class Competition(

    @Id val id: Long,
    @NotBlank val caption: String,
    @NotBlank val league: String,
    @Range(min = 1900, max = 3000) @NotBlank val year: Int,
    @Range(min = 0) @NotNull var currentMatchday: Int,
    @NotNull var lastUpdated: Instant,
    /** Currently only one competition can be played at a time. */
    var current: Boolean,
    @ManyToOne var champion: Team? = null,
    var championBetAllowed: Boolean
) {

    override fun hashCode(): Int =
        Objects.hash(id, caption, league, year, currentMatchday, lastUpdated, current, championBetAllowed, champion?.id)
}
