package com.devsu.microservices.bankingmicroservice.accountservice.api.interfaces;

import com.devsu.microservices.bankingmicroservice.accountservice.api.data.response.AccountStatementReportDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@RequestMapping("/api/report")
public interface ReportAPI {

    @GetMapping()
    Mono<ResponseEntity<AccountStatementReportDTO>> getAccountStatement(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam UUID clientId
    );

}
