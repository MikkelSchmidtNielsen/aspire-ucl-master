package daprchallenge.pizzadelivery.interfaces;

import daprchallenge.pizzadelivery.models.Order;
import reactor.core.publisher.Mono;

public interface DeliveryService {
    Mono<Order> deliverPizza(Order order);
}
