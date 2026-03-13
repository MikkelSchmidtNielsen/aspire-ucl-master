package daprchallenge.pizzastorefront.clients;

import daprchallenge.pizzastorefront.interfaces.DeliveryClient;
import daprchallenge.pizzastorefront.models.Order;
import io.dapr.client.DaprClient;
import io.dapr.client.domain.HttpExtension;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DeliveryClientImpl implements DeliveryClient {
    private static final String APP_ID = "pizza-delivery";
    private static final String METHOD = "delivery";

    private final DaprClient daprClient;

    public DeliveryClientImpl(DaprClient daprClient) {
        this.daprClient = daprClient;
    }

    @Override
    public Mono<Order> sendToDelivery(Order order) {
        return daprClient.invokeMethod(APP_ID, METHOD, order, HttpExtension.POST, Order.class);
    }
}
