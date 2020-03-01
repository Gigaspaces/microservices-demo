package com.gigaspaces.order.model;

public class OrderStatus {

    private String orderId;
    private Status status;

    public OrderStatus() {
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
