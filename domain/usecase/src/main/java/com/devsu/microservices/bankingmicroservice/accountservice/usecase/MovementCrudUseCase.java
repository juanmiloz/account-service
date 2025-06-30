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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MovementCrudUseCase {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final BalanceUseCase balanceSvc;
    private final MovementValidatorUseCase validator;
    private final MovementRecalculatorUseCase recalculator;

    public Mono<Movement> createMovement(Movement m) {
        return accountRepository.getAccountById(m.getAccountId())
                .switchIfEmpty(Mono.error(
                        new DomainException(ErrorType.NOT_FOUND, "Account not found")))
                .flatMap(acc ->
                        balanceSvc.getCurrentBalance(m.getAccountId())
                                .map(bal -> validator.validateAndCalculate(m, bal))
                                .flatMap(movementRepository::createMovement)
                );
    }

    public Mono<Movement> getMovementById(UUID id) {
        return movementRepository.getMovementById(id)
                .switchIfEmpty(Mono.error(new DomainException(ErrorType.NOT_FOUND, "Movement not found")));
    }

    public Flux<Movement> getAllMovements() {
        return movementRepository.getAllMovements();
    }

    public Mono<Movement> updateMovement(Movement updated) {
        return movementRepository.getMovementById(updated.getMovementId())
                .switchIfEmpty(Mono.error(
                        new DomainException(ErrorType.NOT_FOUND, "Movement not found")))
                .flatMap(existing ->
                        accountRepository.getAccountById(updated.getAccountId())
                                .switchIfEmpty(Mono.error(
                                        new DomainException(ErrorType.NOT_FOUND, "Account not found")))
                                .flatMap(acc ->
                                        balanceSvc.getBalanceUpTo(
                                                        acc.getAccountId(),
                                                        existing.getTimestamp(),
                                                        existing.getMovementId()
                                                )
                                                .map(bal -> validator.validateAndCalculate(updated, bal))
                                                .flatMap(mv -> movementRepository.updateMovement(
                                                        mv.toBuilder()
                                                                .timestamp(existing.getTimestamp())
                                                                .build()
                                                ))
                                                .flatMap(saved ->
                                                        recalculator.recalculateAll(acc).thenReturn(saved)
                                                )
                                )
                );
    }

    public Mono<Void> recalculateAllBalances(Account account) {
        return movementRepository
                .getAllMovementsByAccountIdOrderByTimestampAsc(account.getAccountId())
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

}
