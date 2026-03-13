package daprchallenge.pizzaworkflow.clients;

import daprchallenge.pizzaworkflow.interfaces.StorefrontClient;
import daprchallenge.pizzaworkflow.models.Order;
import io.dapr.client.DaprClient;
import io.dapr.client.domain.HttpExtension;
import reactor.core.publisher.Mono;

public class StoreFrontClientImpl implements StorefrontClient {
    private static final String APP_ID = "pizza-storefront";
    private static final String METHOD = "?";

    private final DaprClient daprClient;

    public StoreFrontClientImpl(DaprClient daprClient) {
        this.daprClient = daprClient;
    }

    @Override
    public Mono<Order> sendToStorefront(Order order) {
        return daprClient.invokeMethod(APP_ID, METHOD, order, HttpExtension.POST, Order.class);
    }
}
