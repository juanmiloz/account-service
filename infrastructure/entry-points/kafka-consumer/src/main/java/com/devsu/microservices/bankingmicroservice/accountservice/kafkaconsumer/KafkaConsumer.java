package com.devsu.microservices.bankingmicroservice.accountservice.kafkaconsumer;

import com.devsu.microservices.bankingmicroservice.accountservice.kafkaconsumer.data.NewAccountDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import com.devsu.microservices.bankingmicroservice.accountservice.usecase.AccountCrudUseCase;

@Log4j2
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ReactiveKafkaConsumerTemplate<String, NewAccountDTO> kafkaConsumer;
    private final AccountCrudUseCase accountCrudUseCase;

    @EventListener(ApplicationReadyEvent.class)
    public Flux<Account> consumerRecord(){
        return  kafkaConsumer.receive()
                .flatMap(record -> {
                    NewAccountDTO accountToCreate = record.value();
                    log.info("Received: {}", accountToCreate);
                    return accountCrudUseCase.createAccount(NewAccountDTO.toAccount(accountToCreate));
                })
                .doOnError(e -> log.error("Consumer error: {}", e.getMessage()));
    }

}
