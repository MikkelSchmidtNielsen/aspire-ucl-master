package daprchallenge.pizzastorefront.services;

import daprchallenge.pizzastorefront.interfaces.StorefrontService;
import daprchallenge.pizzastorefront.models.Order;
import io.dapr.client.DaprClient;
import io.dapr.client.domain.HttpExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class StorefrontServiceImpl implements StorefrontService {
    private final Logger logger = LoggerFactory.getLogger(StorefrontServiceImpl.class);
    private final DaprClient daprClient;

    public StorefrontServiceImpl(DaprClient daprClient) {
        this.daprClient = daprClient;
    }

    record Stage(String status, int duration) {
    }

    @Override
    public Mono<Order> processOrder(Order order) {
        var stages = List.of(
                new Stage("validating", 1),
                new Stage("processing", 2),
                new Stage("confirmed", 1)
        );

        var simulation = Flux.fromIterable(stages)
                .concatMap(stage -> {
                    order.setStatus(stage.status());
                    logger.info("Order {} - {}", order.getOrderId(), stage.status());

                    return Mono.delay(Duration.ofSeconds(stage.duration()));
                })
                .then(Mono.just(order));

        var result = simulation.flatMap(pizzaOrder -> {
                    logger.info("Starting cooking process for order {}", pizzaOrder.getOrderId());

                    return daprClient.invokeMethod(
                            "pizza-kitchen",
                            "cook",
                            pizzaOrder,
                            HttpExtension.POST,
                            Order.class
                    );
                }).flatMap(cookedOrder -> {
                    logger.info("Starting delivery process for order {}", cookedOrder.getOrderId());

                    return daprClient.invokeMethod(
                            "pizza-delivery",
                            "delivery",
                            cookedOrder,
                            HttpExtension.POST,
                            Order.class
                    );
                })
                .doOnNext(deliveredOrder -> {
                    logger.info("Order {} delivered with status {}",
                            deliveredOrder.getOrderId(),
                            deliveredOrder.getStatus());
                })
                .onErrorResume(e -> {
                    logger.error("Error processing order {}", order.getOrderId(), e);
                    order.setStatus("failed");
                    order.setError(e.getMessage());

                    return Mono.just(order);
                });

        return result;
    }
}
