package com.devsu.microservices.bankingmicroservice.accountservice.api.interfaces;

import com.devsu.microservices.bankingmicroservice.accountservice.api.data.request.NewAccountDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.api.data.request.UpdateAccountDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequestMapping("/api/account")
public interface AccountAPI {

    @PostMapping
    Mono<ResponseEntity<Void>> createAccount(@Valid @RequestBody NewAccountDTO dto);

    @GetMapping
    Flux<Account> getAllAccounts();

    @GetMapping("/{id}")
    Mono<ResponseEntity<Account>> getAccountById(@PathVariable UUID id);

    @PutMapping("")
    Mono<ResponseEntity<Account>> updateAccount(
            @Valid @RequestBody UpdateAccountDTO dto
    );

}
