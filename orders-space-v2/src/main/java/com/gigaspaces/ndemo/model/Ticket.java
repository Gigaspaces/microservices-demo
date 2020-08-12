package com.gigaspaces.ndemo.model;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

import java.util.List;

@SpaceClass
public class Ticket {

    private String orderId;
    private Integer routing;
    private String restaurantId;
    private List<String> menuItems;
    private String status;
    private Boolean withCutlery;
    private Boolean delivery;

    public Ticket() {
    }

    public Ticket(String orderId) {
        this.orderId = orderId;
    }

    @SpaceId(autoGenerate = true)
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @SpaceRouting
    public Integer getRouting() {
        return routing;
    }

    public void setRouting(Integer routing) {
        this.routing = routing;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<String> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<String> menuItems) {
        this.menuItems = menuItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
