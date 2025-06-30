package com.devsu.microservices.bankingmicroservice.accountservice.api.interfaces;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface MovementHandlerAPI {

    Mono<ServerResponse> createMovement(ServerRequest request);

    Mono<ServerResponse> getMovementById(ServerRequest request);

    Mono<ServerResponse> getAllMovements(ServerRequest request);

    Mono<ServerResponse> updateMovement(ServerRequest request);

}
