package com.example.daprchallenge01.controllers;

import com.example.daprchallenge01.interfaces.OrderStateService;
import com.example.daprchallenge01.models.CloudEvent;
import com.example.daprchallenge01.models.Order;
import io.dapr.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderStateService orderStateService;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderStateService orderStateService) {
        this.orderStateService = orderStateService;
    }

    @PostMapping
    public Mono<ResponseEntity<Order>> createOrder(@RequestBody Order orderRequest)
    {
        logger.info("Received new order: " + orderRequest.getOrderId());

        var result = orderStateService.updateOrderState(orderRequest)
                .map(order -> ResponseEntity.ok(order));

        return result;
    }

    @GetMapping("/{orderId}")
    public Mono<ResponseEntity<Order>> getOrder(@PathVariable String orderId) {
        var result = orderStateService.getOrder(orderId)
                .map(order -> ResponseEntity.ok(order))
                .defaultIfEmpty(ResponseEntity.notFound().build());

        return result;
    }

    @DeleteMapping("/{orderId}")
    public Mono<ResponseEntity<String>> deleteOrder(@PathVariable String orderId) {
        var result = orderStateService.deleteOrder(orderId)
                .thenReturn(ResponseEntity.ok(orderId));

        return result;
    }

    @Topic(name = "orders", pubsubName = "order-pub-sub")
    @PostMapping("/orders-sub")
    public Mono<Void> handleOrderUpdate(@RequestBody CloudEvent<Order> cloudEvent) {
        logger.info("Received order update for order " + cloudEvent.getData().getOrderId());

        var result = orderStateService.updateOrderState(cloudEvent.getData())
                .then();

        return result;
    }
}
