package com.devsu.microservices.bankingmicroservice.accountservice.api.data.request;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record NewAccountDTO(

        @NotNull(message = "Client ID is required.")
        UUID clientId,

        @NotBlank(message = "Account number is required.")
        @Size(max = 30, message = "Account number must be at most 30 characters.")
        String accountNumber,

        @NotNull(message = "Account type is required.")
        @Size(max = 30, message = "Account number must be at most 30 characters.")
        String accountType,

        @NotNull(message = "Initial balance is required.")
        @PositiveOrZero(message = "Initial balance must be zero or positive.")
        Double initialBalance,

        @NotNull(message = "Account status is required.")
        Boolean status
) {

    public static Account toAccount(NewAccountDTO accountDTO) {
        return new Account(
                null,
                accountDTO.clientId(),
                null,
                accountDTO.accountNumber(),
                accountDTO.accountType(),
                accountDTO.initialBalance(),
                accountDTO.status()

        );
    }

}
