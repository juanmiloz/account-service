package com.devsu.microservices.bankingmicroservice.accountservice.api.data.request;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateMovementDTO(

        @NotNull(message = "Movement ID is required.")
        UUID movementId,

        @NotNull(message = "Account ID is required")
        UUID accountId,

        @Null(message = "Timestamp must to be null, the timestamp is static and can't be updated")
        LocalDateTime timestamp,

        @NotNull(message = "Movement type is required.")
        @Size(max = 30, message = "Account number must be at most 30 characters.")
        String movementType,

        @NotNull(message = "Value is required.")
        Double value,

        @Null(message = "Balance must to be null.")
        Double balance

) {

    public static Movement toMovement(UpdateMovementDTO dto) {
        return new Movement(
                dto.movementId(),
                dto.accountId(),
                null,
                dto.movementType(),
                dto.value(),
                dto.balance()
        );
    }

}
