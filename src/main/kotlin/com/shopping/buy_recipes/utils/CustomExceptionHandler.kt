package com.shopping.buy_recipes.utils

import com.shopping.buy_recipes.dto.ErrorResponse
import com.shopping.buy_recipes.dto.OmsErrorResponse
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class CustomExceptionHandler : ResponseEntityExceptionHandler() {
    companion object : Logging {
        private val log = logger()
    }
    @ExceptionHandler(value = [Exception::class])
    fun handleAnyException(ex: Exception): ResponseEntity<OmsErrorResponse> {
        log.error(ex.stackTraceToString())
        return ResponseEntity(
            OmsErrorResponse(
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message = ex.message,
                error = ErrorResponse(
                    status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    message = ex.message
                )
            ), HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(value = [IllegalStateException::class])
    fun handleIllegalStatusException(ex: IllegalStateException): ResponseEntity<OmsErrorResponse> {
        log.error(ex.stackTraceToString())
        return ResponseEntity(
            OmsErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                message = ex.message,
                error = ErrorResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    message = ex.message
                )
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun handleIllegalStatusException(ex: IllegalArgumentException): ResponseEntity<OmsErrorResponse> {
        log.error(ex.stackTraceToString())
        return ResponseEntity(
            OmsErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                message = ex.message,
                error = ErrorResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    message = ex.message
                )
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(value = [EmptyResultDataAccessException::class])
    fun handleEmptyResult(ex: EmptyResultDataAccessException): ResponseEntity<OmsErrorResponse> {
        log.error(ex.stackTraceToString())
        return ResponseEntity(
            OmsErrorResponse(
                status = HttpStatus.NOT_FOUND.value(),
                message = "Resource not found",
                error = ErrorResponse(
                    status = HttpStatus.NOT_FOUND.value(),
                    message = "Resource not found"
                )
            ), HttpStatus.NOT_FOUND
        )
    }
}
