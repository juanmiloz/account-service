package com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.data;

import com.devsu.microservices.bankingmicroservice.accountservice.model.enums.AccountType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table("movement")
public class MovementDAO  {

    @Id
    private UUID movementId;
    private UUID accountId;
    private LocalDateTime timestamp;
    private String movementType;
    private Double value;
    private Double balance;

}
