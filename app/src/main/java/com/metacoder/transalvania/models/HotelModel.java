package com.metacoder.transalvania.models;

import java.io.Serializable;

public class HotelModel implements Serializable {
    String id, address, admin_id, desc, image, name, range ;
      String current_rating ;

    public HotelModel() {
    }

    public HotelModel(String id, String address, String admin_id, String desc, String image, String name, String range, String current_rating) {
        this.id = id;
        this.address = address;
        this.admin_id = admin_id;
        this.desc = desc;
        this.image = image;
        this.name = name;
        this.range = range;
        this.current_rating = current_rating;
    }

    public String getCurrent_rating() {
        return current_rating;
    }

    public void setCurrent_rating(String current_rating) {
        this.current_rating = current_rating;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }
}
