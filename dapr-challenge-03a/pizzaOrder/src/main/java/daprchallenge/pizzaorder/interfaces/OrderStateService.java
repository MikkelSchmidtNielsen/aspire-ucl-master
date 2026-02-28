package daprchallenge.pizzaorder.interfaces;

import daprchallenge.pizzaorder.models.Order;
import reactor.core.publisher.Mono;

public interface OrderStateService {
    Mono<Order> updateOrderState(Order order);
    Mono<Order> getOrder(String orderId);
    Mono<String> deleteOrder(String orderId);
}
