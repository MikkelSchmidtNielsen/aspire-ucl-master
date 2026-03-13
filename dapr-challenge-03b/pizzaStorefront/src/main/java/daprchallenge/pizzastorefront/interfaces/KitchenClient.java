package daprchallenge.pizzastorefront.interfaces;

import daprchallenge.pizzastorefront.models.Order;
import reactor.core.publisher.Mono;

public interface KitchenClient {
    Mono<Order> sendToKitchen(Order order);
}
