package com.devsu.microservices.bankingmicroservice.accountservice.api.controllers;

import com.devsu.microservices.bankingmicroservice.accountservice.api.data.response.AccountStatementReportDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.api.helper.AccountMapper;
import com.devsu.microservices.bankingmicroservice.accountservice.api.helper.MovementMapper;
import com.devsu.microservices.bankingmicroservice.accountservice.api.interfaces.ReportAPI;
import com.devsu.microservices.bankingmicroservice.accountservice.usecase.AccountCrudUseCase;
import com.devsu.microservices.bankingmicroservice.accountservice.usecase.MovementCrudUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReportsController implements ReportAPI {

    private final AccountCrudUseCase accountCrudUseCase;
    private final MovementCrudUseCase movementCrudUseCase;
    private final AccountMapper accountMapper;
    private final MovementMapper movementMapper;

    @Override
    public Mono<ResponseEntity<AccountStatementReportDTO>> getAccountStatement(LocalDate startDate, LocalDate endDate, UUID clientId) {
        return accountCrudUseCase.getAccountsByClientId(clientId)
                .flatMap(account -> movementCrudUseCase.getMovementsByAccountId(account, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX))
                                .collectList()
                                .map(movements -> AccountStatementReportDTO.AccountSummaryDTO.builder()
                                        .account(accountMapper.toAccountBaseDTO(account))
                                        .movements(movements.stream()
                                                .map(movementMapper::toMovementBaseDTO)
                                                .toList())
                                        .build())
                )
                .collectList()
                .map(accountSummaries -> AccountStatementReportDTO.builder()
                        .clientId(clientId)
                        .startDate(startDate.toString())
                        .endDate(endDate.toString())
                        .accountSummaryDTOs(accountSummaries)
                        .build())
                .map(ResponseEntity::ok);
    }

}
