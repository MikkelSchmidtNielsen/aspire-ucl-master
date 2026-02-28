package com.example.daprchallenge01.services;

import com.example.daprchallenge01.interfaces.OrderStateService;
import com.example.daprchallenge01.models.Order;
import io.dapr.client.DaprClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class OrderStateServiceImpl implements OrderStateService {
    private final DaprClient daprClient;
    private final Logger logger = LoggerFactory.getLogger(OrderStateServiceImpl.class);;
    private final String STORE_NAME = "pizzastatestore";

    public OrderStateServiceImpl(DaprClient daprClient) {
        this.daprClient = daprClient;
    }

    @Override
    public Mono<Order> updateOrderState(Order order) {

        var stateKey = "order_" + order.getOrderId();

        // Try to get existing state
        var result = daprClient.getState(STORE_NAME, stateKey, Order.class)
                .flatMap(state -> {

                    Order orderToSave = order;

                    if (state.getValue() != null)
                        // Merge new data with existing state
                        orderToSave = mergeOrderStates(state.getValue(), order);

                    final Order finalOrder = orderToSave;

                    // Save updated state
                    return daprClient.saveState(STORE_NAME, stateKey, finalOrder)
                            .doOnSuccess(value -> logger.info("Updated state for order {} - Status: {}",
                                    finalOrder.getOrderId(), finalOrder.getStatus()))
                            .thenReturn(finalOrder);
                })
                .doOnError(e -> logger.error("Error updating state for order {}", order.getOrderId(), e));

        return result;
    }

    @Override
    public Mono<Order> getOrder(String orderId) {
        // Get order from state store by order ID
        var stateKey = "order_" + orderId;
        var result = daprClient.getState(STORE_NAME, stateKey, Order.class)
                .map(state -> state.getValue())

                .doOnSuccess(order -> {
                    if (order == null) {
                        logger.warn("Order {} not found", orderId);
                    }
                })
                .doOnError(e -> logger.error("Error retrieving order {}", orderId, e));

        return result;
    }

    @Override
    public Mono<Void> deleteOrder(String orderId) {
        try {
            var stateKey = "order_" + orderId;

            // Tries to delete the order from the state store
            var result = daprClient.deleteState(STORE_NAME, stateKey);
            logger.info("Deleted state for order {}", orderId);
            return result;
        } catch (Exception e) {
            logger.error("Error deleting order {}", orderId, e);
            throw e;
        }
    }

    private Order mergeOrderStates(Order existing, Order update) {
        // Preserve important fields from existing state
        update.setCustomer(
                Objects.requireNonNullElse(update.getCustomer(), existing.getCustomer())
        );

        update.setPizzaType(
                Objects.requireNonNullElse(update.getPizzaType(), existing.getPizzaType())
        );

        update.setSize(
                Objects.requireNonNullElse(update.getPizzaType(), existing.getPizzaType())
        );

        return update;
    }
}
