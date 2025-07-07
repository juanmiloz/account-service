package com.devsu.microservices.bankingmicroservice.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        com.devsu.microservices.bankingmicroservice.accountservice.kafkaproducer.config.ProducerKafkaConfig.class,
        com.devsu.microservices.bankingmicroservice.accountservice.kafkaconsumer.config.ConsumerKafkaConfig.class
})
public class AccountServiceApplication {
    public static void main(String[] args) {SpringApplication.run(AccountServiceApplication.class, args);}
}


