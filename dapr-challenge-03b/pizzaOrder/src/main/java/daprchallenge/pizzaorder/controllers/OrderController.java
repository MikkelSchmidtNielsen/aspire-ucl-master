package daprchallenge.pizzaorder.controllers;

import daprchallenge.pizzaorder.interfaces.OrderStateService;
import daprchallenge.pizzaorder.models.Order;
import io.dapr.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderStateService orderStateService;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderStateService orderStateService) {
        this.orderStateService = orderStateService;
    }

    @PostMapping
    public Mono<ResponseEntity<Order>> createOrder(@RequestBody Order order) {
        logger.info("Received new order: {}", order.getOrderId());

        var result = orderStateService.updateOrderState(order)
                .map(ResponseEntity::ok);

        return result;
    }

    @GetMapping("/{orderId}")
    public Mono<ResponseEntity<Order>> getOrder(@PathVariable String orderId) {
        var order = orderStateService.getOrder(orderId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

        return order;
    }

    @DeleteMapping("/{orderId}")
    public Mono<ResponseEntity<String>> deleteOrder(@PathVariable String orderId) {
        var result = orderStateService.getOrder(orderId)
                .flatMap(existingOrder ->
                        orderStateService.deleteOrder(orderId)
                                .map(ResponseEntity::ok))
                .switchIfEmpty(Mono.defer(() -> {
                    return Mono.just(ResponseEntity.notFound().build());
                }));

        return result;
    }

    @Topic(name = "orders", pubsubName = "pizzapubsub")
    @PostMapping("/orders-sub")
    public Mono<ResponseEntity<Void>> handleOrderUpdate(@RequestBody Order order) {
        logger.info("Received order update for order {}", order.getOrderId());

        // The compiler guesses wrong when using var
        Mono<ResponseEntity<Void>> result = orderStateService.updateOrderState(order)
                .then(Mono.just(ResponseEntity.ok().build()));

        return result;
    }
}
