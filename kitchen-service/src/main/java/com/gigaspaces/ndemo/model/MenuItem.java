package com.gigaspaces.ndemo.model;

import com.gigaspaces.annotation.pojo.SpaceId;

public class MenuItem {
    private String id;
    private String name;
    private String restaurantId;

    public MenuItem() {
    }

    public MenuItem(String name, String restaurantId) {
        this.name = name;
        this.restaurantId = restaurantId;
    }

    @SpaceId(autoGenerate = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
