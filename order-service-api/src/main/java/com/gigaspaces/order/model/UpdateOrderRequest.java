package com.gigaspaces.order.model;

public class UpdateOrderRequest {

    private String orderId;
    private Status status;

    public UpdateOrderRequest() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
