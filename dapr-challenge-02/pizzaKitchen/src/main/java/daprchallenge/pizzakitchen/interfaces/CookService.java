package daprchallenge.pizzakitchen.interfaces;

import daprchallenge.pizzakitchen.models.Order;
import reactor.core.publisher.Mono;

public interface CookService {
    Mono<Order> cookPizza(Order order);
}
