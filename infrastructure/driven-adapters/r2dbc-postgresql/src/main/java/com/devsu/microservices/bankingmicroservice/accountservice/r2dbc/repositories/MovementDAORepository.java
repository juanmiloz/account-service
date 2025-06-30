package com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.repositories;

import com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.data.MovementDAO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface MovementDAORepository extends ReactiveCrudRepository<MovementDAO, UUID> {

    @Query("SELECT * FROM movement WHERE account_id = :accountId ORDER BY timestamp ASC")
    Flux<MovementDAO> findByAccountIdOrderByTimestampAsc(UUID accountId);

}
