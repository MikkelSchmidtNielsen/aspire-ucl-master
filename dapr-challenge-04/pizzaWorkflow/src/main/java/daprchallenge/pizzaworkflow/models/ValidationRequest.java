package daprchallenge.pizzaworkflow.models;

import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import reactor.util.annotation.NonNull;

import java.util.Objects;

public class ValidationRequest {
    @NonNull
    private String name;

    private boolean approved;

    public ValidationRequest(String name, boolean approved) {
        setName(name);

    }

    public String getName() { return name; }
    public void setName(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name can't be null");
    }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean value) {
        this.approved = value;
    }
}
