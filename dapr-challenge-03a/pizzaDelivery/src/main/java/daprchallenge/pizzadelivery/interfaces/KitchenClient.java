package daprchallenge.pizzadelivery.interfaces;

import daprchallenge.pizzadelivery.models.Order;
import reactor.core.publisher.Mono;

public interface KitchenClient {
    Mono<Order> sendToKitchen(Order order);
}
