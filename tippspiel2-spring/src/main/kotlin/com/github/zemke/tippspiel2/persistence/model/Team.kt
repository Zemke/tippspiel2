package com.github.zemke.tippspiel2.persistence.model

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.NotBlank

@Entity
data class Team(

        @Id val id: Long,
        @NotBlank val name: String,
        val squadMarketValue: String?,
        @ManyToOne(optional = false, cascade = [CascadeType.MERGE]) val competition: Competition
)
