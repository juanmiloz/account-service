package com.devsu.microservices.bankingmicroservice.accountservice.api.handlers;

import com.devsu.microservices.bankingmicroservice.accountservice.api.data.request.NewAccountDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.api.data.request.UpdateAccount;
import com.devsu.microservices.bankingmicroservice.accountservice.api.interfaces.AccountHandlerAPI;
import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import com.devsu.microservices.bankingmicroservice.accountservice.usecase.AccountCrudUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountHandler implements AccountHandlerAPI {

    private final AccountCrudUseCase accountCrudUseCase;

    @Override
    public Mono<ServerResponse> createAccount(ServerRequest request) {
        return request.bodyToMono(NewAccountDTO.class)
                .map(NewAccountDTO::toAccount)
                .flatMap(accountCrudUseCase::createAccount)
                .flatMap(account -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(account));
    }

    @Override
    public Mono<ServerResponse> getAccountById(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return accountCrudUseCase.getAccountById(id)
                .flatMap(account -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(account));
    }

    @Override
    public Mono<ServerResponse> getAllAccounts(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(accountCrudUseCase.getAllAccounts(), Account.class);
    }

    @Override
    public Mono<ServerResponse> updateAccount(ServerRequest request) {
        return request.bodyToMono(UpdateAccount.class)
                .map(UpdateAccount::toAccount)
                .flatMap(accountCrudUseCase::updateAccount)
                .flatMap(account -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(account));
    }

}
