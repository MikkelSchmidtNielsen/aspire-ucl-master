package daprchallenge.pizzaworkflow.models;

import reactor.util.annotation.NonNull;

import java.util.Objects;

public class ManageWorkflowRequest {
    @NonNull
    private String orderId;

    public ManageWorkflowRequest(@NonNull String orderId) {
        setOrderId(orderId);
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(@NonNull String orderId) {
        orderId = Objects.requireNonNull(orderId, "OrderId can't be null");
    }
}
