package com.github.zemke.tippspiel2.view.exception.handler

import com.github.zemke.tippspiel2.view.exception.AbstractRestException
import com.github.zemke.tippspiel2.view.model.RestError
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*


@ControllerAdvice
@RestController
class CustomResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(AbstractRestException::class)
    fun handleAllExceptions(ex: AbstractRestException, request: WebRequest): ResponseEntity<*> {
        val restError = RestError(
                message = ex.message,
                path = (request as ServletWebRequest).request.requestURI,
                status = ex.status.value(),
                timestamp = Date(),
                locKey = ex.locKey
        )
        return ResponseEntity.status(ex.status).body(restError)
    }
}
