package com.github.zemke.tippspiel2.persistence.model

import org.hibernate.annotations.CreationTimestamp
import java.time.Instant
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
data class BettingGame(

    @Id @GeneratedValue val id: Long? = null,
    @Column(unique = true) @NotBlank val name: String,
    @Column(unique = true) @NotBlank val invitationToken: String = UUID.randomUUID().toString(),
    @ManyToOne(optional = false) val competition: Competition,
    @CreationTimestamp @NotNull val created: Instant = Instant.now(),
)
