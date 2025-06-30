package com.devsu.microservices.bankingmicroservice.accountservice.config;

import com.devsu.microservices.bankingmicroservice.accountservice.r2dbc.config.PostgresqlConnectionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PostgresqlConnectionProperties.class)
public class PropertiesConfig {
}
