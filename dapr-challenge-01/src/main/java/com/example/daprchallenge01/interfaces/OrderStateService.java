package com.example.daprchallenge01.interfaces;

import com.example.daprchallenge01.models.Order;
import reactor.core.publisher.Mono;

public interface OrderStateService {
    Mono<Order> updateOrderState(Order order);
    Mono<Order> getOrder(String orderId);
    Mono<Void> deleteOrder(String orderId);
}
