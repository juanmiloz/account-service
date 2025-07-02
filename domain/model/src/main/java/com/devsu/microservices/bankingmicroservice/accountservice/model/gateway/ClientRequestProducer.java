package com.devsu.microservices.bankingmicroservice.accountservice.model.gateway;

import com.devsu.microservices.bankingmicroservice.accountservice.model.dto.VerificationResult;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ClientRequestProducer {

    Mono<VerificationResult> clientVerificationProducer(UUID userId);

}
