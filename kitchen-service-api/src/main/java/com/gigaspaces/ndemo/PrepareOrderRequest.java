package com.gigaspaces.ndemo;

import java.io.Serializable;
import java.util.List;

public class PrepareOrderRequest implements Serializable {
    private String orderId;
    private String restaurantId;
    private List<String> menuItems;

    public PrepareOrderRequest() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
}
