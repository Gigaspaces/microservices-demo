package com.gigaspaces.ndemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceDetails {
    @JsonProperty("ID")
    private String ID;
    @JsonProperty("Node")
    private String Node;
    @JsonProperty("Address")
    private String Address;
    @JsonProperty("Datacenter")
    private String Datacenter;
    @JsonProperty("ServiceAddress")
    private String ServiceAddress;
    @JsonProperty("ServicePort")
    private int ServicePort;

    public ServiceDetails() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNode() {
        return Node;
    }

    public void setNode(String node) {
        Node = node;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDatacenter() {
        return Datacenter;
    }

    public void setDatacenter(String datacenter) {
        Datacenter = datacenter;
    }

    public String getServiceAddress() {
        return ServiceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        ServiceAddress = serviceAddress;
    }

    public int getServicePort() {
        return ServicePort;
    }

    public void setServicePort(int servicePort) {
        ServicePort = servicePort;
    }
}
