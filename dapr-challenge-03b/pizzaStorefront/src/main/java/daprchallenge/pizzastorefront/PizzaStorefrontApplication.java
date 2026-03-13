package daprchallenge.pizzastorefront;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PizzaStorefrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzaStorefrontApplication.class, args);
    }

    @Bean
    public DaprClient daprClient() {
        return new DaprClientBuilder().build();
    }
}
