package daprchallenge.pizzaorder.services;

import daprchallenge.pizzaorder.models.Order;
import daprchallenge.pizzaorder.interfaces.OrderStateService;
import io.dapr.client.DaprClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class OrderStateServiceImpl implements OrderStateService {
    private final Logger logger = LoggerFactory.getLogger(OrderStateServiceImpl.class);
    private final DaprClient daprClient;
    private final String STORE_NAME = "pizzastatestore";

    public OrderStateServiceImpl(DaprClient daprClient) {
        this.daprClient = daprClient;
    }

    @Override
    public Mono<Order> updateOrderState(Order order) {
        var stateKey = "order_" + order.getOrderId();

        var result = daprClient.getState(STORE_NAME, stateKey, Order.class)
                .map(existingState -> {
                    if (existingState.getValue() != null) {
                        Order existingOrder = existingState.getValue();

                        return mergeOrderStatus(existingOrder, order);
                    }

                    return order;
                })
                .flatMap(orderToSave -> {
                    return daprClient.saveState(STORE_NAME, stateKey, orderToSave)
                            .thenReturn(orderToSave);
                })
                .doOnNext(savedOrder ->
                        logger.info("Updated state for order {} - Status: {}", savedOrder.getOrderId(), savedOrder.getStatus())
                )
                .doOnError(e ->
                        logger.error("Error updating state for order {}", order.getOrderId(), e)
                );

        return result;
    }

    @Override
    public Mono<Order> getOrder(String orderId) {
        var stateKey = "order_" + orderId;

        var result = daprClient.getState(STORE_NAME, stateKey, Order.class)
                .flatMap(orderState -> {
                    if (orderState.getValue() == null) {
                        logger.warn("Order {} not found", orderId);
                        return Mono.empty();
                    }

                    return Mono.just(orderState.getValue());
                })
                .doOnError(e ->
                        logger.error("Error retrieving order {}", orderId, e)
                );

        return result;
    }

    @Override
    public Mono<String> deleteOrder(String orderId) {
        var stateKey = "order_" + orderId;

        var result = daprClient.deleteState(STORE_NAME, stateKey)
                .doOnSuccess(v -> {
                    logger.error("Deleted state for order {}", orderId);
                })
                .thenReturn(orderId)
                .doOnError(e ->
                        logger.error("Error deleting order {}", orderId)
                );

        return result;
    }

    private Order mergeOrderStatus(Order existing, Order update) {
        update.setCustomer(update.getCustomer() != null ? update.getCustomer() : existing.getCustomer());
        update.setPizzaType(update.getPizzaType() != null ? update.getPizzaType() : existing.getPizzaType());
        update.setSize(update.getSize() != null ? update.getSize() : existing.getSize());

        return update;
    }
}
