package com.metacoder.smart_traveler.models;

import java.io.Serializable;

public class ContactModel implements Serializable {
    String area , location  , phone , type  ;

    public ContactModel() {
    }

    public ContactModel(String area, String location, String phone, String type) {
        this.area = area;
        this.location = location;
        this.phone = phone;
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
