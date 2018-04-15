package com.github.zemke.tippspiel2.persistence.model

import com.github.zemke.tippspiel2.persistence.model.enumeration.UserRole
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Role(

        @Id @GeneratedValue val id: Long?,
        @Enumerated(EnumType.STRING) val name: UserRole
) {
    constructor(name: UserRole) : this(null, name)
}
