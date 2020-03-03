package com.gigaspaces.order.model;

public class UpdateOrderRequest {

    private String orderId;
    private Status status;

    public UpdateOrderRequest() {
    }

    public UpdateOrderRequest(String orderId, Status status) {
        this.orderId = orderId;
        this.status = status;
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
