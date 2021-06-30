package com.github.zemke.tippspiel2.persistence.model

import com.github.zemke.tippspiel2.persistence.model.embeddable.FullName
import java.time.Instant
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
    @Embedded var fullName: FullName,
    @Column(unique = true) @NotNull @Email var email: String,
    @Size(min = 60, max = 60) var password: String,
    @NotNull var lastPasswordReset: Instant?,
    @ManyToMany var bettingGames: List<BettingGame> = emptyList(),
    @ManyToMany(fetch = FetchType.EAGER) var roles: List<Role>
) {
    constructor(
        fullName: FullName,
        email: String,
        password: String,
        roles: List<Role>,
        bettingGames: List<BettingGame> = emptyList()
    ) :
        this(
            id = null,
            fullName = fullName,
            email = email,
            password = password,
            lastPasswordReset = null,
            roles = roles,
            bettingGames = bettingGames
        )
}
