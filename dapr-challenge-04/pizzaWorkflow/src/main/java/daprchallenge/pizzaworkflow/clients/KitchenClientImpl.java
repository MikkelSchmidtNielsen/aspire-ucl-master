package daprchallenge.pizzaworkflow.clients;

import daprchallenge.pizzaworkflow.interfaces.KitchenClient;
import daprchallenge.pizzaworkflow.models.Order;
import io.dapr.client.DaprClient;
import io.dapr.client.domain.HttpExtension;
import reactor.core.publisher.Mono;

public class KitchenClientImpl implements KitchenClient {
    private static final String APP_ID = "pizza-kitchen";
    private static final String METHOD = "cook";

    private final DaprClient daprClient;

    public KitchenClientImpl(DaprClient daprClient) {
        this.daprClient = daprClient;
    }

    @Override
    public Mono<Order> sendToKitchen(Order order) {
        return daprClient.invokeMethod(APP_ID, METHOD, order, HttpExtension.POST, Order.class);
    }
}
