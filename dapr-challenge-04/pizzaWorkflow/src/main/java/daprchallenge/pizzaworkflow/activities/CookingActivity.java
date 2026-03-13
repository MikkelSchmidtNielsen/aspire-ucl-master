package daprchallenge.pizzaworkflow.activities;

import daprchallenge.pizzaworkflow.interfaces.KitchenClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CookingActivity {
    private final KitchenClient kitchenClient;
    private final Logger logger = LoggerFactory.getLogger(CookingActivity.class);

    public CookingActivity(KitchenClient kitchenClient) {
        this.kitchenClient = kitchenClient;
    }

    
}
