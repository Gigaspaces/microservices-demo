package com.gigaspaces.order.model;

public enum OrderStatus {
    PENDING_PREPARATION, PREPARING, PREPARATION_DONE,
    PENDING_DELIVERY, DELIVERING, DELIVERY_DONE;

    public static Status toStatus(OrderStatus orderStatus) {

        switch (orderStatus) {
            case PENDING_PREPARATION:
                return Status.PENDING_PREPARATION;
            case PREPARING:
                return Status.PREPARING;
            case PREPARATION_DONE:
                return Status.PREPARATION_DONE;
            case PENDING_DELIVERY:
                return Status.PENDING_DELIVERY;
            case DELIVERING:
                return Status.DELIVERING;
            case DELIVERY_DONE:
                return Status.DELIVERY_DONE;
        }

        throw new IllegalArgumentException("Unknown status : " + orderStatus);
    }

    public static OrderStatus fromStatus(Status status) {

        switch (status) {
            case PENDING_PREPARATION:
                return OrderStatus.PENDING_PREPARATION;
            case PREPARING:
                return OrderStatus.PREPARING;
            case PREPARATION_DONE:
                return OrderStatus.PREPARATION_DONE;
            case PENDING_DELIVERY:
                return OrderStatus.PENDING_DELIVERY;
            case DELIVERING:
                return OrderStatus.DELIVERING;
            case DELIVERY_DONE:
                return OrderStatus.DELIVERY_DONE;
        }

        throw new IllegalArgumentException("Unknown status : " + status);
    }

}
