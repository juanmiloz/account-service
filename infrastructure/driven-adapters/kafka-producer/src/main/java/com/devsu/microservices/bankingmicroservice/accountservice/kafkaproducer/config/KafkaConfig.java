package com.devsu.microservices.bankingmicroservice.accountservice.kafkaproducer.config;

import com.devsu.microservices.bankingmicroservice.accountservice.model.dto.VerificationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;

@Configuration
public class KafkaConfig {

    @Bean
    public ReactiveKafkaProducerTemplate<String, String> kafkaProducerTemplate(
            KafkaProperties kafkaProperties
    ) {
        SenderOptions<String, String> senderOptions = SenderOptions.create(kafkaProperties.buildProducerProperties());
        return new ReactiveKafkaProducerTemplate<>(senderOptions);
    }

    @Bean
    public ReceiverOptions<String, VerificationResult> kafkaReceiverOptions(
            @Value(value = "client-verification-response") String topic,
            KafkaProperties  kafkaProperties,
            SslBundles sslBundles
    ) throws UnknownHostException {
        kafkaProperties.setClientId(InetAddress.getLocalHost().getHostName());

        ReceiverOptions<String, VerificationResult> receiverOptions = ReceiverOptions.create(
                kafkaProperties.buildConsumerProperties(sslBundles)
        );

        return receiverOptions.subscription(Collections.singleton(topic));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, VerificationResult> reactiveKafkaConsumerTemplate(
            ReceiverOptions<String, VerificationResult> kafkaReceiverOptions
    ){
        return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
    }

}
