package com.devsu.microservices.bankingmicroservice.accountservice.api.data.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record MovementDetailDTO(
        UUID movementId,
        UUID accountId,
        LocalDateTime timestamp,
        String movementType,
        Double value,
        Double balance,
        AccountBaseDTO account
) {
}
