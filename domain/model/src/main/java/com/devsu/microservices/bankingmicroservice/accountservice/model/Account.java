package com.devsu.microservices.bankingmicroservice.accountservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Account {

    private UUID accountId;
    private UUID clientId;
    private String clientName;
    private String accountNumber;
    private String accountType;
    private Double initialBalance;
    private Boolean status;

}
