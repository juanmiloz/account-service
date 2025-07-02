package com.devsu.microservices.bankingmicroservice.accountservice.r2dbc;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import com.devsu.microservices.bankingmicroservice.accountservice.model.gateway.MovementRepository;
import com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.helpers.MovementDAOMapper;
import com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.repositories.MovementDAORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public Flux<Movement> getAllMovementsByAccountIdOrderByTimestampAsc(UUID accountId) {
        return movementDAORepository
                .findByAccountIdOrderByTimestampAsc(accountId)
                .map(mapper::toMovement);
    }

    @Override
    public Mono<Movement> updateMovement(Movement movement) {
        return movementDAORepository
                .save(mapper.toMovementDAO(movement))
                .map(mapper::toMovement);
    }

    @Override
    public Flux<Movement> getMovementsByAccountIdAndDateRangeOrderDes(UUID accountId, LocalDateTime startDate, LocalDateTime endDate) {
        return movementDAORepository.findByAccountIdAndDateRangeOrderDes(accountId, startDate, endDate)
                .map(mapper::toMovement);
    }

}
