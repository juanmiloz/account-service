package com.devsu.microservices.bankingmicroservice.accountservice.usecase;

import com.devsu.microservices.bankingmicroservice.accountservice.model.gateway.AccountRepository;
import com.devsu.microservices.bankingmicroservice.accountservice.model.gateway.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;

    public Mono<Double> getBalanceUpTo(UUID accountId,
                                       LocalDateTime cutOff,
                                       UUID excludeMovementId) {
        Mono<Double> delta =
                movementRepository.getAllMovements()
                        .filter(m -> m.getAccountId().equals(accountId))
                        .filter(m -> !m.getMovementId().equals(excludeMovementId))
                        .filter(m -> !m.getTimestamp().isAfter(cutOff))
                        .map(m -> {
                            String t = m.getMovementType().toUpperCase();
                            return (t.equals("CREDIT") || t.equals("DEPOSIT"))
                                    ? m.getValue()
                                    : -m.getValue();
                        })
                        .reduce(Double::sum)
                        .defaultIfEmpty(0.0);

        return accountRepository.getAccountById(accountId)
                .flatMap(acc -> delta.map(sum -> acc.getInitialBalance() + sum));
    }

    public Mono<Double> getCurrentBalance(UUID accountId) {
        return getBalanceUpTo(accountId, LocalDateTime.MAX, null);
    }


}
