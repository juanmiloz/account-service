package com.devsu.microservices.bankingmicroservice.accountservice.api.helper;

import com.devsu.microservices.bankingmicroservice.accountservice.api.data.response.MovementBaseDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    MovementBaseDTO toMovementBaseDTO(Movement movement);

}
