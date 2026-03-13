package daprchallenge.pizzadelivery.services;

import daprchallenge.pizzadelivery.interfaces.DeliveryService;
import daprchallenge.pizzadelivery.models.Order;
import io.dapr.client.DaprClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private final DaprClient daprClient;
    private final String PUBSUB_NAME = "pizzapubsub";
    private final String TOPIC_NAME = "orders";
    private final Logger logger = LoggerFactory.getLogger(DeliveryServiceImpl.class);

    public DeliveryServiceImpl(DaprClient daprClient) {
        this.daprClient = daprClient;
    }

    record Stage(String status, int duration) { }

    @Override
    public Mono<Order> deliverPizza(Order order) {
        var stages = List.of(
                new Stage("delivery_finding_driver", 2),
                new Stage("delivery_driver_assigned", 1),
                new Stage("delivery_picked_up", 2),
                new Stage("delivery_on_the_way", 5),
                new Stage("delivery_arriving", 2),
                new Stage("delivery_at_location", 1)
        );

        var result = Flux.fromIterable(stages)
                .concatMap(stage -> {
                    order.setStatus(stage.status());
                    logger.info("Order {} - {}", order.getOrderId(), stage.status());

                    return daprClient.publishEvent(PUBSUB_NAME, TOPIC_NAME, order)
                            .then(Mono.delay(Duration.ofSeconds(stage.duration())));
                })
                .then(Mono.defer(() -> {
                    order.setStatus("delivered");

                    return daprClient.publishEvent(PUBSUB_NAME, TOPIC_NAME, order)
                            .thenReturn(order);
                }))
                .onErrorResume(e -> {
                    logger.error("Error delivering order {}", order.getOrderId(), e);
                    order.setStatus("delivery_failed");
                    order.setError(e.getMessage());
                    return daprClient.publishEvent(PUBSUB_NAME, TOPIC_NAME, order)
                            .thenReturn(order);
                });

        return result;
    }
}
