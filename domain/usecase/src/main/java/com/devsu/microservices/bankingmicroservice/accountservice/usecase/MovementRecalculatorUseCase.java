package com.devsu.microservices.bankingmicroservice.accountservice.usecase;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import com.devsu.microservices.bankingmicroservice.accountservice.model.gateway.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovementRecalculatorUseCase {

    private final MovementRepository movementRepository;

    public Mono<Void> recalculateAll(Account account) {
        return movementRepository
                .getAllMovementsByAccountIdOrderByTimestampAsc(account.getAccountId())
                .collectList()
                .flatMapMany(list -> {
                    double running = account.getInitialBalance();
                    List<Movement> updated = new ArrayList<>(list.size());
                    for (Movement m : list) {
                        double delta = ("CREDIT".equalsIgnoreCase(m.getMovementType()) ||
                                "DEPOSIT".equalsIgnoreCase(m.getMovementType()))
                                ? m.getValue()
                                : -m.getValue();
                        running += delta;
                        updated.add(m.toBuilder().balance(running).build());
                    }
                    return Flux.fromIterable(updated);
                })
                .flatMap(movementRepository::updateMovement)
                .then();
    }

}
