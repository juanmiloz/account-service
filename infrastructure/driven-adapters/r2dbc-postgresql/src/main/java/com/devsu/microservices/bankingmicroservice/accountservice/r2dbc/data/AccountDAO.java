package com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.data;

import com.devsu.microservices.bankingmicroservice.accountservice.model.enums.AccountType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Data
@Table("account")
public class AccountDAO {
    @Id
    private UUID accountId;
    private String accountNumber;
    private String clientName;
    private String accountType;
    private Double initialBalance;
    private Boolean status;

}
