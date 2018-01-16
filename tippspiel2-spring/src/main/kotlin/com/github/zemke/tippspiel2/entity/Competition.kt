package com.github.zemke.tippspiel2.entity

import org.hibernate.validator.constraints.NotBlank
import org.hibernate.validator.constraints.Range
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.validation.constraints.Min

@Entity
@Table(uniqueConstraints = [
    (UniqueConstraint(name = "competition_caption_league_year", columnNames = ["caption", "league", "year"]))])
data class Competition(

        @Id val id: Long,
        @NotBlank val caption: String,
        @NotBlank val league: String,
        @Range(min = 1900, max = 3000) @NotBlank val year: String,
        @Min(1) val numberOfMatchdays: Int,
        @Min(2) val numberOfTeams: Int,
        @Min(1) val numberOfGames: Int
)
