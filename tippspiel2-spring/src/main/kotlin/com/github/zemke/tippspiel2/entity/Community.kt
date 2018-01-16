package com.github.zemke.tippspiel2.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.validator.constraints.NotBlank
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotNull

@Entity
data class Community(

        @Id val id: Long,
        @Column(unique = true) @NotBlank val name: String,
        @CreationTimestamp @NotNull val created: Timestamp,
        @UpdateTimestamp @NotNull val modified: Timestamp
)
