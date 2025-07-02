package com.devsu.microservices.bankingmicroservice.accountservice.api.data.request;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record NewMovementDTO(

        @Null(message = "Account ID must to be null")
        UUID accountId,

        @NotNull(message = "Date is required.")
        @PastOrPresent(message = "Date cannot be in the future.")
        LocalDateTime timestamp,

        @NotNull(message = "Movement type is required.")
        @Size(max = 30, message = "Account number must be at most 30 characters.")
        String movementType,

        @NotNull(message = "Value is required.")
        Double value,

        @Null(message = "Balance must to be null.")
        Double balance
) {

    public static Movement toMovement(NewMovementDTO dto) {
        return new Movement(
                null,
                dto.accountId(),
                dto.timestamp(),
                dto.movementType(),
                dto.value(),
                null
        );
    }

}
