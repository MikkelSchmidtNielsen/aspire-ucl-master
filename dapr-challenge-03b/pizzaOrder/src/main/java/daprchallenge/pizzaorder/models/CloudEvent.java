package daprchallenge.pizzaorder.models;

public record CloudEvent(String orderId, String customerId, double totalAmount) {
}
