package com.devsu.microservices.bankingmicroservice.accountservice.r2dbc;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import com.devsu.microservices.bankingmicroservice.accountservice.model.gateway.AccountRepository;
import com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.helpers.AccountDAOMapper;
import com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.repositories.AccountDAORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class AccountRepositoryAdapter implements AccountRepository {

    private final AccountDAORepository accountDAORepository;
    private final AccountDAOMapper mapper;

    @Override
    public Mono<Account> createAccount(Account account) {
        return accountDAORepository
                .save(mapper.toAccountDAO(account))
                .map(mapper::toAccount);
    }

    @Override
    public Mono<Account> getAccountById(UUID accountId) {
        return accountDAORepository
                .findById(accountId)
                .map(mapper::toAccount);
    }

    @Override
    public Flux<Account> getAllAccounts() {
        return accountDAORepository
                .findAll()
                .map(mapper::toAccount);
    }

    @Override
    public Mono<Account> updateAccount(Account account) {
        return accountDAORepository
                .save(mapper.toAccountDAO(account))
                .map(mapper::toAccount);
    }

    @Override
    public Flux<Account> getAccountsByClientId(UUID clientId) {
        return accountDAORepository.findByClientId(clientId)
                .map(mapper::toAccount);
    }

}
