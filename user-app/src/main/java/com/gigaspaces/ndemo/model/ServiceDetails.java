package com.gigaspaces.ndemo.model;

public class ServiceDetails {
    private String ID;
    private String Node;
    private String Address;
    private String Datacenter;
    private String ServiceAddress;
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
