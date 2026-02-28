package daprchallenge.pizzaorder.models;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class Order {
    @NonNull
    private String orderId;
    @NonNull
    private String pizzaType;
    @NonNull
    private String size;
    @NonNull
    private Customer customer;
    private String status = "created";
    private String error;

    public Order() {}

    public Order(String orderId, String pizzaType, String size, Customer customer) {
        setOrderId(orderId);
        setPizzaType(pizzaType);
        setSize(size);
        setCustomer(customer);
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(@NonNull String orderId) {
        this.orderId = Objects.requireNonNull(orderId, "orderId can't be null");
    }

    public String getPizzaType() { return pizzaType; }
    public void setPizzaType(@NonNull String pizzaType) {
        this.pizzaType = Objects.requireNonNull(pizzaType, "pizzaType can't be null");
    }

    public String getSize() { return size; }
    public void setSize(@NonNull String size) {
        this.size = Objects.requireNonNull(size, "size can't be null");
    }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) {
        this.customer = Objects.requireNonNull(customer, "customer can't be null");
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
