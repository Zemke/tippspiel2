package com.github.zemke.tippspiel2.view.exception

import org.springframework.http.HttpStatus

class ForbiddenException(message: String, locKey: String) : AbstractRestException(message, locKey, HttpStatus.FORBIDDEN)
