package com.devsu.microservices.bankingmicroservice.accountservice.kafkaproducer.producers;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class ClientVerificationProducer {

    private final ReactiveKafkaProducerTemplate<String, Account> kafkaTemplate;
    private static final String TOPIC = "client-verification-request";
    private static final String KEY = "account";

    public Mono<Void> sendVerificationRequest(Account account) {
        ProducerRecord<String, Account> record = new ProducerRecord<>(TOPIC, KEY, account);

        return sendRecord(record);
    }

    private Mono<Void> sendRecord(ProducerRecord<String, Account> record) {
        return kafkaTemplate.send(record).log("kafka-send")
                .doOnError(e ->
                        System.err.println("[KAFKA] error to send: " + e.getMessage())
                )
                .then();
    }

}
