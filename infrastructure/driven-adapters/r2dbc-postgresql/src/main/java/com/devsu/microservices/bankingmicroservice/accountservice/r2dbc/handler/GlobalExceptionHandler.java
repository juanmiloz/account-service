package com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.handler;

import com.devsu.microservices.bankingmicroservice.accountservice.api.data.response.ErrorResponse;
import com.devsu.microservices.bankingmicroservice.accountservice.model.exceptions.DomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DomainException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleDomainException(DomainException ex) {
        LOGGER.error("DomainException occurred: {}", ex.getMessage());
        LOGGER.debug("Stacktrace: ", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorType().getHttpStatus().value(),
                ex.getErrorType().getHttpStatus().getReasonPhrase(),
                ex.getMessage()
        );

        return Mono.just(
                ResponseEntity
                        .status(ex.getErrorType().getHttpStatus())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(errorResponse)
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception ex) {
        LOGGER.error("Exception occurred: {}", ex.getMessage(), ex);
        LOGGER.debug("Stacktrace: ", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                500,
                "INTERNAL_ERROR",
                ex.getMessage()
        );

        return Mono.just(
                ResponseEntity
                        .status(500)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(errorResponse)
        );
    }

}
