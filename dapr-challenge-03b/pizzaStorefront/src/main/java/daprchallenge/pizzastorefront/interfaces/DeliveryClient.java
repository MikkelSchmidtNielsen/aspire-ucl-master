package daprchallenge.pizzastorefront.interfaces;

import daprchallenge.pizzastorefront.models.Order;
import reactor.core.publisher.Mono;

public interface DeliveryClient {
    Mono<Order> sendToDelivery(Order order);
}
