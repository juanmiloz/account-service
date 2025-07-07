package com.devsu.microservices.bankingmicroservice.accountservice.kafkaconsumer.data;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;

import java.util.UUID;

public record NewAccountDTO(

        String clientName,

        UUID clientId,

        String accountNumber,

        String accountType,

        Double initialBalance,

        Boolean status
) {

    public static Account toAccount(NewAccountDTO accountDTO) {
        return new Account(
                null,
                accountDTO.clientId(),
                accountDTO.clientName(),
                accountDTO.accountNumber(),
                accountDTO.accountType(),
                accountDTO.initialBalance(),
                accountDTO.status()

        );
    }

}
