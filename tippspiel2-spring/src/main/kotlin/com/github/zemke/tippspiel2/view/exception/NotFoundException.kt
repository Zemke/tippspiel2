package com.github.zemke.tippspiel2.view.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND, reason = "Resource not found.")
class NotFoundException : RuntimeException {

    constructor()
    constructor(message: String) : super(message)
}
