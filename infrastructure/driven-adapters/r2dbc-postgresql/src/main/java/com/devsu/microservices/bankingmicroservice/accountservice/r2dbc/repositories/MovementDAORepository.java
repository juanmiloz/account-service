package com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.repositories;

import com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.data.MovementDAO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface MovementDAORepository extends ReactiveCrudRepository<MovementDAO, UUID> {

    @Query("SELECT * FROM movement WHERE account_id = :accountId ORDER BY timestamp ASC")
    Flux<MovementDAO> findByAccountIdOrderByTimestampAsc(UUID accountId);

    @Query("SELECT * FROM movement WHERE account_id = :accountId AND timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    Flux<MovementDAO> findByAccountIdAndDateRangeOrderDes(UUID accountId, LocalDateTime startDate, LocalDateTime endDate);

}
