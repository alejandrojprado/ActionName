package com.mahisoft.kamino.mssearch.config

import com.mahisoft.kamino.commons.exception.ApiError
import com.mahisoft.kamino.mssearch.exception.ExistentRelationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import javax.persistence.EntityNotFoundException

@ControllerAdvice
class ExceptionHandlerAdvise {

    @ExceptionHandler(EntityNotFoundException::class)
    fun handlerColumnNotFoundException(ex: EntityNotFoundException, request: WebRequest): ResponseEntity<ApiError> {
        return ResponseEntity(
                ApiError.createApiError(
                        ex,
                        HttpStatus.NOT_FOUND,
                        HttpStatus.NOT_FOUND.reasonPhrase
                ),
                HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(ExistentRelationException::class)
    fun handlerExistentRelationException(ex: ExistentRelationException, request: WebRequest): ResponseEntity<ApiError> {
        return ResponseEntity(
                ApiError.createApiError(
                        ex,
                        HttpStatus.CONFLICT,
                        HttpStatus.CONFLICT.reasonPhrase
                ),
                HttpStatus.CONFLICT
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMissingRequestBody(ex: HttpMessageNotReadableException): ResponseEntity<*>? {
        return ResponseEntity(
                ApiError.createApiError(
                        ex,
                        HttpStatus.BAD_REQUEST,
                        HttpStatus.BAD_REQUEST.reasonPhrase
                ),
                HttpStatus.BAD_REQUEST
        )
    }
}