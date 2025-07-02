package com.devsu.microservices.bankingmicroservice.accountservice.api.helper;

import com.devsu.microservices.bankingmicroservice.accountservice.api.data.response.AccountBaseDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.api.data.response.AccountDetailDTO;
import com.devsu.microservices.bankingmicroservice.accountservice.model.Account;
import com.devsu.microservices.bankingmicroservice.accountservice.model.Movement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountBaseDTO toAccountBaseDTO(Account account);

    @Mapping(source = "movements", target = "movements")
    AccountDetailDTO toAccountDetailDTO(Account account, List<Movement> movements);

}
