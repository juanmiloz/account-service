package com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.helpers;

import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.data.MovementDAO;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface MovementDAOMapper {

    Movement toMovement(MovementDAO movementDAO);

    MovementDAO toMovementDAO(Movement movement);

    default Mono<Movement> toMovement(Mono<MovementDAO> movementDAO) {
        return movementDAO.map(this::toMovement);
    }

    default Mono<MovementDAO> toMovementDAO(Mono<Movement> movement) {
        return movement.map(this::toMovementDAO);
    }

    default Flux<Movement> toMovement(Flux<MovementDAO> movementDAO) {
        return movementDAO.map(this::toMovement);
    }

    default Flux<MovementDAO> toMovementDAO(Flux<Movement> movement) {
        return movement.map(this::toMovementDAO);
    }

}
