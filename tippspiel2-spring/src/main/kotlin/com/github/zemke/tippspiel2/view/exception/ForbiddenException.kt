package com.github.zemke.tippspiel2.view.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class ForbiddenException : RuntimeException {

    constructor()
    constructor(message: String) : super(message)
}
