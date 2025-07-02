package com.devsu.microservices.bankingmicroservice.accountservice.usecase;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import com.devsu.microservices.bankingmicroservice.accountservice.model.enums.ErrorType;
import com.devsu.microservices.bankingmicroservice.accountservice.model.exceptions.DomainException;
import com.devsu.microservices.bankingmicroservice.accountservice.model.gateway.AccountRepository;
import com.devsu.microservices.bankingmicroservice.accountservice.model.gateway.ClientRequestProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AccountCrudUseCase {

    private final AccountRepository accountRepository;
    private final ClientRequestProducer clientRequestProducer;
    private final MovementRecalculatorUseCase movementRecalculationService;

    public Mono<Account> createAccount(Account account) {
        return accountRepository.createAccount(account)
                .flatMap(saved ->
                        movementRecalculationService
                                .recalculateAll(saved)
                                .thenReturn(saved)
                );
    }

    public Mono<Account> getAccountById(UUID id) {
        return accountRepository.getAccountById(id)
                .switchIfEmpty(Mono.error(new DomainException(ErrorType.NOT_FOUND, "Account not found" )));
    }

    public Flux<Account> getAllAccounts() {
        return accountRepository.getAllAccounts();
    }

    public Mono<Account> updateAccount(Account account) {
        return accountRepository.getAccountById(account.getAccountId())
                .switchIfEmpty(
                        Mono.error(new DomainException(
                                ErrorType.NOT_FOUND, "Account not found"))
                )
                .flatMap(existing ->
                        accountRepository.updateAccount(account.toBuilder().clientId(existing.getClientId()).build())
                                .flatMap(updatedAccount ->
                                        movementRecalculationService
                                                .recalculateAll(updatedAccount)
                                                .thenReturn(updatedAccount)
                                )
                );
    }

    public Flux<Account> getAccountsByClientId(UUID clientId) {
        return accountRepository.getAccountsByClientId(clientId);
    }

}
