package com.example.daprchallenge01;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DaprChallenge01Application {

    public static void main(String[] args) {
        SpringApplication.run(DaprChallenge01Application.class, args);
    }

    @Bean
    public DaprClient daprClient() {
        return new DaprClientBuilder().build();
    }
}
