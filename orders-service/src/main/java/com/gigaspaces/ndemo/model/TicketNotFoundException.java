package com.gigaspaces.ndemo.model;

public class TicketNotFoundException extends Exception {

    private String orderId;

    public TicketNotFoundException(String id) {
        this.orderId = id;
    }

    public String getOrderId() {
        return orderId;
    }
}
