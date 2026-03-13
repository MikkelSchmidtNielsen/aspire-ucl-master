package daprchallenge.pizzastorefront.controllers;

import daprchallenge.pizzastorefront.interfaces.StorefrontService;
import daprchallenge.pizzastorefront.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/storefront")
public class StorefrontController {
    private final Logger logger = LoggerFactory.getLogger(StorefrontController.class);
    private final StorefrontService storefrontService;

    public StorefrontController(StorefrontService storefrontService) {
        this.storefrontService = storefrontService;
    }

    @PostMapping
    public Mono<ResponseEntity<Order>> createOrder(@RequestBody Order order) {
        logger.info("Received new order: {}", order.getOrderId());
        var result = storefrontService.processOrder(order)
                .map(ResponseEntity::ok);

        return result;
    }
}
