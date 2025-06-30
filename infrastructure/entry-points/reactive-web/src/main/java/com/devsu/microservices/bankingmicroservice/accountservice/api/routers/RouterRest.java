package com.devsu.microservices.bankingmicroservice.accountservice.api.routers;

import com.devsu.microservices.bankingmicroservice.accountservice.api.handlers.AccountHandler;
import com.devsu.microservices.bankingmicroservice.accountservice.api.handlers.MovementHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<?> accountRoutes(AccountHandler handler) {
        return route(POST("/api/account"), handler::createAccount)
                .andRoute(GET("/api/account/{id}"), handler::getAccountById)
                .andRoute(GET("/api/account"), handler::getAllAccounts)
                .andRoute(PUT("/api/account"), handler::updateAccount);

    }

    @Bean
    public RouterFunction<?> movementRoutes(MovementHandler handler) {
        return route(POST("/api/movement"), handler::createMovement)
                .andRoute(GET("/api/movement/{id}"), handler::getMovementById)
                .andRoute(GET("/api/movement"), handler::getAllMovements)
                .andRoute(PUT("/api/movement"), handler::updateMovement);
    }
}
