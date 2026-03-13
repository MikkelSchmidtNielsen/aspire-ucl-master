package daprchallenge.pizzaworkflow.interfaces;

import daprchallenge.pizzaworkflow.models.Order;
import reactor.core.publisher.Mono;

public interface DeliveryClient {
    Mono<Order> sendToDelivery(Order order);
}
