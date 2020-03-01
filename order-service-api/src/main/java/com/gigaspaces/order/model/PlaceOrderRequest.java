package com.gigaspaces.order.model;

import java.util.List;

public class PlaceOrderRequest {

    private String resturantId;
    private List<String> menuItemsIds;

    public PlaceOrderRequest() {
    }

    public String getResturantId() {
        return resturantId;
    }

    public void setResturantId(String resturantId) {
        this.resturantId = resturantId;
    }

    public List<String> getMenuItemsIds() {
        return menuItemsIds;
    }

    public void setMenuItemsIds(List<String> menuItemsIds) {
        this.menuItemsIds = menuItemsIds;
    }
}
