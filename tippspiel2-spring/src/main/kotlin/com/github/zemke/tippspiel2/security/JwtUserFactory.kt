package com.github.zemke.tippspiel2.security

import com.github.zemke.tippspiel2.entity.User

object JwtUserFactory {

    fun create(user: User): JwtUser {
        return JwtUser(user.id!!, user.fullName.firstName, user.fullName.lastName, user.email,
                user.password, user.lastPasswordReset)
    }
}
