package com.github.zemke.tippspiel2.view.exception

import org.springframework.http.HttpStatus

class NotFoundException(message: String, locKey: String) : AbstractRestException(message, locKey, HttpStatus.NOT_FOUND)
