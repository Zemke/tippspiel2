package com.github.zemke.tippspiel2.persistence.model

import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
data class BettingGame(

        @Id @GeneratedValue val id: Long?,
        @Column(unique = true) @NotBlank val name: String,
        @ManyToOne(optional = false) val community: Community,
        @ManyToOne(optional = false) val competition: Competition,
        @CreationTimestamp @NotNull val created: Timestamp?
)
