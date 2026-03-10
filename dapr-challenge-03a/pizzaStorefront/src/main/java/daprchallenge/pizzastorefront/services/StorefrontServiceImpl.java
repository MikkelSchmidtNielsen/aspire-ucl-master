package daprchallenge.pizzastorefront.services;

import daprchallenge.pizzastorefront.interfaces.DeliveryClient;
import daprchallenge.pizzastorefront.interfaces.KitchenClient;
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
    private final String PUBSUB_NAME = "pizzapubsub";
    private final String TOPIC_NAME = "orders";

    private final DaprClient daprClient;
    private final KitchenClient kitchenClient;
    private final DeliveryClient deliveryClient;
    private final Logger logger = LoggerFactory.getLogger(StorefrontServiceImpl.class);

    public StorefrontServiceImpl(DaprClient daprClient,
                                 KitchenClient kitchenClient,
                                 DeliveryClient deliveryClient) {
        this.daprClient = daprClient;
        this.kitchenClient = kitchenClient;
        this.deliveryClient = deliveryClient;
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

                    return daprClient.publishEvent(PUBSUB_NAME, TOPIC_NAME, order).
                            then(Mono.delay(Duration.ofSeconds(stage.duration())));
                })
                .then(Mono.just(order));

        var result = simulation
                .flatMap(pizzaOrder -> {
                    logger.info("Starting cooking process for order {}", pizzaOrder.getOrderId());

                    return kitchenClient.sendToKitchen(pizzaOrder);
                }).flatMap(cookedOrder -> {
                    logger.info("Starting delivery process for order {}", cookedOrder.getOrderId());

                    return deliveryClient.sendToDelivery(cookedOrder);
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

                    return daprClient.publishEvent(PUBSUB_NAME, TOPIC_NAME, order)
                            .thenReturn(order);
                });

        return result;
    }
}
