package com.devsu.microservices.bankingmicroservice.accountservice.model.dto;

public record VerificationResult(boolean verified, String message) {
}
