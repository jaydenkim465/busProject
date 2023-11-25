package com.busteamproject.DTO;

public class Address {
    private String address_name;
    private String region_1depth_name;
    private String region_2depth_name;
    private String region_3depth_name;
    private String x;
    private String y;

    public Address(String address_name, String region_1depth_name, String region_2depth_name, String region_3depth_name, String x, String y) {
        this.address_name = address_name;
        this.region_1depth_name = region_1depth_name;
        this.region_2depth_name = region_2depth_name;
        this.region_3depth_name = region_3depth_name;
        this.x = x;
        this.y = y;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getRegion_1depth_name() {
        return region_1depth_name;
    }

    public void setRegion_1depth_name(String region_1depth_name) {
        this.region_1depth_name = region_1depth_name;
    }

    public String getRegion_2depth_name() {
        return region_2depth_name;
    }

    public void setRegion_2depth_name(String region_2depth_name) {
        this.region_2depth_name = region_2depth_name;
    }

    public String getRegion_3depth_name() {
        return region_3depth_name;
    }

    public void setRegion_3depth_name(String region_3depth_name) {
        this.region_3depth_name = region_3depth_name;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Address{" +
                "address_name='" + address_name + '\'' +
                ", region_1depth_name='" + region_1depth_name + '\'' +
                ", region_2depth_name='" + region_2depth_name + '\'' +
                ", region_3depth_name='" + region_3depth_name + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                '}';
    }
}
