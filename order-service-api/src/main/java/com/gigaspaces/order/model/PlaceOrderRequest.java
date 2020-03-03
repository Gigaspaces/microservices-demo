package com.gigaspaces.order.model;

import java.util.List;

public class PlaceOrderRequest {

    private String restaurantId;
    private List<String> menuItemsIds;

    public PlaceOrderRequest() {
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<String> getMenuItemsIds() {
        return menuItemsIds;
    }

    public void setMenuItemsIds(List<String> menuItemsIds) {
        this.menuItemsIds = menuItemsIds;
    }
}
