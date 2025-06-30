package com.devsu.microservices.bankingmicroservice.accountservice.r2dbc;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import com.devsu.microservices.bankingmicroservice.accountservice.model.gateway.MovementRepository;
import com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.helpers.MovementDAOMapper;
import com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.repositories.MovementDAORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class MovementRepositoryAdapter implements MovementRepository {

    private final MovementDAORepository movementDAORepository;
    private final MovementDAOMapper mapper;

    @Override
    public Mono<Movement> createMovement(Movement movement) {
        return movementDAORepository
                .save(mapper.toMovementDAO(movement))
                .map(mapper::toMovement);
    }

    @Override
    public Mono<Movement> getMovementById(UUID movementId) {
        return movementDAORepository
                .findById(movementId)
                .map(mapper::toMovement);
    }

    @Override
    public Flux<Movement> getAllMovements() {
        return movementDAORepository
                .findAll()
                .map(mapper::toMovement);
    }

    @Override
    public Flux<Movement> getAllMovementsByAccountIdOrderByDateAsc(UUID accountId) {
        return movementDAORepository
                .findByAccountIdOrderByDateAsc(accountId)
                .map(mapper::toMovement);
    }

    @Override
    public Mono<Movement> updateMovement(Movement movement) {
        return movementDAORepository
                .save(mapper.toMovementDAO(movement))
                .map(mapper::toMovement);
    }
}
