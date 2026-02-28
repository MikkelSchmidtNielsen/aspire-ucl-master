package daprchallenge.pizzakitchen.services;

import daprchallenge.pizzakitchen.interfaces.CookService;
import daprchallenge.pizzakitchen.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class CookServiceImpl implements CookService {
    private final Logger logger = LoggerFactory.getLogger(CookServiceImpl.class);

    record Stage(String status, int duration) {
    }

    @Override
    public Mono<Order> cookPizza(Order order) {
        var stages = List.of(
                new Stage("cooking_preparing_ingredients", 2),
                new Stage("cooking_making_dough", 3),
                new Stage("cooking_adding_toppings", 2),
                new Stage("cooking_baking", 5),
                new Stage("cooking_quality_check", 1)
        );

        var result = Flux.fromIterable(stages)
                .concatMap(stage -> {
                    order.setStatus(stage.status);
                    logger.info("Order {} - {}", order.getOrderId(), stage.status());

                    return Mono.delay(Duration.ofSeconds(stage.duration()));
                })
                .then(Mono.defer(() -> {
                    order.setStatus("cooked");
                    logger.info("Order {} - {}", order.getOrderId(), order.getStatus());
                    return Mono.just(order);
                }))
                .onErrorResume(e -> {
                    logger.error("Error cooking order {}", order.getOrderId(), e);
                    order.setStatus("cooking_failed");
                    order.setError(e.getMessage());
                    return Mono.just(order);
                });

        return result;
    }
}
