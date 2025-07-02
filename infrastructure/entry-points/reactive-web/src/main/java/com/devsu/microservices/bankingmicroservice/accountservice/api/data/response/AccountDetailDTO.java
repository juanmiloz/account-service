package com.devsu.microservices.bankingmicroservice.accountservice.api.data.response;

import java.util.List;
import java.util.UUID;

public record AccountDetailDTO(
        UUID accountId,
        UUID clientId,
        String clientName,
        String accountNumber,
        String accountType,
        Double initialBalance,
        Boolean status,
        List<MovementBaseDTO> movements
) {
}
