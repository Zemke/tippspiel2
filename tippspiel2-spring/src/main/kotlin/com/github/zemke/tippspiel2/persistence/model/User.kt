package com.github.zemke.tippspiel2.persistence.model

import com.github.zemke.tippspiel2.persistence.model.embeddable.FullName
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.FetchType
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
        @Column(unique = true) @NotNull @Email val email: String,
        @Size(min = 60, max = 60) val password: String,
        @NotNull @CreationTimestamp val lastPasswordReset: Timestamp?,
        @ManyToMany(mappedBy = "users") val communities: List<Community>,
        @ManyToMany(fetch = FetchType.EAGER) val roles: List<Role>
) {
    constructor(fullName: FullName, email: String, password: String, roles: List<Role>) :
            this(
                    id = null,
                    fullName = fullName,
                    email = email,
                    password = password,
                    lastPasswordReset = null,
                    communities = emptyList(),
                    roles = roles)
}
