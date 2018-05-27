package com.github.zemke.tippspiel2.view.exception

import org.springframework.http.HttpStatus

class BadRequestException(message: String, locKey: String) : AbstractRestException(message, locKey, HttpStatus.BAD_REQUEST)
