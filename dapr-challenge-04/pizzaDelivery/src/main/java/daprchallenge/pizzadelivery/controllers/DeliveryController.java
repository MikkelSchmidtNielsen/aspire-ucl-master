package daprchallenge.pizzadelivery.controllers;

import daprchallenge.pizzadelivery.interfaces.DeliveryService;
import daprchallenge.pizzadelivery.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {
    private final Logger logger = LoggerFactory.getLogger(DeliveryController.class);
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public Mono<ResponseEntity<Order>> deliver(@RequestBody Order order) {
        logger.info("Starting delivery for order: {}", order.getOrderId());
        var result = deliveryService.deliverPizza(order)
                .map(ResponseEntity::ok);

        return result;
    }
}
