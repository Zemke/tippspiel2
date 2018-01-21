package com.github.zemke.tippspiel2.persistence.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.validator.constraints.NotBlank
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
data class BettingGame(

        @Id val id: Long,
        @Column(unique = true) @NotBlank val name: String,
        @ManyToOne(optional = false) val community: Community,
        @ManyToOne(optional = false) val competition: Competition,
        @CreationTimestamp @NotNull val created: Timestamp
)
