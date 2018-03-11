package com.github.zemke.tippspiel2.persistence.model

import com.github.zemke.tippspiel2.persistence.model.embeddable.FullName
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "\"user\"")
data class User(

        @Id @GeneratedValue val id: Long?,
        @Embedded val fullName: FullName,
        @NotNull @Email val email: String,
        @Size(min = 60, max = 60) val password: String,
        @NotNull @CreationTimestamp val lastPasswordReset: Timestamp?,
        @ManyToMany(mappedBy = "users") val communities: List<Community>
) {
    constructor(fullName: FullName, email: String, password: String) : this(null, fullName, email, password, null, emptyList())
}
