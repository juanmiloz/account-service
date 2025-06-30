package com.devsu.microservices.bankingmicroservice.accountservice.api.interfaces;

import com.devsu.microservices.bankingmicroservice.accountservice.api.data.request.NewMovementDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.api.data.request.UpdateMovementDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequestMapping("/api/movement")
public interface MovementAPI {

    @PostMapping
    Mono<ResponseEntity<Movement>> createMovement(@Valid @RequestBody NewMovementDTO dto);

    @GetMapping("/{id}")
    Mono<ResponseEntity<Movement>> getMovementById(@PathVariable UUID id);

    @GetMapping
    Flux<Movement> getAllMovements();

    @PutMapping
    Mono<ResponseEntity<Movement>> updateMovement(@Valid @RequestBody UpdateMovementDTO dto);

}
