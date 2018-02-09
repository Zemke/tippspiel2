package com.github.zemke.tippspiel2.persistence.model

import org.hibernate.validator.constraints.NotBlank
import org.hibernate.validator.constraints.Range
import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.validation.constraints.Min
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
)
