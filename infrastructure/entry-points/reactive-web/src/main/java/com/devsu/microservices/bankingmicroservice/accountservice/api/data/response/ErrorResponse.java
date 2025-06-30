package com.devsu.microservices.bankingmicroservice.accountservice.api.data.response;

public record ErrorResponse(
        int status,
        String errorCode,
        String message
) {
}
