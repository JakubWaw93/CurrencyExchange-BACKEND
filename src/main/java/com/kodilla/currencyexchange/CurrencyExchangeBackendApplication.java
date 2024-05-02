package com.kodilla.currencyexchange;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CurrencyExchangeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyExchangeBackendApplication.class, args);

    }

}
