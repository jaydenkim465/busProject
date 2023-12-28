package com.busteamproject.DTO.address;

public class Documents {
    private String address_name;
    private String x;
    private String y;
    private String address_type;
    private Address address;
    private RoadAddress road_address;

    public String getAddressName() {
        return address_name;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getAddressType() {
        return address_type;
    }

    public Address getAddress() {
        return address;
    }

    public RoadAddress getRoadAddress() {
        return road_address;
    }
}
