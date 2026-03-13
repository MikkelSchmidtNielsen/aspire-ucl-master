package daprchallenge.pizzaworkflow.interfaces;

import daprchallenge.pizzaworkflow.models.Order;
import reactor.core.publisher.Mono;

public interface KitchenClient {
    Mono<Order> sendToKitchen(Order order);
}
