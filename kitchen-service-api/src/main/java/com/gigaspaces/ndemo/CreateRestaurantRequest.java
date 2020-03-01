package com.gigaspaces.ndemo;

public class CreateRestaurantRequest {
    private String name;
    private String region;

    public CreateRestaurantRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
