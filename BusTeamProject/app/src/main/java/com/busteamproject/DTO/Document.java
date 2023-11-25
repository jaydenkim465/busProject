package com.busteamproject.DTO;

import com.busteamproject.DTO.Address;
import org.json.JSONObject;

public class Document {
    private String address_name;
    private String address_type;
    private String x;
    private String y;
    private Address address;

    public Document(String address_name, String address_type, String x, String y, JSONObject address) {
        try {

            this.address_name = address_name;
            this.address_type = address_type;
            this.x = x;
            this.y = y;
            this.address = new Address(
                    address.get("address_name").toString()
                    , address.get("region_1depth_name").toString()
                    , address.get("region_2depth_name").toString()
                    , address.get("region_3depth_name").toString()
                    , address.get("x").toString()
                    , address.get("y").toString()
            );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Document{" +
                "address_name='" + address_name + '\'' +
                ", address_type='" + address_type + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                ", address=" + address +
                '}';
    }

    public Document(String address_name) {
        this.address_name = address_name;

    }
}
