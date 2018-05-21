package com.github.zemke.tippspiel2.persistence.model

import org.hibernate.validator.constraints.Range
import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(uniqueConstraints = [
    (UniqueConstraint(name = "competition_caption_league_year", columnNames = ["caption", "league", "year"]))])
data class Competition(

        @Id val id: Long,
        @NotBlank val caption: String,
        @NotBlank val league: String,
        @Range(min = 1900, max = 3000) @NotBlank val year: String,
        @Range(min = 0) @NotNull val currentMatchday: Int,
        @Min(1) val numberOfMatchdays: Int,
        @Min(2) val numberOfTeams: Int,
        @Min(1) val numberOfGames: Int,
        @NotNull val lastUpdated: Timestamp
) {

    constructor(id: Long,
                caption: String,
                league: String,
                year: String,
                currentMatchday: Int,
                numberOfMatchdays: Int,
                numberOfTeams: Int,
                numberOfGames: Int,
                lastUpdated: Timestamp,
                current: Boolean = false,
                champion: Team? = null,
                championBetAllowed: Boolean = true) : this(
            id = id,
            caption = caption,
            league = league,
            year = year,
            currentMatchday = currentMatchday,
            numberOfMatchdays = numberOfMatchdays,
            numberOfTeams = numberOfTeams,
            numberOfGames = numberOfGames,
            lastUpdated = lastUpdated
    ) {
        this.current = current
        this.champion = champion
        this.championBetAllowed = championBetAllowed
    }

    /**
     * Currently only one competition can be played at a time.
     */
    var current: Boolean = false
    @ManyToOne var champion: Team? = null
    var championBetAllowed: Boolean = true
}
