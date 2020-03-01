package com.gigaspaces.ndemo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gigaspaces.annotation.pojo.SpaceId;

public class Restaurant {
    private String id;
    private String name;
    private String region;

    public Restaurant() {
    }

    public Restaurant(String name, String region) {
        this.name = name;
        this.region = region;
    }

    @SpaceId(autoGenerate = true)
    @JsonIgnore
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
