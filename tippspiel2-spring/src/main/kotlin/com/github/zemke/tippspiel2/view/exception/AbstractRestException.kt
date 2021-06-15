package com.github.zemke.tippspiel2.view.exception

import org.springframework.http.HttpStatus

abstract class AbstractRestException(override val message: String, val locKey: String, val status: HttpStatus) : RuntimeException(message)
