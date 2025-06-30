//package com.devsu.microservices.bankingmicroservice.accountservice.config;
//
//import jakarta.validation.ClockProvider;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.hibernate.validator.HibernateValidator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.Clock;
//
//@Configuration
//public class ValidationConfig {
//
//    @Bean
//    public Validator validator() {
//        ValidatorFactory factory = Validation
//                .byProvider(HibernateValidator.class)
//                .configure()
//                .clockProvider(utcClockProvider())
//                .buildValidatorFactory();
//
//       return factory.getValidator();
//    }
//
//    private ClockProvider utcClockProvider() {
//        return Clock::systemUTC;
//    }
//
//}
