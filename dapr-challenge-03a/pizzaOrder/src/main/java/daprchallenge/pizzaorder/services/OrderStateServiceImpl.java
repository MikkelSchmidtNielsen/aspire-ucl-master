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
        try {
            var stateKey = "order_" + order.getOrderId();

            var existingState = daprClient.getState(STORE_NAME, stateKey, Order.class);
        }
    }

    @Override
    public Mono<Order> getOrder(String orderId) {
        return null;
    }

    @Override
    public Mono<String> deleteOrder(String orderId) {
        return null;
    }
}

//        if (existingState != null)
//        {
//            // Merge new data with existing state
//            order = MergeOrderStates(existingState, order);
//        }
//
//        // Save updated state
//        await _daprClient.SaveStateAsync(STORE_NAME, stateKey, order);
//        _logger.LogInformation("Updated state for order {OrderId} - Status: {Status}",
//                order.OrderId, order.Status);
//
//        return order;
//    }
//    catch (Exception ex)
//    {
//        _logger.LogError(ex, "Error updating state for order {OrderId}", order.OrderId);
//        throw;
//    }
//}
//
//public async Task<Order?> GetOrderAsync(string orderId)
//{
//    try
//    {
//        // Get order from state store by order ID
//        var stateKey = $"order_{orderId}";
//        var order = await _daprClient.GetStateAsync<Order>(STORE_NAME, stateKey);
//
//        if (order == null)
//        {
//            _logger.LogWarning("Order {OrderId} not found", orderId);
//            return null;
//        }
//
//        return order;
//    }
//    catch (Exception ex)
//    {
//        _logger.LogError(ex, "Error retrieving order {OrderId}", orderId);
//        throw;
//    }
//}
//
//public async Task<string?> DeleteOrderAsync(string orderId)
//{
//    try
//    {
//        var stateKey = $"order_{orderId}";
//
//        // Tries to delete the order from the state store
//        await _daprClient.DeleteStateAsync(STORE_NAME, stateKey);
//        _logger.LogInformation("Deleted state for order {OrderId}", orderId);
//        return orderId;
//    }
//    catch (Exception ex)
//    {
//        _logger.LogError(ex, "Error deleting order {OrderId}", orderId);
//        throw;
//    }
//}
//
//private Order MergeOrderStates(Order existing, Order update)
//{
//    // Preserve important fields from existing state
//    update.Customer = update.Customer ?? existing.Customer;
//    update.PizzaType = update.PizzaType ?? existing.PizzaType;
//    update.Size = update.Size ?? existing.Size;
//
//    return update;
//}
//}
