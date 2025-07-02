package com.devsu.microservices.bankingmicroservice.accountservice.kafkaproducer.producers;

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

    private final ReactiveKafkaProducerTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "client-verification-request";
    private static final String KEY = "client-id";
    private static final String REPLY_TOPIC   = "client-verification-response";

    public Mono<Void> sendVerificationRequest(UUID userId) {
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, KEY, userId.toString());

        return sendRecord(record);
    }

    public Mono<Void> sendVerificationRequest(UUID userId, String correlationId) {
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, KEY, userId.toString());
        record.headers().add(KafkaHeaders.REPLY_TOPIC, REPLY_TOPIC.getBytes(StandardCharsets.UTF_8));
        record.headers().add(KafkaHeaders.CORRELATION_ID, correlationId.getBytes(StandardCharsets.UTF_8));

        return sendRecord(record);
    }

    private Mono<Void> sendRecord(ProducerRecord<String, String> record) {
        return kafkaTemplate.send(record).log("kafka-send")
                .doOnError(e ->
                        System.err.println("[KAFKA] error to send: " + e.getMessage())
                )
                .then();
    }

}
