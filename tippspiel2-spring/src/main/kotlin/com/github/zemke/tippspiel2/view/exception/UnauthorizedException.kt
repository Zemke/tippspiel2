package com.github.zemke.tippspiel2.view.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException : AbstractRestException("Unauthorized", "err.unauthorized", HttpStatus.UNAUTHORIZED)
