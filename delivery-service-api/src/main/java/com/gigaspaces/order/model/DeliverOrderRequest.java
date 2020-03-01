package com.gigaspaces.order.model;

public class DeliverOrderRequest {
    private String orderId;
    private String region;

    public DeliverOrderRequest() {
    }

    public DeliverOrderRequest(String orderId, String region) {
        this.orderId = orderId;
        this.region = region;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
