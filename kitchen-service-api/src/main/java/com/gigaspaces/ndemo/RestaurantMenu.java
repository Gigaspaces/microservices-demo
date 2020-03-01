package com.gigaspaces.ndemo;

import java.util.Map;

public class RestaurantMenu {
    private String restaurantId;
    private String restaurantName;
    private Map<String, String> menuItems;

    public RestaurantMenu() {
    }

    public RestaurantMenu(String restaurantId, String restaurantName, Map<String, String> menuItems) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.menuItems = menuItems;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Map<String, String> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(Map<String, String> menuItems) {
        this.menuItems = menuItems;
    }
}
