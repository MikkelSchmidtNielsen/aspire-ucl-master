package daprchallenge.pizzadelivery.interfaces;

import daprchallenge.pizzadelivery.models.Order;
import reactor.core.publisher.Mono;

public interface DeliveryClient {
    Mono<Void> sendToDelivery(Order order);
}
