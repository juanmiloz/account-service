package com.devsu.microservices.bankingmicroservice.accountservice.usecase;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import com.devsu.microservices.bankingmicroservice.accountservice.model.enums.ErrorType;
import com.devsu.microservices.bankingmicroservice.accountservice.model.exceptions.DomainException;
import org.springframework.stereotype.Component;

@Component
public class MovementValidatorUseCase {

    public Movement validateAndCalculate(Movement movement, double currentBalance) {
        double newBal;
        String t = movement.getMovementType().toUpperCase();

        switch (t) {
            case "CREDIT", "DEPOSIT" -> newBal = currentBalance + movement.getValue();
            case "DEBIT", "WITHDRAWAL" -> {
                if (currentBalance < movement.getValue()) {
                    throw new DomainException(ErrorType.BUSINESS_RULE_VIOLATION,
                            "Insufficient funds");
                }
                newBal = currentBalance - movement.getValue();
            }
            default -> throw new DomainException(ErrorType.BUSINESS_RULE_VIOLATION,
                    "Invalid movement type");
        }

        return movement.toBuilder()
                .balance(newBal)
                .build();
    }

}
