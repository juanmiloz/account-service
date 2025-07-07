package com.devsu.microservices.bankingmicroservice.accountservice.kafkaproducer;

import com.devsu.microservices.bankingmicroservice.accountservice.kafkaproducer.producers.ClientVerificationProducer;
import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import com.devsu.microservices.bankingmicroservice.accountservice.model.dto.VerificationResult;
import com.devsu.microservices.bankingmicroservice.accountservice.model.gateway.ClientRequestProducer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientVerificationAdapter implements ClientRequestProducer {

    private final ClientVerificationProducer clientVerificationProducer;

    @Override
    public Mono<Void> clientVerificationProducer(Account account) {
        return clientVerificationProducer.sendVerificationRequest(account);
    }

}
