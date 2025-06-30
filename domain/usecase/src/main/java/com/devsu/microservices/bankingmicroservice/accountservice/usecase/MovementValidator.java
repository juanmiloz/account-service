package com.devsu.microservices.bankingmicroservice.accountservice.usecase;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import com.devsu.microservices.bankingmicroservice.accountservice.model.enums.ErrorType;
import com.devsu.microservices.bankingmicroservice.accountservice.model.exceptions.DomainException;
import org.springframework.stereotype.Component;

@Component
public class MovementValidator {

    public Movement validateAndCalculate(Movement m, double currentBalance) {
        double newBal;
        String t = m.getMovementType().toUpperCase();

        switch (t) {
            case "CREDIT", "DEPOSIT" -> newBal = currentBalance + m.getValue();
            case "DEBIT", "WITHDRAWAL" -> {
                if (currentBalance < m.getValue()) {
                    throw new DomainException(ErrorType.BUSINESS_RULE_VIOLATION,
                            "Insufficient funds");
                }
                newBal = currentBalance - m.getValue();
            }
            default -> throw new DomainException(ErrorType.BUSINESS_RULE_VIOLATION,
                    "Invalid movement type");
        }

        return m.toBuilder()
                .balance(newBal)
                .build();
    }

}
