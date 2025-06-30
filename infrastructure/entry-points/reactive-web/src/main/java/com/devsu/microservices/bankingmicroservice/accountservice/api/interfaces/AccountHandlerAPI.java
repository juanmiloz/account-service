package com.devsu.microservices.bankingmicroservice.accountservice.api.interfaces;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface AccountHandlerAPI {

    Mono<ServerResponse> createAccount(ServerRequest request);

    Mono<ServerResponse> getAccountById(ServerRequest request);

    Mono<ServerResponse> getAllAccounts(ServerRequest request);

    Mono<ServerResponse> updateAccount(ServerRequest request);

}
