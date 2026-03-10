package daprchallenge.pizzastorefront.clients;

import daprchallenge.pizzastorefront.interfaces.KitchenClient;
import daprchallenge.pizzastorefront.models.Order;
import io.dapr.client.DaprClient;
import io.dapr.client.domain.HttpExtension;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class KitchenClientImpl implements KitchenClient {
    private static final String APP_ID = "pizza-kitchen";
    private static final String METHOD = "api/orders";

    private final DaprClient daprClient;

    public KitchenClientImpl(DaprClient daprClient) {
        this.daprClient = daprClient;
    }

    @Override
    public Mono<Order> sendToKitchen(Order order) {
        return daprClient.invokeMethod(APP_ID, METHOD, order, HttpExtension.POST, Order.class);
    }
}