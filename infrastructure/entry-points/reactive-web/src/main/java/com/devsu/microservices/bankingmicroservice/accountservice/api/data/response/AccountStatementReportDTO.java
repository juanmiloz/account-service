package com.devsu.microservices.bankingmicroservice.accountservice.api.data.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AccountStatementReportDTO {
    private UUID clientId;
    private String startDate;
    private String endDate;
    private List<AccountSummaryDTO> accountSummaryDTOs;

    @Data
    @Builder
    public static class AccountSummaryDTO {
        private AccountBaseDTO account;
        private List<MovementBaseDTO> movements;
    }

}