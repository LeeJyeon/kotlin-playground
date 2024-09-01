package com.playground.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handler(
            ex: Exception,
            request: WebRequest
    ): ResponseEntity<String> {
        val body = "An error occurred: ${ex.message}"
        return ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}