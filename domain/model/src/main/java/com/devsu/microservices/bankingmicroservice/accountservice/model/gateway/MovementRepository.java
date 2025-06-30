package com.devsu.microservices.bankingmicroservice.accountservice.model.gateway;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface MovementRepository {

    Mono<Movement> createMovement(Movement movement);

    Mono<Movement> getMovementById(UUID movementId);

    Flux<Movement> getAllMovements();

    Flux<Movement> getAllMovementsByAccountIdOrderByTimestampAsc(UUID accountId);

    Mono<Movement> updateMovement(Movement movement);

}
