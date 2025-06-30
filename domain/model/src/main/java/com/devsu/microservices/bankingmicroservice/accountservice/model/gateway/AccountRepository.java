package com.devsu.microservices.bankingmicroservice.accountservice.model.gateway;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AccountRepository {

    Mono<Account> createAccount(Account account);

    Mono<Account> getAccountById(UUID accountId);

    Flux<Account> getAllAccounts();

    Mono<Account> updateAccount(Account account);

}
