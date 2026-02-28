package daprchallenge.pizzastorefront.interfaces;

import daprchallenge.pizzastorefront.models.Order;
import reactor.core.publisher.Mono;

public interface StorefrontService {
    Mono<Order> processOrder(Order order);
}
