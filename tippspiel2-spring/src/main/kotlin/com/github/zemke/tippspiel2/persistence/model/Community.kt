package com.github.zemke.tippspiel2.persistence.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.validator.constraints.NotBlank
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.validation.constraints.NotNull

@Entity
data class Community(

        @Id @GeneratedValue val id: Long?,
        @Column(unique = true) @NotBlank val name: String,
        @ManyToMany val users: List<User>,
        @CreationTimestamp @NotNull val created: Timestamp?,
        @UpdateTimestamp @NotNull val modified: Timestamp?
)
