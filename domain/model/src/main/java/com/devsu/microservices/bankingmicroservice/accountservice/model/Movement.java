package com.devsu.microservices.bankingmicroservice.accountservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Movement {

    private UUID movementId;
    private UUID accountId;
    private LocalDateTime timestamp;
    private String movementType;
    private Double value;
    private Double balance;

}
