package com.devsu.microservices.bankingmicroservice.accountservice.api.handlers;

import com.devsu.microservices.bankingmicroservice.accountservice.api.data.request.NewMovementDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.api.data.request.UpdateMovementDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.api.interfaces.MovementHandlerAPI;
import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import com.devsu.microservices.bankingmicroservice.accountservice.usecase.MovementCrudUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@AllArgsConstructor
public class MovementHandler implements MovementHandlerAPI {

    private final MovementCrudUseCase movementCrudUseCase;

    @Override
    public Mono<ServerResponse> createMovement(ServerRequest request) {
        return request.bodyToMono(NewMovementDTO.class)
                .map(NewMovementDTO::toMovement)
                .flatMap(movementCrudUseCase::createMovement)
                .flatMap(movement -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(movement));
    }

    @Override
    public Mono<ServerResponse> getMovementById(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return movementCrudUseCase.getMovementById(id)
                .flatMap(movement -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(movement));
    }

    @Override
    public Mono<ServerResponse> getAllMovements(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(movementCrudUseCase.getAllMovements(), Movement.class);
    }

    @Override
    public Mono<ServerResponse> updateMovement(ServerRequest request) {
        return request.bodyToMono(UpdateMovementDTO.class)
                .map(UpdateMovementDTO::toMovement)
                .flatMap(movementCrudUseCase::updateMovement)
                .flatMap(movement -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(movement));
    }

}
