package com.devsu.microservices.bankingmicroservice.accountservice.api.controllers;

import com.devsu.microservices.bankingmicroservice.accountservice.api.data.request.NewMovementDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.api.data.request.UpdateAccountDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.api.data.request.UpdateMovementDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.api.interfaces.AccountAPI;
import com.devsu.microservices.bankingmicroservice.accountservice.api.interfaces.MovementAPI;
import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import com.devsu.microservices.bankingmicroservice.accountservice.usecase.AccountCrudUseCase;
import com.devsu.microservices.bankingmicroservice.accountservice.usecase.MovementCrudUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MovementController implements MovementAPI {

    private final MovementCrudUseCase movementCrudUseCase;

    @Override
    public Mono<ResponseEntity<Movement>> createMovement(@Valid @RequestBody NewMovementDTO dto) {
        return movementCrudUseCase
                .createMovement(NewMovementDTO.toMovement(dto))
                .map(mov -> ResponseEntity.ok().body(mov));
    }

    @Override
    public Mono<ResponseEntity<Movement>> updateMovement(@Valid @RequestBody UpdateMovementDTO dto) {
        return movementCrudUseCase
                .updateMovement(UpdateMovementDTO.toMovement(dto))
                .map(mov -> ResponseEntity.ok().body(mov));
    }

    @Override
    public Mono<ResponseEntity<Movement>> getMovementById(@PathVariable UUID id) {
        return movementCrudUseCase
                .getMovementById(id)
                .map(mov -> ResponseEntity.ok().body(mov));
    }

    @Override
    public Flux<Movement> getAllMovements() {
        return movementCrudUseCase.getAllMovements();
    }

}
