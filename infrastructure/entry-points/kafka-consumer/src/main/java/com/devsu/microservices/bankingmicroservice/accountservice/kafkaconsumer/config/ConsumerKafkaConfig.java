package com.devsu.microservices.bankingmicroservice.accountservice.kafkaconsumer.config;

import com.devsu.microservices.bankingmicroservice.accountservice.kafkaconsumer.data.NewAccountDTO;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;

@Configuration
public class ConsumerKafkaConfig {

    @Bean
    public ReceiverOptions<String, NewAccountDTO> kafkaReceiverOptions(
            @Value(value = "client-verification-response") String topic,
            KafkaProperties kafkaProperties
    ) throws UnknownHostException {
        kafkaProperties.setClientId(InetAddress.getLocalHost().getHostName());
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();

        return ReceiverOptions
                .<String, NewAccountDTO>create(props)
                .withKeyDeserializer(new StringDeserializer())
                .withValueDeserializer(new JsonDeserializer<>(NewAccountDTO.class, false))
                .subscription(Collections.singleton(topic));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, NewAccountDTO> reactiveKafkaConsumerTemplate(
            ReceiverOptions<String, NewAccountDTO> kafkaReceiverOptions
    ) {
        return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
    }

}
