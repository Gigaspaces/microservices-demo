package com.gigaspaces.ndemo;

import java.util.List;

public class GetMenusResponse {
    private List<RestaurantMenu> resturantMenuList;

    public GetMenusResponse() {
    }

    public GetMenusResponse(List<RestaurantMenu> resturantMenuList) {
        this.resturantMenuList = resturantMenuList;
    }

    public List<RestaurantMenu> getResturantMenuList() {
        return resturantMenuList;
    }

    public void setResturantMenuList(List<RestaurantMenu> resturantMenuList) {
        this.resturantMenuList = resturantMenuList;
    }
}
