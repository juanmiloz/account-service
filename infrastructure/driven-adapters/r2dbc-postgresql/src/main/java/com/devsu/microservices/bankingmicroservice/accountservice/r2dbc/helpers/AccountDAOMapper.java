package com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.helpers;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.data.AccountDAO;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface AccountDAOMapper {

    Account toAccount(AccountDAO accountDAO);
    AccountDAO toAccountDAO(Account account);

    default Mono<Account> toAccount(Mono<AccountDAO> accountDAO) {
        return accountDAO.map(this::toAccount);
    }

    default Mono<AccountDAO> toAccountDAO(Mono<Account> account) {
        return account.map(this::toAccountDAO);
    }

    default Flux<Account> toAccount(Flux<AccountDAO> accountDAO) {
        return accountDAO.map(this::toAccount);
    }

    default Flux<AccountDAO> toAccountDAO(Flux<Account> account) {
        return account.map(this::toAccountDAO);
    }
}
