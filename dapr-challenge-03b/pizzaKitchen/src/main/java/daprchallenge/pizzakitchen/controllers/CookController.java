package daprchallenge.pizzakitchen.controllers;

import daprchallenge.pizzakitchen.interfaces.CookService;
import daprchallenge.pizzakitchen.models.Order;
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
public class CookController {
    private final CookService cookService;
    private final Logger logger = LoggerFactory.getLogger(CookController.class);

    public CookController(CookService cookService) {
        this.cookService = cookService;
    }

    @PostMapping
    public Mono<ResponseEntity<Order>> cook(@RequestBody Order orderRequest) {
        logger.info("Starting cooking for order: {}", orderRequest.getOrderId());
        var result = cookService.cookPizza(orderRequest)
                .map(ResponseEntity::ok);

        return result;
    }
}
