package daprchallenge.pizzaorder.controllers;

import daprchallenge.pizzaorder.interfaces.OrderStateService;
import daprchallenge.pizzaorder.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cook")
public class OrderController {
    private final OrderStateService orderStateService;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderStateService orderStateService) {
        this.orderStateService = orderStateService;
    }

    @PostMapping
    public Mono<ResponseEntity<Order>> cook(@RequestBody Order orderRequest) {
        logger.info("Starting cooking for order: {}", orderRequest.getOrderId());
        var result = orderStateService.cookPizza(orderRequest)
                .map(ResponseEntity::ok);

        return result;
    }
}
