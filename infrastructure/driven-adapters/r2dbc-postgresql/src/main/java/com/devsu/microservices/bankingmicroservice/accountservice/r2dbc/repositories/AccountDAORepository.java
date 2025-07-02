package com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.repositories;

import com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.data.AccountDAO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface AccountDAORepository extends ReactiveCrudRepository<AccountDAO, UUID> {

    @Query("SELECT * FROM account WHERE client_id = :clientId")
    Flux<AccountDAO> findByClientId(UUID clientId);

}
