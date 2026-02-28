package com.example.daprchallenge01.models;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class ValidationRequest {
    @NonNull
    private String orderId;
    private boolean approved;

    public ValidationRequest(String orderId, boolean approved) {
        setOrderId(orderId);
        setApproved(approved);
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(@NonNull String orderId) {
        this.orderId = Objects.requireNonNull(orderId, "orderId can't be null");
    }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
