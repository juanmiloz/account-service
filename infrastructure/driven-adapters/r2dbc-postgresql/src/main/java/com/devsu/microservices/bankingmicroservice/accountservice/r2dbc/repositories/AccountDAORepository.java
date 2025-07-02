package com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.repositories;

import com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.data.AccountDAO;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface AccountDAORepository extends ReactiveCrudRepository<AccountDAO, UUID> {
}
