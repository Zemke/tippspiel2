package com.github.zemke.tippspiel2.entity

import com.github.zemke.tippspiel2.entity.embeddable.FullName
import org.hibernate.validator.constraints.Email
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.Size

@Entity(name = "\"user\"")
data class User(

        @Id val id: Long,
        @Embedded val fullName: FullName,
        @Email val email: String,
        @Size(min = 60, max = 60) val password: String
)
