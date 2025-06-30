package com.devsu.microservices.bankingmicroservice.accountservice.usecase;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import com.devsu.microservices.bankingmicroservice.accountservice.model.enums.ErrorType;
import com.devsu.microservices.bankingmicroservice.accountservice.model.exceptions.DomainException;
import com.devsu.microservices.bankingmicroservice.accountservice.model.gateway.AccountRepository;
import com.devsu.microservices.bankingmicroservice.accountservice.model.gateway.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MovementCrudUseCase {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;

    public Mono<Movement> createMovement(Movement movement) {
        return accountRepository.getAccountById(movement.getAccountId())
                .switchIfEmpty(Mono.error(new DomainException(ErrorType.NOT_FOUND, "Account not found")))
                .flatMap(account -> getCurrentBalance(movement.getAccountId())
                        .map(currentBalance -> validateAndCalculateBalance(movement, currentBalance))
                        .flatMap(movementRepository::createMovement));
    }

    public Mono<Movement> getMovementById(UUID id) {
        return movementRepository.getMovementById(id)
                .switchIfEmpty(Mono.error(new DomainException(ErrorType.NOT_FOUND, "Movement not found")));
    }

    public Flux<Movement> getAllMovements() {
        return movementRepository.getAllMovements();
    }

    public Mono<Movement> updateMovement(Movement updatedMovement) {
        UUID accountId        = updatedMovement.getAccountId();

        return movementRepository.getMovementById(updatedMovement.getMovementId())
                .switchIfEmpty(Mono.error(new DomainException(ErrorType.NOT_FOUND, "Movement not found")))
                .flatMap(existing ->
                        accountRepository.getAccountById(accountId)
                                .switchIfEmpty(Mono.error(new DomainException(ErrorType.NOT_FOUND, "Account not found")))
                                .flatMap(account ->
                                        getBalanceUpTo(accountId, updatedMovement.getMovementId(), existing.getTimestamp())
                                                .map(currentBalance -> validateAndCalculateBalance(updatedMovement, currentBalance))
                                                .flatMap(toSave ->
                                                        movementRepository.updateMovement(
                                                                toSave.toBuilder()
                                                                        .movementId(updatedMovement.getMovementId())
                                                                        .timestamp(existing.getTimestamp())
                                                                        .build()
                                                        )
                                                )
                                                .flatMap(saved -> recalculateAllBalances(account).thenReturn(saved))
                                )
                );
    }

    private Mono<Double> getCurrentBalance(UUID accountId) {
        Mono<Double> deltaSum = movementRepository.getAllMovements()
                .filter(m -> m.getAccountId().equals(accountId))
                .map(m -> {
                    String t = m.getMovementType().toUpperCase();
                    if (t.equals("CREDIT") || t.equals("DEPOSIT")) {
                        return m.getValue();
                    } else if (t.equals("DEBIT") || t.equals("WITHDRAWAL")) {
                        return -m.getValue();
                    }
                    return 0.0;
                })
                .reduce(Double::sum)
                .defaultIfEmpty(0.0);

        return accountRepository.getAccountById(accountId)
                .flatMap(acc ->
                        deltaSum.map(sum -> acc.getInitialBalance() + sum)
                );
    }

    private Movement validateAndCalculateBalance(Movement movement, Double currentBalance) {
        Double newBalance;

        if ("CREDIT".equalsIgnoreCase(movement.getMovementType()) || "DEPOSIT".equalsIgnoreCase(movement.getMovementType())) {
            newBalance = currentBalance + movement.getValue();
        } else if ("DEBIT".equalsIgnoreCase(movement.getMovementType()) || "WITHDRAWAL".equalsIgnoreCase(movement.getMovementType())) {
            if (currentBalance < movement.getValue()) {
                throw new DomainException(ErrorType.BUSINESS_RULE_VIOLATION, "Insufficient funds");
            }
            newBalance = currentBalance - movement.getValue();
        } else {
            throw new DomainException(ErrorType.BUSINESS_RULE_VIOLATION, "Invalid movement type");
        }

        return movement.toBuilder()
                .balance(newBalance)
                .build();
    }

    private Mono<Double> getCurrentBalanceExcludingMovement(UUID accountId, UUID excludeMovementId) {
        Mono<Double> deltaSum = movementRepository.getAllMovements()
                .filter(m -> m.getAccountId().equals(accountId))
                .filter(m -> !m.getMovementId().equals(excludeMovementId))
                .map(m -> {
                    String t = m.getMovementType().toUpperCase();
                    if (t.equals("CREDIT") || t.equals("DEPOSIT"))    return m.getValue();
                    if (t.equals("DEBIT")  || t.equals("WITHDRAWAL")) return -m.getValue();
                    return 0.0;
                })
                .reduce(Double::sum)
                .defaultIfEmpty(0.0);

        return accountRepository.getAccountById(accountId)
                .flatMap(acc ->
                        deltaSum.map(sum -> acc.getInitialBalance() + sum)
                );
    }

    private Mono<Void> recalculateAllBalances(Account account) {
        UUID accountId = account.getAccountId();

        return movementRepository
                .getAllMovementsByAccountIdOrderByDateAsc(accountId)
                .collectList()
                .flatMapMany(list -> {
                    double running = account.getInitialBalance();
                    List<Movement> toUpdate = new ArrayList<>(list.size());
                    for (Movement m : list) {
                        double delta = ("CREDIT".equalsIgnoreCase(m.getMovementType())   ||
                                "DEPOSIT".equalsIgnoreCase(m.getMovementType()))
                                ? m.getValue()
                                : -m.getValue();
                        running += delta;
                        toUpdate.add(m.toBuilder().balance(running).build());
                    }
                    return Flux.fromIterable(toUpdate);
                })
                .flatMap(movementRepository::updateMovement)
                .then();
    }

    private Mono<Double> getBalanceUpTo(UUID accountId,
                                        UUID excludeMovementId,
                                        LocalDateTime cutOffTimestamp) {

        Mono<Double> deltaSum = movementRepository.getAllMovements()
                .filter(m -> m.getAccountId().equals(accountId))
                .filter(m -> !m.getMovementId().equals(excludeMovementId))
                .filter(m -> !m.getTimestamp().isAfter(cutOffTimestamp))
                .map(m -> {
                    String type = m.getMovementType().toUpperCase();
                    if (type.equals("CREDIT") || type.equals("DEPOSIT")) {
                        return m.getValue();
                    } else if (type.equals("DEBIT") || type.equals("WITHDRAWAL")) {
                        return -m.getValue();
                    }
                    return 0.0;
                })
                .reduce(Double::sum)
                .defaultIfEmpty(0.0);

        return accountRepository.getAccountById(accountId)
                .flatMap(acc -> deltaSum.map(sum -> acc.getInitialBalance() + sum));
    }


}
