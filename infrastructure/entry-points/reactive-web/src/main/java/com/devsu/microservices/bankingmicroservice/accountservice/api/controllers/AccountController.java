package com.devsu.microservices.bankingmicroservice.accountservice.api.controllers;

import com.devsu.microservices.bankingmicroservice.accountservice.api.data.request.NewAccountDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.api.data.request.UpdateAccountDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.api.interfaces.AccountAPI;
import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import com.devsu.microservices.bankingmicroservice.accountservice.usecase.AccountCrudUseCase;
import com.devsu.microservices.bankingmicroservice.accountservice.usecase.MovementValidatorUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountAPI {

    private final AccountCrudUseCase accountCrudUseCase;
    private final MovementValidatorUseCase movementValidatorUseCase;

    @Override
    public Mono<ResponseEntity<Void>> createAccount(NewAccountDTO dto) {
        return accountCrudUseCase
                .validateClientAccount(NewAccountDTO.toAccount(dto))
                .then(
                        Mono.just(ResponseEntity
                                .accepted()
                                .build()
                        )
                );
    }

    @Override
    public Flux<Account> getAllAccounts() {
        return accountCrudUseCase.getAllAccounts();
    }

    @Override
    public Mono<ResponseEntity<Account>> getAccountById(UUID id) {
        return accountCrudUseCase
                .getAccountById(id)
                .map(acc -> ResponseEntity.ok(acc));
    }

    @Override
    public Mono<ResponseEntity<Account>> updateAccount(UpdateAccountDTO dto) {
        return accountCrudUseCase
                .updateAccount(UpdateAccountDTO.toAccount(dto))
                .map(ResponseEntity::ok);
    }

}
