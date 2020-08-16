package com.gigaspaces.ndemo.model;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

import java.util.List;

@SpaceClass
public class Ticket {

    private String orderId;
    private String RestaurantId;
    private List<String> menuItems;
    private OrderStatus status;
    private Boolean withCutlery;
    private Boolean delivery;

    public Ticket() {
    }

    public Ticket(String orderId) {
        this.orderId = orderId;
    }

    @SpaceId
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        RestaurantId = restaurantId;
    }

    public List<String> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<String> menuItems) {
        this.menuItems = menuItems;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Boolean getWithCutlery() {
        return withCutlery;
    }

    public void setWithCutlery(Boolean withCutlery) {
        this.withCutlery = withCutlery;
    }

    public Boolean getDelivery() {
        return delivery;
    }

    public void setDelivery(Boolean delivery) {
        this.delivery = delivery;
    }
}
