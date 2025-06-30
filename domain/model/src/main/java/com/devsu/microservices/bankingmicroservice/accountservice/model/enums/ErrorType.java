package com.devsu.microservices.bankingmicroservice.accountservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found: %s"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request: %s"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized: %s"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error: %s"),
    ALREADY_EXISTS(HttpStatus.CONFLICT, "Resource already exists: %s"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token: %s"),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable entity: %s"),
    INVALID_DATA(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid data: %s" ),
    BUSINESS_RULE_VIOLATION(HttpStatus.BAD_REQUEST, "Business rule violation: %s");

    private final HttpStatus httpStatus;
    private final String description;

}
