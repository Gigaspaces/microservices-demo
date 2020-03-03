package com.gigaspaces.ndemo.model;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;
import com.gigaspaces.metadata.index.SpaceIndexType;

@SpaceClass
public class Courier {

    private String id;
    private String name;
    private String phoneNumber;
    private String region;
    private Boolean available;

    public Courier() {
    }

    public Courier(String id, String name, String phoneNumber, String region, Boolean available) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.region = region;
        this.available = available;
    }

    public Courier(String courierId) {
        this.id = courierId;
    }

    @SpaceId
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @SpaceIndex(type = SpaceIndexType.EQUAL)
    public String getRegion() {
        return region;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
