package com.devsu.microservices.bankingmicroservice.accountservice.api.data.request;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateAccountDTO(

        @NotBlank(message = "Account id is required.")
        UUID accountId,

        @NotBlank(message = "Client name is required.")
        @Size(max = 100, message = "Client name must be at most 100 characters.")
        String clientName,

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

    public static Account toAccount(UpdateAccountDTO accountDTO) {
        return new Account(
                accountDTO.accountId(),
                accountDTO.clientName(),
                accountDTO.accountNumber(),
                accountDTO.accountType(),
                accountDTO.initialBalance(),
                accountDTO.status()
        );
    }

}
