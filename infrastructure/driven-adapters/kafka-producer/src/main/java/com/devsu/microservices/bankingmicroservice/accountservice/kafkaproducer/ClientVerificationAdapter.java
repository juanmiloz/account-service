package com.devsu.microservices.bankingmicroservice.accountservice.kafkaproducer;

import com.devsu.microservices.bankingmicroservice.accountservice.kafkaproducer.producers.ClientVerificationProducer;
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
    private final Map<String, Sinks.One<VerificationResult>> pendingRequests = new ConcurrentHashMap<>();
    private final ReactiveKafkaConsumerTemplate<String, VerificationResult> replyConsumer;

    @Override
    public Mono<VerificationResult> clientVerificationProducer(UUID userId) {
        String correlationId = UUID.randomUUID().toString();

        Sinks.One<VerificationResult> responseSink = Sinks.one();
        pendingRequests.put(correlationId, responseSink);

        return clientVerificationProducer.sendVerificationRequest(userId, correlationId)
                .then(responseSink.asMono())
                .timeout(Duration.ofSeconds(30))
                .doFinally(signal -> pendingRequests.remove(correlationId))
                .doOnError(error -> {
                    log.error("Error in client verification for user {}: {}", userId, error.getMessage());
                    pendingRequests.remove(correlationId);
                });
    }

    @PostConstruct
    public void startListening() {
        log.info("Initializing client verification response consumer...");

        replyConsumer.receive()
                .doOnSubscribe(subscription -> log.info("Consumer subscribed to client verification responses"))
                .doOnNext(record -> log.debug("Received message on topic: {} with key: {}", record.topic(), record.key()))
                .doOnNext(this::handleResponse)
                .doOnError(error -> log.error("Error consuming replies: {}", error.getMessage()))
                .onErrorContinue((error, obj) -> log.warn("Skipping message due to error: {}", error.getMessage()))
                .retry()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    private void handleResponse(ConsumerRecord<String, VerificationResult> record) {
        var correlationHeader = record.headers().lastHeader(KafkaHeaders.CORRELATION_ID);

        if (correlationHeader == null) {
            log.warn("Received response without correlation ID header");
            return;
        }

        String correlationId = new String(correlationHeader.value());

        Sinks.One<VerificationResult> sink = pendingRequests.remove(correlationId);
        if (sink != null) {
            sink.tryEmitValue(record.value());
            log.debug("Response received for correlation ID: {}", correlationId);
        } else {
            log.warn("No pending request found for correlation ID: {}", correlationId);
        }
    }


}
