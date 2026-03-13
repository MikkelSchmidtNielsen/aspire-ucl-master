package daprchallenge.pizzaworkflow.interfaces;

import daprchallenge.pizzaworkflow.models.Order;
import reactor.core.publisher.Mono;

public interface StorefrontClient {
    Mono<Order> sendToStorefront(Order order);
}
