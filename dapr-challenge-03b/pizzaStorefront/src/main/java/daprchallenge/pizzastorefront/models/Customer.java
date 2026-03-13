package daprchallenge.pizzastorefront.models;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class Customer {
    @NonNull
    private String name;
    @NonNull
    private String address;
    @NonNull
    private String phone;

    public Customer() { }

    public Customer(String name, String address, String phone) {
        setName(name);
        setAddress(address);
        setPhone(phone);
    }

    public String getName() { return name; }
    public void setName(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name can't be null");
    }

    public String getAddress() { return address; }
    public void setAddress(@NonNull String address) {
        this.address = Objects.requireNonNull(address, "address can't be null");
    }

    public String getPhone() { return phone; }
    public void setPhone(@NonNull String phone) {
        this.phone = Objects.requireNonNull(phone, "phone can't be null");
    }
}
